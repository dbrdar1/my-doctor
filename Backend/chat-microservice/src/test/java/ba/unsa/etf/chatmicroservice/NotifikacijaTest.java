package ba.unsa.etf.chatmicroservice;

import ba.unsa.etf.chatmicroservice.model.*;
import ba.unsa.etf.chatmicroservice.dto.NotifikacijaProjection;
import ba.unsa.etf.chatmicroservice.repository.*;
import ba.unsa.etf.chatmicroservice.request.DodajNotifikacijuRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotifikacijaTest {

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
    public void getNotifikacijeTest() throws Exception {
        inicijalizirajBazu();
        this.mockMvc.perform(get("/notifikacije")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].naslov", is("naziv notifikacije")))
                .andExpect(jsonPath("$[0].tekst", is("imate novu poruku")))
                .andExpect(jsonPath("$[0].vrijeme", is("14:00")));
    }

    @Test
    public void getNotifikacijaByIdTest() throws Exception {
        inicijalizirajBazu();
        NotifikacijaProjection notifikacija = notifikacijaRepository.findByNaslov("naziv notifikacije");
        this.mockMvc.perform(get("/notifikacije/" + notifikacija.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.naslov", is("naziv notifikacije")))
                .andExpect(jsonPath("$.tekst", is("imate novu poruku")))
                .andExpect(jsonPath("$.vrijeme", is("14:00")));
    }

    @Test
    public void postIspravnaNotifikacijaTest() throws Exception {
        inicijalizirajBazu();
        Korisnik korisnik = new Korisnik("test","testovic", new Date(), "Bulevar Testova 64","ttestovic1@gmail.com","061123456");
        korisnik = korisnikRepository.save(korisnik);
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(korisnik.getId(), "Test naslov", "Test notifikacija!", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        korisnikRepository.delete(korisnik);
    }

    @Test
    public void postNotifikacijaNepostojeciKorisnikTest() throws Exception {
        inicijalizirajBazu();
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(10L,"Test naslov", "Test notifikacija!", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Ne postoji korisnik s ovim id-om!")))
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    public void postNotifikacijeBezNaslova() throws Exception {
        inicijalizirajBazu();
        Korisnik korisnik = new Korisnik("test","testovic", new Date(), "Bulevar Testova 64","ttestovic1@gmail.com","061123456");
        korisnik = korisnikRepository.save(korisnik);
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(korisnik.getId(),"", "Test notifikacija!", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(400)));
    }
}
