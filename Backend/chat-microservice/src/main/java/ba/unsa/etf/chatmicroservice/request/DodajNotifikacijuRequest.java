package ba.unsa.etf.chatmicroservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajNotifikacijuRequest {

    private Long idKorisnika;
    private String naslov;
    private String tekst;
    private Date datum;
    private String vrijeme;
}
