package ba.unsa.etf.termini.controllers;

import ba.unsa.etf.termini.Requests.DodajTerminRequest;
import ba.unsa.etf.termini.Requests.UrediTerminRequest;
import ba.unsa.etf.termini.Responses.ListaTerminaResponse;
import ba.unsa.etf.termini.Responses.Response;
import ba.unsa.etf.termini.Responses.TerminResponse;
import ba.unsa.etf.termini.exceptions.ResourceNotFoundException;
import ba.unsa.etf.termini.exceptions.UnauthorizedException;
import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.models.PacijentKartonDoktor;
import ba.unsa.etf.termini.models.Termin;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentKartonDoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import ba.unsa.etf.termini.repositories.TerminRepository;
import ba.unsa.etf.termini.security.TrenutniKorisnikSecurity;
import ba.unsa.etf.termini.services.TerminService;
import ba.unsa.etf.termini.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;


@AllArgsConstructor
@RestController
public class TerminController {
    private TerminService terminService;
    private PacijentRepository pacijentRepository;
    private DoktorRepository doktorRepository;
    private PacijentKartonDoktorRepository pacijentKartonDoktorRepository;
    private TerminRepository terminRepository;
    private  final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    @PostMapping("/dodaj-termin")
    public ResponseEntity<Response> dodajTermin(@RequestBody DodajTerminRequest dodajTerminRequest){
        Response response = terminService.dodajTermin(dodajTerminRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/obrisi-termin/{id}")
    public ResponseEntity<Response> obrisiTermin(@PathVariable Long id) {
        Response response = terminService.obrisiTermin(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/dobavi-termin/{id}")
    public ResponseEntity<TerminResponse> dajTermin(@PathVariable Long id){
        TerminResponse response = terminService.dajTermin(id);
        return ResponseEntity.ok(response);
    }

//    @DeleteMapping("/obrisi-termin")
//    public ResponseEntity<Response> obrisiTermin(@RequestBody ObrisiTerminRequest obrisiTerminRequest) {
//        Response response = terminService.obrisiTerminPoAtributima(obrisiTerminRequest);
//        return ResponseEntity.ok(response);
//    }

    @PutMapping("/uredi-termin/{id}")
    public  ResponseEntity<Response> urediTermin(@RequestBody UrediTerminRequest urediTerminRequest, @PathVariable Long id){
        Response response=terminService.urediTermin(id, urediTerminRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/termini-pacijenta/{id}")
    public  ResponseEntity<ListaTerminaResponse> dajTerminePacijenta(@RequestHeader HttpHeaders headers, @PathVariable Long id){

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, id))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        ListaTerminaResponse response=terminService.dajTerminePacijenta(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/termini-doktora/{id}")
    public  ResponseEntity<ListaTerminaResponse> dajTermineDoktora(@PathVariable Long id){
        ListaTerminaResponse response=terminService.dajTermineDoktora(id);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleConstraintViolationException(ConstraintViolationException exception) {
        return ErrorHandlingHelper.handleConstraintViolationException(exception);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response handleEntityNotFoundException(ResourceNotFoundException exception) {
        return ErrorHandlingHelper.handleEntityNotFoundException(exception);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleEntityUnauthorizedxception(UnauthorizedException exception) {
        return ErrorHandlingHelper.handleEntityUnauthorizedException(exception);
    }

}
