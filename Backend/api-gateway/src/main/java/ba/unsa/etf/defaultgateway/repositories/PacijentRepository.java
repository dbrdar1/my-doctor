package ba.unsa.etf.defaultgateway.repositories;

import ba.unsa.etf.defaultgateway.models.Pacijent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacijentRepository extends JpaRepository<Pacijent, Long> {
}