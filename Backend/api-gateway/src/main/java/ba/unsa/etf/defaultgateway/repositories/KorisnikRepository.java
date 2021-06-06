package ba.unsa.etf.defaultgateway.repositories;

import ba.unsa.etf.defaultgateway.models.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long>{

    Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);

    Optional<Korisnik> findByEmail(String email);

    Optional<Korisnik> findByBrojTelefona(String broj);

    Optional<Korisnik> findById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByKorisnickoIme(String username);

    Boolean existsByBrojTelefona(String broj);

}
