package ba.unsa.etf.pregledi_i_kartoni.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@AllArgsConstructor
@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public class KartonResponse {

    private Long id;
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
