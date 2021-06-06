package ba.unsa.etf.doktordetalji.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcjenaRequest {
    private Long id;
    private Integer ocjena;
}
