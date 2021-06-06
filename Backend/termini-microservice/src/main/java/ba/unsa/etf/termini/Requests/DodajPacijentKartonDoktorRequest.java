package ba.unsa.etf.termini.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajPacijentKartonDoktorRequest {
    Long pacijentId;
    Long doktorId;
}
