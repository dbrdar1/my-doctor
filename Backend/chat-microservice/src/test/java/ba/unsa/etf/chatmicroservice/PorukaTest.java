package ba.unsa.etf.chatmicroservice;

import ba.unsa.etf.chatmicroservice.model.Doktor;
import ba.unsa.etf.chatmicroservice.model.Notifikacija;
import ba.unsa.etf.chatmicroservice.model.Pacijent;
import ba.unsa.etf.chatmicroservice.model.Poruka;
import ba.unsa.etf.chatmicroservice.dto.PorukaProjection;
import ba.unsa.etf.chatmicroservice.repository.*;
import ba.unsa.etf.chatmicroservice.request.DodajPorukuRequest;
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
public class PorukaTest {

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

    String timestampAkcijeNow =
            Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Sarajevo")).toInstant()).toString();

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

        Poruka poruka = new Poruka(
                "dje si",
                timestampAkcijeNow,
                doktor,
                pacijent);

        doktorRepository.save(doktor);
        pacijentRepository.save(pacijent);
        notifikacijaRepository.save(notifikacija);
        porukaRepository.save(poruka);
    }

    @Test
    public void getPorukeTest() throws Exception {
        inicijalizirajBazu();
        this.mockMvc.perform(get("/poruke")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0].sadrzaj", is("dje si")))
                .andExpect(jsonPath("$[0].vrijeme", is("13:00")));
    }

    @Test
    public void getPorukaByIdTest() throws Exception {
        inicijalizirajBazu();
        PorukaProjection poruka = porukaRepository.findBySadrzaj("dje si");
        this.mockMvc.perform(get("/poruke/" + poruka.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.sadrzaj", is("dje si")))
                .andExpect(jsonPath("$.vrijeme", is("13:00")));
    }

    @Test
    public void postIspravnaPorukaTest() throws Exception {
        inicijalizirajBazu();
        Doktor posiljalac = new Doktor("test1","testovic1", new Date(), "Bulevar Testova 64","ttestovic1@gmail.com","061123456");
        Pacijent primalac = new Pacijent("test2","testovic2", new Date(), "Bulevar Testova 66","ttestovic2@gmail.com","061123457");
        posiljalac = korisnikRepository.save(posiljalac);
        primalac = korisnikRepository.save(primalac);
        this.mockMvc.perform(post("/dodaj-poruku")
                .content(asJsonString(new DodajPorukuRequest
                        ("Neka poruka test.", timestampAkcijeNow, posiljalac.getId(), primalac.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        korisnikRepository.delete(posiljalac);
        korisnikRepository.delete(primalac);
    }

    @Test
    public void postPorukaNepostojeciKorisnikTest() throws Exception {
        inicijalizirajBazu();
        this.mockMvc.perform(post("/dodaj-poruku")
                .content(asJsonString(new DodajPorukuRequest
                        ("Neka poruka test.", timestampAkcijeNow, 10L, 11L)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Ne postoji posiljalac s ovim id-om!")))
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    public void postPorukaBezSadrzajaTest() throws Exception {
        inicijalizirajBazu();
        Doktor posiljalac = new Doktor("test1","testovic1", new Date(), "Bulevar Testova 64","ttestovic1@gmail.com","061123456");
        Pacijent primalac = new Pacijent("test2","testovic2", new Date(), "Bulevar Testova 66","ttestovic2@gmail.com","061123457");
        posiljalac = korisnikRepository.save(posiljalac);
        primalac = korisnikRepository.save(primalac);
        this.mockMvc.perform(post("/dodaj-poruku")
                .content(asJsonString(new DodajPorukuRequest
                        ("", timestampAkcijeNow, posiljalac.getId(), primalac.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(400)));
    }
}
