package ba.unsa.etf.pregledi_i_kartoni.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncTerminiRequest {
    private Long id;
    private Long idDoktora;
    private Long idPacijenta;
    private String datum;
    private String vrijeme;
}
