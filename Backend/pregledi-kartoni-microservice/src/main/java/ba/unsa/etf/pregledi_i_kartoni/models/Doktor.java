package ba.unsa.etf.pregledi_i_kartoni.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "doktor")
public class Doktor extends Korisnik {
    public Doktor(String ime, String prezime, Date datumRodjenja, String adresa,
                  String brojTelefona, String email) {
        super(ime, prezime, datumRodjenja, adresa, brojTelefona, email);
    }

    public Doktor(Long id, String ime, String prezime, Date datumRodjenja, String adresa,
                  String brojTelefona, String email) {
        super(id, ime, prezime, datumRodjenja, adresa, brojTelefona, email);
    }


    @OneToMany(mappedBy = "doktor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PacijentDoktor> vezeSaPacijentima = new ArrayList<>();
}
