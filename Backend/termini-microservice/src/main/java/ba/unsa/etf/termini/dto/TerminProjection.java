package ba.unsa.etf.termini.dto;
import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.models.Pacijent;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface TerminProjection {
    Long getId();
    Date getDatum();
    String getVrijeme();

    @Value("#{target.getPacijentKartonDoktor().getPacijent()}")
    Pacijent getPacijent();

    @Value("#{target.getPacijentKartonDoktor().getDoktor()}")
    Doktor getDoktor();
}
