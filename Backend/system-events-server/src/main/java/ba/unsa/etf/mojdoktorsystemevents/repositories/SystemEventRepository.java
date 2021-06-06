package ba.unsa.etf.mojdoktorsystemevents.repositories;

import ba.unsa.etf.mojdoktorsystemevents.models.SystemEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemEventRepository extends JpaRepository<SystemEvent, Long> { }
