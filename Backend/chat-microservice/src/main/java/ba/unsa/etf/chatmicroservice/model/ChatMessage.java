package ba.unsa.etf.chatmicroservice.model;

public class ChatMessage {

    private Long senderId;
    private Long recipientId;
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(Long senderId, Long recipientId, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
