package ba.unsa.etf.mojdoktorsystemevents.models;

import ba.unsa.etf.grpc.ActionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "system_event")
public class SystemEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String timestampAkcije;

    private String nazivMikroservisa;

    private String resurs;

    private ActionRequest.TipAkcije tipAkcije;

    private ActionRequest.TipOdgovoraNaAkciju tipOdgovoraNaAkciju;

    public SystemEvent(String timestampAkcije, String nazivMikroservisa, String resurs,
                       ActionRequest.TipAkcije tipAkcije, ActionRequest.TipOdgovoraNaAkciju tipOdgovoraNaAkciju) {
        this.timestampAkcije = timestampAkcije;
        this.nazivMikroservisa = nazivMikroservisa;
        this.resurs = resurs;
        this.tipAkcije = tipAkcije;
        this.tipOdgovoraNaAkciju = tipOdgovoraNaAkciju;
    }
}

