package ba.unsa.etf.termini.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncTerminiResponse {
    private Long id;
    private Long idDoktora;
    private Long idPacijenta;
    private String datum;
    private String vrijeme;
}
