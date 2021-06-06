package ba.unsa.etf.doktordetalji.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoktorCVResponse {
    private String ime;
    private String prezime;
    private String adresa;
    private String email;
    private String titula;
    private String biografija;
    private Double ocjena;
    private List<EdukacijeResponse> edukacije;
    private List<CertifikatiResponse> certifikati;
}
