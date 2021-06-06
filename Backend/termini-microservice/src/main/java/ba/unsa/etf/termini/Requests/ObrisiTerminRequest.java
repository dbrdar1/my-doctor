package ba.unsa.etf.termini.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class ObrisiTerminRequest {
    Date datum;
    String vrijeme;
}
