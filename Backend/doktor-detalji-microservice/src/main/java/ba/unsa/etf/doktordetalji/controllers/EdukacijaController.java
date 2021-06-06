package ba.unsa.etf.doktordetalji.controllers;

import ba.unsa.etf.doktordetalji.exceptions.ResourceNotFoundException;
import ba.unsa.etf.doktordetalji.exceptions.UnauthorizedException;
import ba.unsa.etf.doktordetalji.requests.DodajEdukacijuRequest;
import ba.unsa.etf.doktordetalji.requests.UrediEdukacijuRequest;
import ba.unsa.etf.doktordetalji.responses.Response;
import ba.unsa.etf.doktordetalji.security.TrenutniKorisnikSecurity;
import ba.unsa.etf.doktordetalji.services.EdukacijaService;
import ba.unsa.etf.doktordetalji.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@AllArgsConstructor
@RestController
public class EdukacijaController {

    private final EdukacijaService edukacijaService;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    @PostMapping("/dodaj-edukaciju")
    public ResponseEntity<Response> dodajEdukaciju(@RequestHeader HttpHeaders headers, @RequestBody DodajEdukacijuRequest dodajEdukacijuRequest) {

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, dodajEdukacijuRequest.getIdDoktora()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        Response response = edukacijaService.dodajEdukaciju(dodajEdukacijuRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/uredi-edukaciju")
    public ResponseEntity<Response> urediEdukaciju(@RequestHeader HttpHeaders headers, @RequestBody UrediEdukacijuRequest urediEdukacijuRequest) {
        Response response = edukacijaService.urediEdukaciju(headers, urediEdukacijuRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/obrisi-edukaciju/{id}")
    public ResponseEntity<Response> obrisiEdukaciju(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        Response response = edukacijaService.obrisiEdukaciju(headers, id);
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
