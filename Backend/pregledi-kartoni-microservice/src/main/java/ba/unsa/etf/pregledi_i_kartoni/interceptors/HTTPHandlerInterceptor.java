package ba.unsa.etf.pregledi_i_kartoni.interceptors;

import ba.unsa.etf.grpc.ActionRequest;
import ba.unsa.etf.grpc.ActionResponse;
import ba.unsa.etf.grpc.SystemEventsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class HTTPHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        ActionRequest.TipOdgovoraNaAkciju tipOdgovoraNaAkciju = ActionRequest.TipOdgovoraNaAkciju.USPJEH;
        System.out.println("-------------------");
        System.out.println(response.getStatus());
        System.out.println("-------------------");

        if (response.getStatus() != HttpStatus.OK.value()) {
            tipOdgovoraNaAkciju = ActionRequest.TipOdgovoraNaAkciju.GRESKA;
            System.out.println("------- RESPONSE STATUS NOT OK ------------ " + ActionRequest.TipOdgovoraNaAkciju.GRESKA + " ------- RESPONSE STATUS NOT OK ------------");
        }
        if (request.getMethod().equals("GET")) {
            ActionRequest.TipAkcije tipAkcijeGet = ActionRequest.TipAkcije.GET;
            if (request.getServletPath().contains("/pregled") || request.getServletPath().contains("/svi-pregledi"))
                saveEventUsingGRPC("pregled", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/karton") || request.getServletPath().contains("/svi-kartoni"))
                saveEventUsingGRPC("karton", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/pacijent") || request.getServletPath().contains("/svi-pacijenti"))
                saveEventUsingGRPC("pacijent", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/termin") || request.getServletPath().contains("/svi-termini"))
                saveEventUsingGRPC("termin", tipAkcijeGet, tipOdgovoraNaAkciju);
        }
        if (request.getMethod().equals("POST")) {
            ActionRequest.TipAkcije tipAkcijeCreate = ActionRequest.TipAkcije.CREATE;
            if (request.getServletPath().contains("/dodaj-pregled"))
                saveEventUsingGRPC("pregled", tipAkcijeCreate, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/dodaj-termin"))
                saveEventUsingGRPC("termin", tipAkcijeCreate, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/dodaj-pacijenta"))
                saveEventUsingGRPC("pacijent", tipAkcijeCreate, tipOdgovoraNaAkciju);
        }
        if (request.getMethod().equals("PUT")) {
            ActionRequest.TipAkcije tipAkcijeCreate = ActionRequest.TipAkcije.UPDATE;
            if (request.getServletPath().contains("/karton"))
                saveEventUsingGRPC("pacijent", tipAkcijeCreate, tipOdgovoraNaAkciju);
        }
        if (request.getMethod().equals("DELETE")) {
            ActionRequest.TipAkcije tipAkcijeDelete = ActionRequest.TipAkcije.DELETE;
            if (request.getServletPath().contains("/pregled"))
                saveEventUsingGRPC("pregled", tipAkcijeDelete, tipOdgovoraNaAkciju);
        }
    }

    private void saveEventUsingGRPC(String resurs,
                                    ActionRequest.TipAkcije tipAkcije,
                                    ActionRequest.TipOdgovoraNaAkciju tipOdgovoraNaAkciju) {
        String timestampAkcijeNow =
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Sarajevo")).toInstant()).toString();
        String nazivMikroservisaChat =
                "pregledi-i-kartoni";
        try {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8866)
                    .usePlaintext()
                    .build();
            SystemEventsServiceGrpc.SystemEventsServiceBlockingStub stub
                    = SystemEventsServiceGrpc.newBlockingStub(channel);
            ActionResponse actionResponse = stub.registrujAkciju(ActionRequest.newBuilder()
                    .setTimestampAkcije(timestampAkcijeNow)
                    .setNazivMikroservisa(nazivMikroservisaChat)
                    .setResurs(resurs)
                    .setTipAkcije(tipAkcije)
                    .setTipOdgovoraNaAkciju(tipOdgovoraNaAkciju)
                    .build());
            System.out.println("Response received from server:\n" + actionResponse);
            channel.shutdown();
        }
        catch (Exception e) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("system-events", 8866)
                    .usePlaintext()
                    .build();
            SystemEventsServiceGrpc.SystemEventsServiceBlockingStub stub
                    = SystemEventsServiceGrpc.newBlockingStub(channel);
            ActionResponse actionResponse = stub.registrujAkciju(ActionRequest.newBuilder()
                    .setTimestampAkcije(timestampAkcijeNow)
                    .setNazivMikroservisa(nazivMikroservisaChat)
                    .setResurs(resurs)
                    .setTipAkcije(tipAkcije)
                    .setTipOdgovoraNaAkciju(tipOdgovoraNaAkciju)
                    .build());
            System.out.println("Response received from server:\n" + actionResponse);
            channel.shutdown();
        }
    }
}