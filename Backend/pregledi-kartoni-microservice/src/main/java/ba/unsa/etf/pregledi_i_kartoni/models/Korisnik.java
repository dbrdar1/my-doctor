package ba.unsa.etf.pregledi_i_kartoni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "korisnik", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })})


@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Korisnik {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 2, message = "Ime mora biti dugo bar dva znaka")
    @NotBlank(message = "Korisnik mora imati uneseno ime")
    private String ime;

    @Size(min = 2, message = "PrezIme mora biti dugo bar dva znaka")
    @NotBlank(message = "Korisnik mora imati uneseno prezime")
    private String prezime;

    @NotNull(message = "Korisnik mora imati unesen datum rodjenja")
    private Date datumRodjenja;

    @NotBlank(message = "Korisnik mora imati unesenu adresu")
    private String adresa;

    @NotBlank(message = "Korisnik mora imati unesen broj telefona")
    @Size(min=9, max=13, message = "Telefonski broj korisnika mora biti dug 9-13 cifara")
    private String brojTelefona;

    @NotBlank(message = "Korisnik mora imati unesenu e-mail adresu")
    @Email(message = "Korisnik mora imati unesen ispravan e-mail")
    private String email;

    public Korisnik(String ime, String prezime, Date datumRodjenja, String adresa, String brojTelefona, String email) {
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.email = email;
    }
    /*

    public Korisnik(Long id, String ime, String prezime, Date datumRodjenja, String adresa, String brojTelefona, String email) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.email = email;
    }

     */

}

