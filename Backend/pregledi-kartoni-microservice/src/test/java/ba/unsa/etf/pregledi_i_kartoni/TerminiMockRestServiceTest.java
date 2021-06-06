package ba.unsa.etf.pregledi_i_kartoni;

import ba.unsa.etf.pregledi_i_kartoni.models.Termin;
import ba.unsa.etf.pregledi_i_kartoni.responses.ListaTerminaResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.TerminResponse;
import ba.unsa.etf.pregledi_i_kartoni.services.TerminService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class TerminiMockRestServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TerminService terminService;

    @Test
    public void getTerminePacijentaTest() {

        String termini = "{\"termini\":[{\"id\":3,\"datum\":\"3921-06-24T22:00:00.000+00:00\",\"vrijeme\":\"10:00\"},{\"id\":8,\"datum\":\"3921-06-24T22:00:00.000+00:00\",\"vrijeme\":\"10:00\"}]}";

        //List<Termin> termini = new ArrayList<Termin>();


        Mockito
                .when(restTemplate.getForEntity(
                        "http://termini/termini-pacijenta/11", String.class))
          .thenReturn(new ResponseEntity(termini, HttpStatus.OK));

        String dobiveniTermini = terminService.dajTermin(11L);

        Assert.assertEquals(termini, dobiveniTermini);
    }
}