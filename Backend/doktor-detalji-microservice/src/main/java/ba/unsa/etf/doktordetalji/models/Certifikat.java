package ba.unsa.etf.doktordetalji.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certifikat")
public class Certifikat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Naziv institucije mora biti unesen.")
    @Size(min = 2, message = "Naziv institucije mora biti dug bar dva znaka.")
    private String institucija;

    @NotBlank(message = "Naziv certifikata mora biti unesen.")
    @Size(min = 2, message = "Naziv certifikata mora biti dug bar dva znaka.")
    private String naziv;

    private Integer godinaIzdavanja;

    public Certifikat(String institucija, String naziv, Integer godinaIzdavanja) {
        this.institucija = institucija;
        this.naziv = naziv;
        this.godinaIzdavanja = godinaIzdavanja;
    }

    @JsonIgnore
    @ManyToOne
    private Doktor doktor;

}
