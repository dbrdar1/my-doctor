package ba.unsa.etf.defaultgateway.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Long idKorisnika;
    private String uloga;
    private final String vrstaTokena = "Bearer";
    private String token;
}
