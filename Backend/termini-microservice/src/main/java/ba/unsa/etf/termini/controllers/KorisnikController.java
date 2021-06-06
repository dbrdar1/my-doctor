package ba.unsa.etf.termini.controllers;

import ba.unsa.etf.termini.Requests.AsyncRequest;
import ba.unsa.etf.termini.Requests.DodajKorisnikaRequest;
import ba.unsa.etf.termini.Responses.Response;
import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.repositories.KorisnikRepository;
import ba.unsa.etf.termini.services.KorisnikService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
public class KorisnikController {

    private KorisnikRepository korisnikRepository;
    private final KorisnikService korisnikService;

    @Autowired
    private RestTemplate restTemplate;

    private final List<Korisnik> korisnici;

    @GetMapping("/korisnici")
    List<Korisnik> all() {
        return korisnikRepository.findAll();
    }

    @GetMapping("/korisnici/{id}")
    Korisnik one(@PathVariable Long id) throws Exception {

        return korisnikRepository.findById(id)
                .orElseThrow(() -> new Exception("Korisnik nije pronađen"));
    }

    @GetMapping("/dobavi-korisnike")
    public ResponseEntity<String> postKorisnici(){

        String fooResourceUrl = "http://user-management/getKorisnici";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        return response;
    }

    @PostMapping("/async")
    public ResponseEntity<Response> asyncKorisnici(@RequestBody AsyncRequest asyncRequest) throws ParseException {
        String poruka = korisnikService.asyncKorisnici(asyncRequest);
        return ResponseEntity.ok(new Response(poruka));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleNoSuchElementFoundException(
            ConstraintViolationException exception
    ) {
        return new Response(exception.getConstraintViolations().toString(),500);
    }
}

