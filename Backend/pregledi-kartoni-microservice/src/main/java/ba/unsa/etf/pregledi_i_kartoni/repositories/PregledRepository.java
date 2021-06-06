package ba.unsa.etf.pregledi_i_kartoni.repositories;

import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Pacijent;
import ba.unsa.etf.pregledi_i_kartoni.models.Pregled;
import ba.unsa.etf.pregledi_i_kartoni.models.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PregledRepository extends JpaRepository<Pregled, Long> {

    String filterPregledeUpit = "SELECT p FROM Pregled p WHERE " +
            "(:termin IS NULL OR p.termin = :termin) AND " +
            "(:doktor IS NULL OR p.termin IN (SELECT ter1 FROM Termin ter1 " +
            "WHERE ter1.pacijentDoktor IN (SELECT pd1 FROM PacijentDoktor pd1 WHERE pd1.doktor = :doktor))) AND " +
            "(:pacijent IS NULL OR p.termin IN (SELECT ter2 FROM Termin ter2 " +
            "WHERE ter2.pacijentDoktor IN (SELECT pd2 FROM PacijentDoktor pd2 WHERE pd2.pacijent = :pacijent)))";


    Optional<Pregled> findById(Long id);

    @Query(value = filterPregledeUpit)
    Optional<List<Pregled>> findByQueryPregled(@Param("doktor") Doktor doktor,
                                               @Param("pacijent") Pacijent pacijent,
                                               @Param("termin") Termin termin);


}
