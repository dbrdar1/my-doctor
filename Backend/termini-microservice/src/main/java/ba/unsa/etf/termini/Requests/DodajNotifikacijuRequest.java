package ba.unsa.etf.termini.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajNotifikacijuRequest {
    private String tekst;
    private String datum;
    private Long idKorisnika;
}
