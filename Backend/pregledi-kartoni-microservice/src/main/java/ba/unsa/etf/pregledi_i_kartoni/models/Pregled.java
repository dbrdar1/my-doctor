package ba.unsa.etf.pregledi_i_kartoni.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pregled")
public class Pregled {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Pregled mora imati simptome")
    private String simptomi;

    @NotBlank(message = "Pregled mora imati fizikalni pregled")
    private String fizikalniPregled;

    @NotBlank(message = "Pregled mora imati dijagnozu")
    private String dijagnoza;

    @NotBlank(message = "Pregled mora imati tretman")
    private String tretman;

    private String komentar;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="termin_id", referencedColumnName = "id")
    private Termin termin;

    public Pregled(String simptomi, String fizikalniPregled, String dijagnoza, String tretman, String komentar, Termin termin) {
        this.simptomi = simptomi;
        this. fizikalniPregled = fizikalniPregled;
        this.dijagnoza = dijagnoza;
        this.tretman = tretman;
        this.komentar = komentar;
        this.termin = termin;
    }

}
