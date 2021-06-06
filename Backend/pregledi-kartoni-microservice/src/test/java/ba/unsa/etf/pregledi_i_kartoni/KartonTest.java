package ba.unsa.etf.pregledi_i_kartoni;

import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.repositories.*;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPacijentaRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPregledRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;


import static org.springframework.web.servlet.function.RequestPredicates.contentType;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
public class KartonTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TerminRepository terminRepository;
    @Autowired
    DoktorRepository doktorRepository;
    @Autowired
    PacijentRepository pacijentRepository;
    @Autowired
    PacijentDoktorRepository pacijentDoktorRepository;
    @Autowired
    PregledRepository pregledRepository;
    @Autowired
    KorisnikRepository korisnikRepository;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //@BeforeEach
    public void inicijalizirajBazu() {

/*        korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();

        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();

        pacijentRepository.deleteAllInBatch();
        pacijentRepository.flush();

        pacijentDoktorRepository.deleteAllInBatch();
        pacijentDoktorRepository.flush();

        terminRepository.deleteAllInBatch();
        terminRepository.flush();

        pregledRepository.deleteAllInBatch();
        pregledRepository.flush();

        Doktor doktor1 = new Doktor("ImeDoktora1", "PrezimeDoktora1", new Date(), "AdresaDoktora1", "061-123-123", "nekimaildoktora1@gmail.com");
        Pacijent pacijent1 = new Pacijent( "ImePacijenta1", "PrezimePacijenta1", new Date(), "Adresa1", "061-456-456", "nekimailpacijenta1@gmail.com",
                "zenski", 175.2, 68.3, "0+", "/", "/");

        Doktor doktor2 = new Doktor("ImeDoktora2", "PrezimeDoktora2", new Date(), "AdresaDoktora2", "061-723-723", "nekimaildoktora2@gmail.com");
        Pacijent pacijent2 = new Pacijent( "ImePacijenta2", "PrezimePacijenta2", new Date(), "Adresa2", "061-323-323", "nekimailpacijenta2@gmail.com",
                "zenski", 165.4, 56.3, "A-", "/", "/");


        doktorRepository.save(doktor1);
        doktorRepository.save(doktor2);
        pacijentRepository.save(pacijent1);
        pacijentRepository.save(pacijent2);

        PacijentDoktor pd1 = new PacijentDoktor();
        pd1.setDoktor(doktor1);
        pd1.setPacijent(pacijent1);
        PacijentDoktor pd2 = new PacijentDoktor();
        pd2.setDoktor(doktor2);
        pd2.setPacijent(pacijent2);

        pacijentDoktorRepository.save(pd1);
        pacijentDoktorRepository.save(pd2);


        Termin termin1 = new Termin(new Date(), "15:20", pd1);
        Termin termin2 = new Termin(new Date(), "16:30", pd2);

        terminRepository.save(termin1);
        terminRepository.save(termin2);



        Pregled pregled1 = new Pregled("neki simptomi1", "neki fiz pregled1", "neka dijagnoza1",
                "neki tretman1", "neki komentar1", termin1);

        Pregled pregled2 = new Pregled("neki simptomi2", "neki fiz pregled2", "neka dijagnoza2",
                "neki tretman2", "neki komentar2", termin2);


        pregledRepository.save(pregled1);
        pregledRepository.save(pregled2);*/

    }

    @BeforeEach
    public void ocistiBazu() {
        korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();

        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();

        pacijentRepository.deleteAllInBatch();
        pacijentRepository.flush();

        pacijentDoktorRepository.deleteAllInBatch();
        pacijentDoktorRepository.flush();

        terminRepository.deleteAllInBatch();
        terminRepository.flush();

        pregledRepository.deleteAllInBatch();
        pregledRepository.flush();
    }


    @Test
    public void postIspravnogKartona() throws Exception {


        this.mockMvc.perform(post("/dodaj-pacijenta")
                .content(asJsonString(new DodajPacijentaRequest("test1", "test", new Date(), "adresa", "033211211", "neko1@gmail.com",
                        null, 0, 0, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void postNeispravnihFormata() throws Exception {


        this.mockMvc.perform(post("/dodaj-pacijenta")
                .content(asJsonString(new DodajPacijentaRequest("", "test", new Date(), "adresa", "033211211", "neko1@gmail.com",
                        null, 1.75, 80, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusniKod", is(400)));
        this.mockMvc.perform(post("/dodaj-pregled")
                .content(asJsonString(new DodajPacijentaRequest("test1", "test", new Date(), "adresa", "211211", "neko1@gmail.com",
                        null, 1.75, 80, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusniKod", is(400)));
        this.mockMvc.perform(post("/dodaj-pacijenta")
                .content(asJsonString(new DodajPacijentaRequest("test1", "test", new Date(), "adresa", "033211211", "neko1gmail.com",
                        null, 1.75, 80, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusniKod", is(400)));
        this.mockMvc.perform(post("/dodaj-pacijenta")
                .content(asJsonString(new DodajPacijentaRequest("test1", "test", new Date(), "adresa", "033211211", "neko1@gmail.com",
                        null, 1.75, 80, "C+", null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusniKod", is(400)));

    }

    @Test
    public void getKartona() throws Exception {


        this.mockMvc.perform(post("/dodaj-pacijenta")
                .content(asJsonString(new DodajPacijentaRequest("Sabit", "Iztarcina", new Date(), "adresa", "033211211", "neko1@gmail.com",
                        null, 1.75, 80, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/dodaj-pacijenta")
                .content(asJsonString(new DodajPacijentaRequest("Ragib", "Nagib", new Date(), "adresa", "033211211", "neko2@gmail.com",
                        null, 1.75, 80, null, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/karton?ime=Ragib"))
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ime", is("Ragib")));


    }


}