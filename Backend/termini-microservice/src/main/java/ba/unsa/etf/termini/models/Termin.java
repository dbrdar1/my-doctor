package ba.unsa.etf.termini.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Data
@Table(name = "termin")
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Termin mora imati datum")
    private Date datum;

    @NotBlank(message = "Termin mora imati vrijeme")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Vrijeme mora biti zadano u ispravnom formatu (HH:MM)")
    private String vrijeme;

    @ManyToOne
    @JsonIgnore
    private PacijentKartonDoktor pacijentKartonDoktor;

    public Termin(Date datumPregleda, String vrijemePregleda, PacijentKartonDoktor pacijentKartonDoktor) {
        this.datum = datumPregleda;
        this.vrijeme = vrijemePregleda;
        this.pacijentKartonDoktor = pacijentKartonDoktor;
    }

    public Termin() {

    }

}
