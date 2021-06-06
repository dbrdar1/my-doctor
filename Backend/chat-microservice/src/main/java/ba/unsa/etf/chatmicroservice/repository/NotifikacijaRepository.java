package ba.unsa.etf.chatmicroservice.repository;

import ba.unsa.etf.chatmicroservice.model.Korisnik;
import ba.unsa.etf.chatmicroservice.model.Notifikacija;
import ba.unsa.etf.chatmicroservice.dto.NotifikacijaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifikacijaRepository extends JpaRepository<Notifikacija, Long>{

    List<NotifikacijaProjection> findAllByKorisnik(Korisnik korisnik);
    NotifikacijaProjection findByNaslov(String naslov);
}
