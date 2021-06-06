package ba.unsa.etf.defaultgateway.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpasiLozinkuRequest {
    private String userInfo;
    private String novaLozinka;
}
