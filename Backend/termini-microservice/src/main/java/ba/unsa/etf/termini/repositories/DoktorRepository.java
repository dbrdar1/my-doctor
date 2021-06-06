package ba.unsa.etf.termini.repositories;

import ba.unsa.etf.termini.models.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoktorRepository extends JpaRepository<Doktor, Long> {

    Doktor findByIme(String test2);
}