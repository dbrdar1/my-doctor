package ba.unsa.etf.chatmicroservice.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface PorukaProjection {

    Long getId();
    String getSadrzaj();
    String getTimestamp();

    @Value("#{target.getPosiljalac().getId()}")
    Long getPosiljalacId();

    @Value("#{target.getPrimalac().getId()}")
    Long getPrimalacId();
}
