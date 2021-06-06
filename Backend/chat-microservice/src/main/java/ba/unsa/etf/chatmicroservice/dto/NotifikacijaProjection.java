package ba.unsa.etf.chatmicroservice.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface NotifikacijaProjection {

    Long getId();
    String getNaslov();
    String getTekst();
    Date getDatum();
    String getVrijeme();

    @Value("#{target.getKorisnik().getId()}")
    Long getKorisnikId();
}
