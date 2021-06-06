package ba.unsa.etf.termini;

import ba.unsa.etf.termini.Requests.DodajNotifikacijuRequest;
import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.models.Notifikacija;
import ba.unsa.etf.termini.repositories.KorisnikRepository;
import ba.unsa.etf.termini.repositories.NotifikacijaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    NotifikacijaRepository notifikacijaRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*
    @Test
    public void postIspravneNotifikacije() throws Exception {
        Korisnik k = new Korisnik("test","test",new Date(), "adresa","033211211","neko@gmail.com");
        korisnikRepository.save(k);
        k = korisnikRepository.findByIme("test");
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(k.getId(),"Test", "test notifikacija", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        korisnikRepository.delete(k);
    }

    @Test
    public void postNotifikacijeNepostojeciKorisnik() throws Exception {
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(0L,"Test", "test notifikacija", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message",is("Id korisnika nije postojeći!")))
                .andExpect(jsonPath("$.statusCode",is(400)));
    }

    @Test
    public void postNotifikacijeBezNaslova() throws Exception {
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(60L,"", "test notifikacija", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(500)));
    }

    @Test
    public void deleteAfterPost() throws Exception{
        Korisnik k = new Korisnik("test","test",new Date(), "adresa","033211211","neko@gmail.com");
        korisnikRepository.save(k);
        k = korisnikRepository.findByIme("test");
        this.mockMvc.perform(post("/dodaj-notifikaciju")
                .content(asJsonString(new DodajNotifikacijuRequest(k.getId(),"Test", "test notifikacija", new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Notifikacija n = notifikacijaRepository.findByNaslov("Test");
        this.mockMvc.perform(delete("/notifikacije/"+n.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        korisnikRepository.delete(k);
    }*/
}
