package ba.unsa.etf.termini.repositories;

import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.models.Notifikacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifikacijaRepository extends JpaRepository<Notifikacija, Long> {
    List<Notifikacija> findAllByKorisnik(Korisnik korisnik);
}
