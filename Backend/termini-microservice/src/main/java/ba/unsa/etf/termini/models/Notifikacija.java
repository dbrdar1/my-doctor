package ba.unsa.etf.termini.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Data
@Table(name = "notifikacija")
public class Notifikacija {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tekst;
    private String datum;
    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    @JsonIgnore
    private Korisnik korisnik;

    public Notifikacija(String tekst, String datum, Korisnik korisnik) {
        this.tekst = tekst;
        this.datum = datum;
        this.korisnik = korisnik;
    }

    public Notifikacija() {

    }
}
