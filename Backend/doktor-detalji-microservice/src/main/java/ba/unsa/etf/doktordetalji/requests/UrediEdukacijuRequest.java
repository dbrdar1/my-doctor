package ba.unsa.etf.doktordetalji.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrediEdukacijuRequest {
    private Long id;
    private String institucija;
    private String odsjek;
    private String stepen;
    private Integer godinaPocetka;
    private Integer godinaZavrsetka;
    private String grad;
    private String drzava;
}
