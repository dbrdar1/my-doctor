package ba.unsa.etf.pregledi_i_kartoni.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Data
@AllArgsConstructor
@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public class TerminResponse {

    private Long id;
    private Date datumPregleda;
    private String vrijemePregleda;
    private Long idPacijentDoktor;

}
