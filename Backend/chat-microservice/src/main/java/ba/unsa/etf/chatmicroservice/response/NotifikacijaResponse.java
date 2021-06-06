package ba.unsa.etf.chatmicroservice.response;

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
public class NotifikacijaResponse {
    private Long id;
    private String naslov;
    private String tekst;
    private Date datum;
    private String vrijeme;
    private Long korisnikId;
}
