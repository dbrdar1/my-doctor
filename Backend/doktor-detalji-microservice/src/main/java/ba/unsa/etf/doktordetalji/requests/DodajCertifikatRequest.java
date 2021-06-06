package ba.unsa.etf.doktordetalji.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajCertifikatRequest {
    private Long idDoktora;
    private String institucija;
    private String naziv;
    private Integer godinaIzdavanja;
}
