package ba.unsa.etf.doktordetalji.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KorisnikResponse {
    private Long id;
    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private String adresa;
    private String brojTelefona;
    private String email;
}
