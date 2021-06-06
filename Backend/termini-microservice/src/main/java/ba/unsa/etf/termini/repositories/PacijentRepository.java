package ba.unsa.etf.termini.repositories;

import ba.unsa.etf.termini.models.Pacijent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacijentRepository extends JpaRepository<Pacijent, Long>{

    Pacijent findByIme(String test1);
}
