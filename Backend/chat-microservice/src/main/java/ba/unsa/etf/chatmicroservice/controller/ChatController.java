package ba.unsa.etf.chatmicroservice.controller;

import ba.unsa.etf.chatmicroservice.model.ChatMessage;
import ba.unsa.etf.chatmicroservice.model.TypingMessage;
import ba.unsa.etf.chatmicroservice.request.DodajPorukuRequest;
import ba.unsa.etf.chatmicroservice.service.PorukaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Controller
public class ChatController {

    @Autowired private SimpMessagingTemplate messagingTemplate;

    @Autowired private PorukaService porukaService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()),
                "/queue/messages",
                chatMessage
        );
        String timestampAkcijeNow =
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Sarajevo")).toInstant()).toString();
        DodajPorukuRequest dodajPorukuRequest = new DodajPorukuRequest(
                chatMessage.getContent(),
                timestampAkcijeNow,
                chatMessage.getSenderId(),
                chatMessage.getRecipientId()
        );
        porukaService.dodajPoruku(dodajPorukuRequest);
    }

    @MessageMapping("/isTyping")
    public void processTyping(@Payload TypingMessage typingMessage) {
        messagingTemplate.convertAndSendToUser(
                typingMessage.getRecipientId(),
                "/queue/typingMessages",
                typingMessage
        );
    }

}
