package ba.unsa.etf.termini.repositories;

import ba.unsa.etf.termini.dto.TerminProjection;
import ba.unsa.etf.termini.models.PacijentKartonDoktor;
import ba.unsa.etf.termini.models.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Long> {
    List<TerminProjection> findAllByPacijentKartonDoktor(PacijentKartonDoktor pkd);
    Termin findByDatumAndVrijeme(Date d, String s);
    Termin getByVrijeme(String s);
    Termin findByPacijentKartonDoktorAndDatumAndVrijeme(PacijentKartonDoktor p, Date d, String v);
}