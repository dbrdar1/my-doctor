package ba.unsa.etf.pregledi_i_kartoni.configurations;

import ba.unsa.etf.pregledi_i_kartoni.repositories.DoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.responses.KorisnikResponse;
import ba.unsa.etf.pregledi_i_kartoni.services.DoktorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RabbitListener(queues = "korisnikQueue2")
public class NoviKorisnikMessageReceiver {

    @RabbitHandler
    public void receive(String receivedMessage) {
        System.out.println("Received: " + receivedMessage);
        String post_url = "http://localhost:8082/async";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject(receivedMessage);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
        restTemplate.postForObject(post_url, httpEntity, String.class);
    }
}