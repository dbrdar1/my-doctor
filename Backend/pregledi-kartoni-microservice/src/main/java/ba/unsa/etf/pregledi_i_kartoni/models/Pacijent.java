package ba.unsa.etf.pregledi_i_kartoni.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pacijent")
public class Pacijent extends Korisnik{

    @Pattern(regexp = "muški|ženski", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Spol može biti samo 'muški' ili 'ženski'")
    private String spol;

    // visina u metrima
    @DecimalMin(value = "0.0", message = "Visina ne može biti negativna!")
    @DecimalMax(value = "3.0", message = "Visina ne može biti veća od 3m!")
    private double visina;

    @DecimalMin(value = "0.0", message = "Težina ne može biti negativna!")
    private double tezina;

    @Pattern(regexp = "^(A|B|AB|0)[+-]$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Nevalidna krvna grupa")
    private String krvnaGrupa;

    private String hronicneBolesti;
    private String hronicnaTerapija;


    public Pacijent(String ime, String prezime, Date datumRodjenja, String adresa,
                    String brojTelefona, String mail, String spol, double visina,
                    double tezina, String krvnaGrupa, String hronicneBolesti,
                    String hronicnaTerapija) {

        super(ime, prezime, datumRodjenja, adresa, brojTelefona, mail);
        this.spol = spol;
        this.visina = visina;
        this.tezina = tezina;
        this.krvnaGrupa = krvnaGrupa;
        this.hronicneBolesti = hronicneBolesti;
        this.hronicnaTerapija = hronicnaTerapija;
    }

    public Pacijent(String ime, String prezime, Date datumRodjenja, String adresa,
                    String brojTelefona, String mail) {
        super(ime, prezime, datumRodjenja, adresa, brojTelefona, mail);
    }


    public Pacijent(Long id, String ime, String prezime, Date datumRodjenja, String adresa,
                    String brojTelefona, String mail) {
        super(id, ime, prezime, datumRodjenja, adresa, brojTelefona, mail);
    }


    @OneToMany(mappedBy = "pacijent", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PacijentDoktor> vezeSaDoktorima = new ArrayList<>();

}

