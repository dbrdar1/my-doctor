package ba.unsa.etf.termini.controllers;

import ba.unsa.etf.termini.Responses.DoktorResponse;
import ba.unsa.etf.termini.Responses.PacijentResponse;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.repositories.KorisnikRepository;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import ba.unsa.etf.termini.services.PacijentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
public class PacijentController {
    private final PacijentService pacijentService;
    private KorisnikRepository korisnikRepository;
    private PacijentRepository pacijentRepository;

    @GetMapping("/pacijenti/{id}")
    public ResponseEntity<PacijentResponse> dajPacijenta(@PathVariable Long id){
        PacijentResponse response = pacijentService.dajPacijenta(id);
        return ResponseEntity.ok(response);
    }
}
