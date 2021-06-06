package ba.unsa.etf.doktordetalji.repositories;

import ba.unsa.etf.doktordetalji.models.Certifikat;
import ba.unsa.etf.doktordetalji.models.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertifikatRepository extends JpaRepository<Certifikat, Long> {
    List<Certifikat> findByDoktor(Doktor doktor);

    Certifikat findByNaziv(String naziv);
}
