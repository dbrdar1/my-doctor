package ba.unsa.etf.pregledi_i_kartoni.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajTerminRequest {

    private Long pacijentDoktorId;
    private Date datumPregleda;
    private String vrijemePregleda;

}
