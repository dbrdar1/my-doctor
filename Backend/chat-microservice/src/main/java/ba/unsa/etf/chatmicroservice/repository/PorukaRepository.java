package ba.unsa.etf.chatmicroservice.repository;

import ba.unsa.etf.chatmicroservice.model.Korisnik;
import ba.unsa.etf.chatmicroservice.model.Poruka;
import ba.unsa.etf.chatmicroservice.dto.PorukaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PorukaRepository extends JpaRepository<Poruka, Long> {

    List<PorukaProjection> findAllByPosiljalacAndPrimalac(Korisnik posiljalac, Korisnik primalac);
    List<PorukaProjection> findAllByPosiljalacAndPrimalacOrPrimalacAndPosiljalac
            (Korisnik posiljalac1, Korisnik primalac1, Korisnik posiljalac2, Korisnik primalac2);
    PorukaProjection findBySadrzaj(String sadrzaj);
}
