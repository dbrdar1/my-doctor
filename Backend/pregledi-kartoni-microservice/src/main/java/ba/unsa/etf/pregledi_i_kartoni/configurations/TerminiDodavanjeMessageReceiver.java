package ba.unsa.etf.pregledi_i_kartoni.configurations;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@RabbitListener(queues = "terminiDodavanje")
public class TerminiDodavanjeMessageReceiver {

    @RabbitHandler
    public void receive(String receivedMessage) {
        System.out.println("Received: " + receivedMessage);
        String post_url = "http://localhost:8082/asyncTermini";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject(receivedMessage);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
        restTemplate.postForObject(post_url, httpEntity, String.class);
    }
}
