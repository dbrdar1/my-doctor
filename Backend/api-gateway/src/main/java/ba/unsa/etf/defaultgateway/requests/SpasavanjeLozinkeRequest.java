package ba.unsa.etf.defaultgateway.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpasavanjeLozinkeRequest {
    private String informacijaOKorisnku;
    private String novaLozinka;
}
