package ba.unsa.etf.chatmicroservice.repository;

import ba.unsa.etf.chatmicroservice.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    Korisnik findByIme(String ime);
}
