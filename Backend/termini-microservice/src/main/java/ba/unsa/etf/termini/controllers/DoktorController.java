package ba.unsa.etf.termini.controllers;

import ba.unsa.etf.termini.Responses.DoktorResponse;
import ba.unsa.etf.termini.models.PacijentKartonDoktor;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import ba.unsa.etf.termini.repositories.KorisnikRepository;
import ba.unsa.etf.termini.services.DoktorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@AllArgsConstructor
@RestController
public class DoktorController {
    private final DoktorService doktorService;
    private KorisnikRepository korisnikRepository;
    private DoktorRepository doktorRepository;

    @OneToMany(targetEntity = PacijentKartonDoktor.class,
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            mappedBy="doktor")
    private List<PacijentKartonDoktor> vezeSaPacijentima;


    @GetMapping("/doktori/{id}")
    public ResponseEntity<DoktorResponse> dajDoktora(@PathVariable Long id){
        DoktorResponse response = doktorService.dajDoktora(id);
        return ResponseEntity.ok(response);
    }
}
