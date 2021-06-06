package ba.unsa.etf.chatmicroservice.model;

public class TypingMessage {

    private String senderId;
    private String recipientId;

    public TypingMessage() {
    }

    public TypingMessage(String senderId, String recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
