package ba.unsa.etf.pregledi_i_kartoni.responses;

import lombok.Data;

@Data
public class Response {
    private String poruka;
    private Integer statusniKod;

    public Response(String poruka) {
        this(poruka, 200);
    }

    public Response(String poruka, Integer statusniKod) {
        this.poruka = poruka;
        this.statusniKod = statusniKod;
    }
}