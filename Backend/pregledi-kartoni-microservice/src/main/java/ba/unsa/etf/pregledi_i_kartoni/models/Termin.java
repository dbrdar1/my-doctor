package ba.unsa.etf.pregledi_i_kartoni.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Data
@Table(name = "termin")
@NoArgsConstructor
public class Termin {

    @Id
    private Long id;

    @NotNull(message = "Termin mora imati datum")
    private Date datumPregleda;

    @NotBlank(message = "Termin mora imati vrijeme")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Vrijeme mora biti zadano u ispravnom formatu (HH:MM)")
    private String vrijemePregleda;

    @ManyToOne
    @JsonIgnore
    private PacijentDoktor pacijentDoktor;


    @OneToOne(mappedBy = "termin")
    @JsonIgnore
    private Pregled pregled;


    public Termin(Long id, Date datumPregleda, String vrijemePregleda, PacijentDoktor pacijentDoktor) {
        this.id = id;
        this.datumPregleda = datumPregleda;
        this.vrijemePregleda = vrijemePregleda;
        this.pacijentDoktor = pacijentDoktor;
    }

}
