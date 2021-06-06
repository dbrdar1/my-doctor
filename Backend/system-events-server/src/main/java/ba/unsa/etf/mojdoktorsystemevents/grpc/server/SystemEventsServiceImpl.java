package ba.unsa.etf.mojdoktorsystemevents.grpc.server;

import ba.unsa.etf.grpc.ActionRequest;
import ba.unsa.etf.grpc.ActionResponse;
import ba.unsa.etf.grpc.SystemEventsServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class SystemEventsServiceImpl extends SystemEventsServiceGrpc.SystemEventsServiceImplBase {

    @Override
    public void registrujAkciju(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);
        String post_url = "http://localhost:8867/system-events";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timestampAkcije", request.getTimestampAkcije());
            jsonObject.put("nazivMikroservisa", request.getNazivMikroservisa());
            jsonObject.put("resurs", request.getResurs());
            jsonObject.put("tipAkcije", request.getTipAkcije());
            jsonObject.put("tipOdgovoraNaAkciju", request.getTipOdgovoraNaAkciju());
        } catch (Exception e) {
            System.out.println(".");
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
        restTemplate.postForObject(post_url, httpEntity, String.class);
        String poruka = "Uspjesno sacuvana akcija!";
        ActionResponse response = ActionResponse.newBuilder().setPoruka(poruka).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
