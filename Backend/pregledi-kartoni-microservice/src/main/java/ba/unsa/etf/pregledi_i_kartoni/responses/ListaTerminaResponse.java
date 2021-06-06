package ba.unsa.etf.pregledi_i_kartoni.responses;

import ba.unsa.etf.pregledi_i_kartoni.models.Termin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public class ListaTerminaResponse {
    List<Termin> termini = new ArrayList<>();
}