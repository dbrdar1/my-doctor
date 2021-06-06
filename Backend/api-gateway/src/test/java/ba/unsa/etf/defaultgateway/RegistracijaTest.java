package ba.unsa.etf.defaultgateway;

import ba.unsa.etf.defaultgateway.requests.LoginRequest;
import ba.unsa.etf.defaultgateway.requests.RegistracijaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistracijaTest {

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registracijaTest() throws Exception {
        RegistracijaRequest registracijaRequest = new RegistracijaRequest(
                "Test",
                "Test",
                new Date(),
                "Test",
                "000000000",
                "pusina.samra@gmail.com",
                "test",
                "test",
                "DOKTOR");
        this.mockMvc.perform(post("/registracija")
                .content(asJsonString(registracijaRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.poruka", is("Uspješna registracija!")));
    }
}
