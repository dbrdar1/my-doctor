package ba.unsa.etf.defaultgateway.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Table(name = "doktor")
public class Doktor extends Korisnik {

    public Doktor(String ime, String prezime, Date datumRodjenja, String adresa, String brojTelefona, String mail, String korisnickoIme, String lozinka) {
        super(ime, prezime, datumRodjenja, adresa, brojTelefona, mail, korisnickoIme, lozinka);
    }
}
