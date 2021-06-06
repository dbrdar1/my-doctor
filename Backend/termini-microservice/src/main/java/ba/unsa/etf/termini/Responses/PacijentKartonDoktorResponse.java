package ba.unsa.etf.termini.Responses;

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
public class PacijentKartonDoktorResponse {
    Long id;
}

