package ba.unsa.etf.pregledi_i_kartoni.repositories;

import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Korisnik;
import ba.unsa.etf.pregledi_i_kartoni.models.Pacijent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoktorRepository extends JpaRepository<Doktor, Long> {
    Optional<Doktor> findById(Long id);
    Optional<Doktor> findByIme(String ime);
    Optional<Doktor> findByPrezime(String prezime);

    String filterDoktoriPacijentaUpit = "SELECT d FROM Doktor d, PacijentDoktor pd WHERE " +
            "d.id IN " +
            "(" +
            "SELECT pd.doktor.id FROM PacijentDoktor pd WHERE " +
            "(:idPacijent IS NULL OR pd.pacijent.id = :idPacijent)" +
            ")";


    @Query(value = filterDoktoriPacijentaUpit)
    Optional<List<Doktor>> findDoktoriPacijenta(@Param("idPacijent") Long idPacijent);


}
