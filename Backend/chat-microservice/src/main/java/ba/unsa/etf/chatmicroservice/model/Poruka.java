package ba.unsa.etf.chatmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "poruka")
public class Poruka {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Poruka mora imati sadrzaj")
    private String sadrzaj;

    @NotBlank(message = "Poruka mora imati datum i vrijeme")
    private String timestamp;

    @ManyToOne
    @JoinColumn(name = "posiljalac_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Korisnik posiljalac;

    @ManyToOne
    @JoinColumn(name = "primalac_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Korisnik primalac;

    public Poruka(String sadrzaj, String timestamp, Korisnik posiljalac, Korisnik primalac) {
        this.sadrzaj = sadrzaj;
        this.timestamp = timestamp;
        this.posiljalac = posiljalac;
        this.primalac = primalac;
    }
}
