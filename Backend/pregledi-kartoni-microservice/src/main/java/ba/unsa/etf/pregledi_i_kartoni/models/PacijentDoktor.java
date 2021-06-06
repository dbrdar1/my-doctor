package ba.unsa.etf.pregledi_i_kartoni.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pacijent_doktor")
public class PacijentDoktor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Doktor doktor;

    @ManyToOne
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Pacijent pacijent;

    @OneToMany(mappedBy = "pacijentDoktor", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Termin> termini = new ArrayList<>();

    public PacijentDoktor(Doktor doktor, Pacijent pacijent) {
        this.doktor = doktor;
        this.pacijent = pacijent;
    }





}
