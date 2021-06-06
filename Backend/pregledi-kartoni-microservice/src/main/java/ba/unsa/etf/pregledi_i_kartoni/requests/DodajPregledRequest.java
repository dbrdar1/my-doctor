package ba.unsa.etf.pregledi_i_kartoni.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajPregledRequest {

    private Long terminId;
    private String simptomi;
    private String fizikalniPregled;
    private String dijagnoza;
    private String tretman;
    private String komentar;

}
