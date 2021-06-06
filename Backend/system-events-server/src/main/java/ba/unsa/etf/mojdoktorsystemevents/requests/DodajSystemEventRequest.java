package ba.unsa.etf.mojdoktorsystemevents.requests;

import ba.unsa.etf.grpc.ActionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DodajSystemEventRequest {

    private String timestampAkcije;
    private String nazivMikroservisa;
    private String resurs;
    private ActionRequest.TipAkcije tipAkcije;
    private ActionRequest.TipOdgovoraNaAkciju tipOdgovoraNaAkciju;
}
