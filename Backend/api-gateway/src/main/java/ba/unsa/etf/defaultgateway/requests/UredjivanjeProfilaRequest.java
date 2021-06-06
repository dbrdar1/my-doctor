package ba.unsa.etf.defaultgateway.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UredjivanjeProfilaRequest {
    Long id;
    String ime;
    String prezime;
    Date datumRodjenja;
    String adresa;
    String brojTelefona;
    String email;
    String korisnickoIme;
}
