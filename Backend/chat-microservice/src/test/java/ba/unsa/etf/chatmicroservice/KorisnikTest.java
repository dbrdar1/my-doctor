package ba.unsa.etf.chatmicroservice;

import ba.unsa.etf.chatmicroservice.model.*;
import ba.unsa.etf.chatmicroservice.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class KorisnikTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    DoktorRepository doktorRepository;

    @Autowired
    PacijentRepository pacijentRepository;

    @Autowired
    NotifikacijaRepository notifikacijaRepository;

    @Autowired
    PorukaRepository porukaRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void inicijalizirajBazu() {

        korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();

        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();

        pacijentRepository.deleteAllInBatch();
        pacijentRepository.flush();

        notifikacijaRepository.deleteAllInBatch();
        notifikacijaRepository.flush();

        porukaRepository.deleteAllInBatch();
        porukaRepository.flush();

        Date date = new Date();

        Doktor doktor = new Doktor(
                "Samra",
                "Pusina",
                date,
                "NekaAdresa1",
                "spusina1@etf.unsa.ba",
                "061111222");

        Pacijent pacijent = new Pacijent(
                "Esmina",
                "Radusic",
                date,
                "NekaAdresa2",
                "eradusic1@etf.unsa.ba",
                "061111222");

        Notifikacija notifikacija = new Notifikacija(
                "naziv notifikacije",
                "imate novu poruku",
                date,
                "14:00",
                pacijent);

        String timestampAkcijeNow =
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Sarajevo")).toInstant()).toString();

        Poruka poruka = new Poruka(
                "dje si",
                timestampAkcijeNow,
                doktor,
                pacijent
        );

        doktorRepository.save(doktor);
        pacijentRepository.save(pacijent);
        notifikacijaRepository.save(notifikacija);
        porukaRepository.save(poruka);
    }

    @Test
    public void getKorisniciTest() throws Exception {
        inicijalizirajBazu();
        this.mockMvc.perform(get("/korisnici")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].ime", is("Samra")))
                .andExpect(jsonPath("$[0].prezime", is("Pusina")))
                .andExpect(jsonPath("$[0].email", is("spusina1@etf.unsa.ba")))
                .andExpect(jsonPath("$[1].ime", is("Esmina")))
                .andExpect(jsonPath("$[1].prezime", is("Radusic")))
                .andExpect(jsonPath("$[1].email", is("eradusic1@etf.unsa.ba")));
    }

    @Test
    public void getKorisnikByIdTest() throws Exception {
        inicijalizirajBazu();
        Korisnik korisnik = korisnikRepository.findByIme("Esmina");
        this.mockMvc.perform(get("/korisnici/" + korisnik.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.ime", is("Esmina")))
                .andExpect(jsonPath("$.prezime", is("Radusic")))
                .andExpect(jsonPath("$.email", is("eradusic1@etf.unsa.ba")));
    }
}
