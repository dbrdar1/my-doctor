package ba.unsa.etf.termini;

import ba.unsa.etf.termini.Requests.DodajTerminRequest;
import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.models.PacijentKartonDoktor;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentKartonDoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import ba.unsa.etf.termini.repositories.TerminRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TerminTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TerminRepository terminRepository;
    @Autowired
    DoktorRepository doktorRepository;
    @Autowired
    PacijentRepository pacijentRepository;
    @Autowired
    PacijentKartonDoktorRepository pacijentKartonDoktorRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

  /*  @Test
    public void postIspravnogTermina() throws Exception {
        Pacijent p = new Pacijent("test1","test",new Date(), "adresa","033211211","neko1@gmail.com");
        Doktor d = new Doktor("test2","test",new Date(), "adresa","033211211","neko2@gmail.com","doktor");
        pacijentRepository.save(p);
        doktorRepository.save(d);
        p=pacijentRepository.findByIme("test1");
        d=doktorRepository.findByIme("test2");
        PacijentKartonDoktor pkd = new PacijentKartonDoktor();
        pkd.setDoktor(d);
        pkd.setPacijent(p);
        pacijentKartonDoktorRepository.save(pkd);
        pkd = pacijentKartonDoktorRepository.findByPacijent(p);
        this.mockMvc.perform(post("/dodaj-termin")
                .content(asJsonString(new DodajTerminRequest(pkd.getId(), new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        pacijentRepository.delete(p);
        doktorRepository.delete(d);
    }

    @Test
    public void postNeispravnihFormata() throws Exception {
        Pacijent p = new Pacijent("test1","test",new Date(), "adresa","033211211","neko1@gmail.com");
        Doktor d = new Doktor("test2","test",new Date(), "adresa","033211211","neko2@gmail.com","doktor");
        pacijentRepository.save(p);
        doktorRepository.save(d);
        p=pacijentRepository.findByIme("test1");
        d=doktorRepository.findByIme("test2");
        PacijentKartonDoktor pkd = new PacijentKartonDoktor();
        pkd.setDoktor(d);
        pkd.setPacijent(p);
        pacijentKartonDoktorRepository.save(pkd);
        pkd = pacijentKartonDoktorRepository.findByPacijent(p);
        this.mockMvc.perform(post("/dodaj-termin")
                .content(asJsonString(new DodajTerminRequest(pkd.getId(), new Date(), "")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(500)));
        this.mockMvc.perform(post("/dodaj-termin")
                .content(asJsonString(new DodajTerminRequest(pkd.getId(), null, "12:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(500)));
        this.mockMvc.perform(post("/dodaj-termin")
                .content(asJsonString(new DodajTerminRequest(pkd.getId(), new Date(), "52:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(500)));
        pacijentRepository.delete(p);
        doktorRepository.delete(d);
    }

    @Test
    public void getTermina() throws Exception {
        Pacijent p = new Pacijent("test1","test",new Date(), "adresa","033211211","neko1@gmail.com");
        Doktor d = new Doktor("test2","test",new Date(), "adresa","033211211","neko2@gmail.com","doktor");
        pacijentRepository.save(p);
        doktorRepository.save(d);
        p=pacijentRepository.findByIme("test1");
        d=doktorRepository.findByIme("test2");
        PacijentKartonDoktor pkd = new PacijentKartonDoktor();
        pkd.setDoktor(d);
        pkd.setPacijent(p);
        pacijentKartonDoktorRepository.save(pkd);
        pkd = pacijentKartonDoktorRepository.findByPacijent(p);
        this.mockMvc.perform(post("/dodaj-termin")
                .content(asJsonString(new DodajTerminRequest(pkd.getId(), new Date(), "11:00")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/termini-pacijenta/"+p.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.termini", hasSize(1)));
        this.mockMvc.perform(get("/termini-doktora/"+d.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.termini", hasSize(1)));
        pacijentRepository.delete(p);
        doktorRepository.delete(d);
    }*/

//    @Test
//    public void deleteNakonPosta() throws Exception {
//        Pacijent p1 = new Pacijent("test1","test",new Date(), "adresa","033211211","neko1@gmail.com");
//        Doktor d1 = new Doktor("test2","test",new Date(), "adresa","033211211","neko2@gmail.com","doktor");
//        pacijentRepository.save(p1);
//        doktorRepository.save(d1);
//        Pacijent p =pacijentRepository.findByIme("test1");
//        Doktor d =doktorRepository.findByIme("test2");
//        PacijentKartonDoktor pkd = new PacijentKartonDoktor();
//        pkd.setDoktor(d);
//        pkd.setPacijent(p);
//        pacijentKartonDoktorRepository.save(pkd);
//        pkd = pacijentKartonDoktorRepository.findByPacijent(p);
//        this.mockMvc.perform(post("/dodaj-termin")
//                .content(asJsonString(new DodajTerminRequest(pkd.getId(), new Date(), "05:05")))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//        this.mockMvc.perform(delete("/obrisi-termin")
//                .content(asJsonString(new ObrisiTerminRequest(new Date(), "05:05")))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//        pacijentRepository.delete(p);
//        doktorRepository.delete(d);
//    }
}
