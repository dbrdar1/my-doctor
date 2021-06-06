package ba.unsa.etf.defaultgateway.models;

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
                "korisnickoIme"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })})
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Korisnik mora imati uneseno ime.")
    @Size(min = 2, message = "Ime mora biti dugo bar dva znaka.")
    private String ime;

    @NotBlank(message = "Korisnik mora imati uneseno prezime.")
    @Size(min = 2, message = "Prezime mora biti dugo bar dva znaka")
    private String prezime;

    @NotNull(message = "Korisnik mora imati unesen datum rodjenja.")
    private Date datumRodjenja;

    @NotBlank(message = "Korisnik mora imati unesenu adresu.")
    private String adresa;

    @NotBlank(message = "Korisnik mora imati unesen broj telefona.")
    @Size(min = 9, max = 13, message = "Telefonski broj korisnika mora biti dug 9-13 cifara.")
    private String brojTelefona;

    @Email(message = "Email mora biti validan")
    @NotBlank(message = "Korisnik mora imati unesen ispravan e-mail.")
    private String email;

    @NotBlank(message = "Korisničko ime mora biti uneseno.")
    @Size(min = 2, message = "Korisnicko ime mora biti dugo bar dva znaka")
    private String korisnickoIme;

    @JsonIgnore
    @NotBlank
    private String lozinka;

    private String resetToken = "";

    public Korisnik(String ime, String prezime, Date datumRodjenja, String adresa, String brojTelefona, String email, String korisnickoIme, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "korisnik_uloga",
            joinColumns = @JoinColumn(name = "korisnik_id"),
            inverseJoinColumns = @JoinColumn(name = "korisnicka_uloga_id"))
    private Set<KorisnickaUloga> uloge = new HashSet<>();
}
