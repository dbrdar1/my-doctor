package ba.unsa.etf.termini.repositories;

import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.models.PacijentKartonDoktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacijentKartonDoktorRepository extends JpaRepository<PacijentKartonDoktor, Long> {
    List<PacijentKartonDoktor> findAllByDoktor(Doktor doktor);
    List<PacijentKartonDoktor> findAllByPacijent(Pacijent pacijent);
    Optional<PacijentKartonDoktor>  findByPacijentAndDoktor(Pacijent pacijent, Doktor doktor);

}