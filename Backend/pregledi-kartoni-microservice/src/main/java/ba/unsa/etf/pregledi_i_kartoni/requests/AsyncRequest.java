package ba.unsa.etf.pregledi_i_kartoni.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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