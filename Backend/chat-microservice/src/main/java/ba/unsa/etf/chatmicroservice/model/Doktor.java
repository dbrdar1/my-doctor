package ba.unsa.etf.chatmicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Table(name = "doktor")
@JsonIgnoreProperties({"notifikacijas", "razgovors1", "razgovors2"})
public class Doktor extends Korisnik {

    public Doktor(String ime, String prezime, Date datumRodjenja, String adresa, String brojTelefona, String email) {
        super(ime, prezime, datumRodjenja, adresa, brojTelefona, email);
    }

    public Doktor(
            Long id,
            @NotBlank(message = "Korisnik mora imati ime") String ime,
            @NotBlank(message = "Korisnik mora imati prezime") String prezime,
            @NotNull(message = "Korisnik mora imati datum rodjenja") Date datumRodjenja,
            @NotBlank(message = "Korisnik mora imati adresu") String adresa,
            @NotBlank(message = "Korisnik mora imati broj telefona") String brojTelefona,
            @NotBlank(message = "Korisnik mora imati email") @Email(message = "Email mora biti validan") String email
    ) {
        super(id, ime, prezime, datumRodjenja, adresa, brojTelefona, email);
    }
}
