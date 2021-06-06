package ba.unsa.etf.chatmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifikacija")
public class Notifikacija {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Notifikacija mora imati naslov")
    private String naslov;

    @Size(min = 10, max = 500, message = "Tekst notifikacije mora biti izmedju 10 i 500 karaktera")
    private String tekst;

    @NotNull(message = "Notifikacija mora imati datum")
    private Date datum;

    @NotBlank(message = "Notifikacija mora imati vrijeme")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$",
            message = "Vrijeme mora biti zadano u ispravnom formatu (HH:MM)")
    private String vrijeme;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Korisnik korisnik;

    public Notifikacija(String naslov, String tekst, Date datum, String vrijeme, Korisnik korisnik) {
        this.naslov = naslov;
        this.tekst = tekst;
        this.datum = datum;
        this.vrijeme = vrijeme;
        this.korisnik = korisnik;
    }
}
