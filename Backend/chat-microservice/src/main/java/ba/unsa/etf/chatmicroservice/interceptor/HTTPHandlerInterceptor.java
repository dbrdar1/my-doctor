package ba.unsa.etf.chatmicroservice.interceptor;

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
        if (response.getStatus() != HttpStatus.OK.value())
            tipOdgovoraNaAkciju = ActionRequest.TipOdgovoraNaAkciju.GRESKA;
        if (request.getMethod().equals("GET")) {
            ActionRequest.TipAkcije tipAkcijeGet = ActionRequest.TipAkcije.GET;
            if (request.getServletPath().contains("/korisnici"))
                saveEventUsingGRPC("korisnici", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/doktori"))
                saveEventUsingGRPC("doktori", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/pacijenti"))
                saveEventUsingGRPC("pacijenti", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/notifikacije"))
                saveEventUsingGRPC("notifikacije", tipAkcijeGet, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/poruke"))
                saveEventUsingGRPC("poruke", tipAkcijeGet, tipOdgovoraNaAkciju);
        }
        if (request.getMethod().equals("POST")) {
            ActionRequest.TipAkcije tipAkcijeCreate = ActionRequest.TipAkcije.CREATE;
            if (request.getServletPath().contains("/dodaj-notifikaciju"))
                saveEventUsingGRPC("notifikacije", tipAkcijeCreate, tipOdgovoraNaAkciju);
            if (request.getServletPath().contains("/dodaj-poruku"))
                saveEventUsingGRPC("poruke", tipAkcijeCreate, tipOdgovoraNaAkciju);
        }
        if (request.getMethod().equals("DELETE")) {
            ActionRequest.TipAkcije tipAkcijeDelete = ActionRequest.TipAkcije.DELETE;
            if (request.getServletPath().contains("/notifikacije"))
                saveEventUsingGRPC("notifikacije", tipAkcijeDelete, tipOdgovoraNaAkciju);
        }
    }

    private void saveEventUsingGRPC(String resurs,
                                   ActionRequest.TipAkcije tipAkcije,
                                   ActionRequest.TipOdgovoraNaAkciju tipOdgovoraNaAkciju) {
        String timestampAkcijeNow =
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Sarajevo")).toInstant()).toString();
        String nazivMikroservisaChat =
                "chat";
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