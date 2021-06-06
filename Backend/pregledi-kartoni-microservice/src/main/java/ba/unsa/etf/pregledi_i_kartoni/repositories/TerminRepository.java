package ba.unsa.etf.pregledi_i_kartoni.repositories;

import ba.unsa.etf.pregledi_i_kartoni.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Long> {

    String terminiPacijentaUpit = "SELECT t FROM Termin t WHERE " +
            "t.pacijentDoktor IN (SELECT pd FROM PacijentDoktor pd WHERE pd.pacijent = :pacijent)";

    Optional<Termin> findById(Long id);

    @Query(value = terminiPacijentaUpit)
    Optional<List<Termin>> findByPacijent(@Param("pacijent") Pacijent pacijent);
}
