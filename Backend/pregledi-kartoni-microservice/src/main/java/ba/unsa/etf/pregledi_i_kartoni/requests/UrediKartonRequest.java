package ba.unsa.etf.pregledi_i_kartoni.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrediKartonRequest {

    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private String adresa;
    private String brojTelefona;
    private String email;
    private String spol;
    private double visina;
    private double tezina;
    private String krvnaGrupa;
    private String hronicneBolesti;
    private String hronicnaTerapija;

}
