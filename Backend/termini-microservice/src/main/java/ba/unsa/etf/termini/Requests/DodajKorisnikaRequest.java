package ba.unsa.etf.termini.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajKorisnikaRequest {
    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private String adresa;
    private String brojTelefona;
    private String email;
}
