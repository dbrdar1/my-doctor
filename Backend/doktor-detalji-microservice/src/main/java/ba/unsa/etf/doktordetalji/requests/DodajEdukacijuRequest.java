package ba.unsa.etf.doktordetalji.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajEdukacijuRequest {
    private Long idDoktora;
    private String institucija;
    private String odsjek;
    private String stepen;
    private Integer godinaPocetka;
    private Integer godinaZavrsetka;
    private String grad;
    private String drzava;
}
