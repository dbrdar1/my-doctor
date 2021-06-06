package ba.unsa.etf.defaultgateway.repositories;

import ba.unsa.etf.defaultgateway.models.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoktorRepository extends JpaRepository<Doktor, Long> {

    Doktor findByIme(String ime);

}