package ba.unsa.etf.doktordetalji.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdukacijeResponse {
    private String institucija;
    private String odsjek;
    private String stepen;
    private Integer godinaPocetka;
    private Integer godinaZavrsetka;
}
