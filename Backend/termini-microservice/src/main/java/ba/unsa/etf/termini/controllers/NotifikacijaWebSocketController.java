package ba.unsa.etf.termini.controllers;

import ba.unsa.etf.termini.Requests.DodajNotifikacijuRequest;
import ba.unsa.etf.termini.models.Notifikacija;
import ba.unsa.etf.termini.models.NotifikacijaWebSocket;
import ba.unsa.etf.termini.services.NotifikacijaService;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class NotifikacijaWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotifikacijaService notifikacijaService;

    @MessageMapping("/notifikacije")
    public void processTerminNotification(@Payload NotifikacijaWebSocket notifikacijaWebSocket) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(notifikacijaWebSocket.getRecipientId()),
                "/queue/notifikacije",
                notifikacijaWebSocket
        );
        LocalDateTime danas = LocalDateTime.now(ZoneId.systemDefault());
        String datum = danas.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String post_url = "http://termini/dodaj-notifikaciju";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idKorisnika", notifikacijaWebSocket.getRecipientId());
            jsonObject.put("tekst",notifikacijaWebSocket.getTekst());
            jsonObject.put("datum", datum);
        } catch (Exception e) {
            System.out.println(".");
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
        restTemplate.postForObject(post_url, httpEntity, String.class);
    }

    @MessageMapping("/chat-notifikacije")
    public void processChatNotification(@Payload NotifikacijaWebSocket notifikacijaWebSocket) {
        System.out.println("USAOOOOOOOOOOOOO");
        messagingTemplate.convertAndSendToUser(
                String.valueOf(notifikacijaWebSocket.getRecipientId()),
                "/queue/chat-notifikacije",
                notifikacijaWebSocket
        );
        LocalDateTime danas = LocalDateTime.now(ZoneId.systemDefault());
        String datum = danas.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String post_url = "http://termini/dodaj-notifikaciju";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idKorisnika", notifikacijaWebSocket.getRecipientId());
            jsonObject.put("tekst","Nova poruka od korisnika " + notifikacijaWebSocket.getNaslov());
            jsonObject.put("datum", datum);
        } catch (Exception e) {
            System.out.println(".");
        }

        System.out.println("JSON chaT not"+jsonObject.toString());
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
        restTemplate.postForObject(post_url, httpEntity, String.class);
    }
}
