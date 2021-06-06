package ba.unsa.etf.doktordetalji.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncRequest {
    private Long id;
    private String ime;
    private String prezime;
    private String datumRodjenja;
    private String adresa;
    private String brojTelefona;
    private String email;
    private String uloga;
    private String akcija;
}
