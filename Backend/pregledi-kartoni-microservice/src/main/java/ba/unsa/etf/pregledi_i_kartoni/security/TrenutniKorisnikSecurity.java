package ba.unsa.etf.pregledi_i_kartoni.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TrenutniKorisnikSecurity {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public Long getIdTrenutnogKorisnika(HttpHeaders headers){
        String t = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = t.replaceAll("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public Boolean isTrenutniKorisnik(HttpHeaders headers, Long idKorisnika){
        String t = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = t.replaceAll("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        Long id = Long.parseLong(claims.getSubject());
        return id.equals(idKorisnika);
    }
}