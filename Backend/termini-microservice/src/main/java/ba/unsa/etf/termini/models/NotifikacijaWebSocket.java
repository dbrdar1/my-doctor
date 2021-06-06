package ba.unsa.etf.termini.models;

public class NotifikacijaWebSocket {
    private Long senderId;
    private Long recipientId;
    private String naslov;
    private String tekst;
    public NotifikacijaWebSocket(Long senderId, Long recipientId, String naslov, String tekst) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.naslov = naslov;
        this.tekst = tekst;
    }

    public NotifikacijaWebSocket() {
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

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
}
