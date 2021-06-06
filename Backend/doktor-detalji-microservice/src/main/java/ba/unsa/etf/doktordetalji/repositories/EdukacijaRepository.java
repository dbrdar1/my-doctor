package ba.unsa.etf.doktordetalji.repositories;

import ba.unsa.etf.doktordetalji.models.Doktor;
import ba.unsa.etf.doktordetalji.models.Edukacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdukacijaRepository extends JpaRepository<Edukacija, Long> {
    List<Edukacija> findByDoktor(Doktor doktor);

    Edukacija findByInstitucija(String naziv);
}
