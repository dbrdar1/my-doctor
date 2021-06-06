package ba.unsa.etf.chatmicroservice.repository;

import ba.unsa.etf.chatmicroservice.model.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoktorRepository extends JpaRepository<Doktor, Long> {

}