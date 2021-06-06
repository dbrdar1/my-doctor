package ba.unsa.etf.doktordetalji.repositories;

import ba.unsa.etf.doktordetalji.models.Ocjena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcjenaRepository extends JpaRepository<Ocjena, Long> {
}
