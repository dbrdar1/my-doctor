package ba.unsa.etf.doktordetalji.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "korisnik", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })})
@Inheritance(strategy = InheritanceType.JOINED)
public class Korisnik {
    @Id
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

    public Korisnik(Long id, String ime, String prezime, Date datumRodjenja, String adresa, String brojTelefona, String email) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.email = email;
    }
}
