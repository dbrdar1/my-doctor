package ba.unsa.etf.doktordetalji.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrediCertifikatRequest {
    private Long id;
    private String institucija;
    private String naziv;
    private Integer godinaIzdavanja;
}
