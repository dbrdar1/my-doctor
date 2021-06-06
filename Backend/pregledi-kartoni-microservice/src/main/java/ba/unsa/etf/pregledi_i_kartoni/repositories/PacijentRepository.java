package ba.unsa.etf.pregledi_i_kartoni.repositories;

import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Pacijent;
import ba.unsa.etf.pregledi_i_kartoni.models.Pregled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacijentRepository extends JpaRepository<Pacijent, Long> {


    String filterKartoneUpit = "SELECT p FROM Pacijent p WHERE " +
            "p.id IN " +
            "(SELECT k.id FROM Korisnik k WHERE (:ime IS NULL OR k.ime = :ime) AND " +
            "(:prezime IS NULL OR k.prezime = :prezime)) AND " +
            "(:spol IS NULL OR p.spol = :spol) AND " +
            "(:krvnaGrupa IS NULL OR p.krvnaGrupa = :krvnaGrupa) AND " +
            "(:hronicneBolesti IS NULL OR p.hronicneBolesti = :hronicneBolesti) AND " +
            "(:hronicnaTerapija IS NULL OR p.hronicnaTerapija = :hronicnaTerapija)";

    String filterPacijenteUpit = "SELECT p FROM Pacijent p WHERE " +
            "p.id IN " +
            "(SELECT k.id FROM Korisnik k WHERE (:ime IS NULL OR k.ime = :ime) AND " +
            "(:prezime IS NULL OR k.prezime = :prezime))";

    String filterPacijentiDoktoraUpit = "SELECT p FROM Pacijent p, PacijentDoktor pd WHERE " +
            "p.id IN " +
            "(" +
            "SELECT k.id FROM Korisnik k WHERE " +
            "(:ime IS NULL OR k.ime = :ime) AND " +
            "(:prezime IS NULL OR k.prezime = :prezime)" +
            ")" +
            " AND " +
            "(p.id IN " +
            "(" +
            "SELECT pd.pacijent.id FROM PacijentDoktor pd WHERE " +
            "(:idDoktor IS NULL OR pd.doktor.id = :idDoktor)" +
            ")" +
            ")";



    Optional<Pacijent> findById(Long id);
    Optional<Pacijent> findByIme(String ime);
    Optional<Pacijent> findByPrezime(String prezime);

    @Query(value = filterKartoneUpit)
    Optional<List<Pacijent>> findByQueryKarton(@Param("ime") String ime,
                                         @Param("prezime") String prezime,
                                         @Param("spol") String spol,
                                         @Param("krvnaGrupa") String krvnaGrupa,
                                         @Param("hronicneBolesti")String hronicneBolesti,
                                         @Param("hronicnaTerapija") String hronicnaTerapija
    );

    @Query(value = filterPacijenteUpit)
    Optional<List<Pacijent>> findByQueryPacijent(@Param("ime") String ime,
                                                 @Param("prezime") String prezime
    );

    @Query(value = filterPacijentiDoktoraUpit)
    Optional<List<Pacijent>> findPacijentiDoktoraFiltrirano(@Param("idDoktor") Long idDoktor,
                                                            @Param("ime") String ime,
                                                            @Param("prezime") String prezime
    );



}
