package ba.unsa.etf.chatmicroservice.repository;

import ba.unsa.etf.chatmicroservice.model.Pacijent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacijentRepository extends JpaRepository<Pacijent, Long> {
}