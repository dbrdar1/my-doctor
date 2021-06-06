package ba.unsa.etf.pregledi_i_kartoni.repositories;

import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Pacijent;
import ba.unsa.etf.pregledi_i_kartoni.models.PacijentDoktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacijentDoktorRepository extends JpaRepository<PacijentDoktor, Long> {
    Optional<PacijentDoktor> findById(Long id);
    Optional<PacijentDoktor> findByDoktor(Doktor doktor);
    Optional<PacijentDoktor> findByPacijent(Pacijent pacijent);

    Optional<PacijentDoktor>  findByPacijentAndDoktor(Pacijent pacijent, Doktor doktor);
}
