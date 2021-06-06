package ba.unsa.etf.doktordetalji.repositories;

import ba.unsa.etf.doktordetalji.models.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoktorRepository extends JpaRepository<Doktor, Long>, DoktorFilterRepository {
    Doktor findByIme(String ime);
}
