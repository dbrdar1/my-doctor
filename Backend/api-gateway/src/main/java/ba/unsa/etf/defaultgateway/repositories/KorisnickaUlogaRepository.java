package ba.unsa.etf.defaultgateway.repositories;

import ba.unsa.etf.defaultgateway.models.KorisnickaUloga;
import ba.unsa.etf.defaultgateway.models.NazivKorisnickeUloge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KorisnickaUlogaRepository extends JpaRepository<KorisnickaUloga, Long> {
    KorisnickaUloga findByNazivKorisnickeUloge(NazivKorisnickeUloge naziv);
}
