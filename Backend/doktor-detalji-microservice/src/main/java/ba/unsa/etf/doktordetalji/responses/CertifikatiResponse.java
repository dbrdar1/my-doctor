package ba.unsa.etf.doktordetalji.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertifikatiResponse {
    private String institucija;
    private String naziv;
    private Integer godinaIzdavanja;
}
