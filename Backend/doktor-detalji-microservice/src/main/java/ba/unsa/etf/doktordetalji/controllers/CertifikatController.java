package ba.unsa.etf.doktordetalji.controllers;

import ba.unsa.etf.doktordetalji.exceptions.ResourceNotFoundException;
import ba.unsa.etf.doktordetalji.exceptions.UnauthorizedException;
import ba.unsa.etf.doktordetalji.requests.DodajCertifikatRequest;
import ba.unsa.etf.doktordetalji.requests.UrediCertifikatRequest;
import ba.unsa.etf.doktordetalji.responses.Response;
import ba.unsa.etf.doktordetalji.security.TrenutniKorisnikSecurity;
import ba.unsa.etf.doktordetalji.services.CertifikatService;
import ba.unsa.etf.doktordetalji.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@AllArgsConstructor
@RestController
public class CertifikatController {

    private final CertifikatService certifikatService;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    @PostMapping("/dodaj-certifikat")
    public ResponseEntity<Response> dodajCertifikat(@RequestHeader HttpHeaders headers, @RequestBody DodajCertifikatRequest dodajCertifikatRequest) {

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, dodajCertifikatRequest.getIdDoktora()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        Response response = certifikatService.dodajCertifikat(dodajCertifikatRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/uredi-certifikat")
    public ResponseEntity<Response> urediCertifikat(@RequestHeader HttpHeaders headers, @RequestBody UrediCertifikatRequest urediCertifikatRequest) {

        Response response = certifikatService.urediCertifikat(headers, urediCertifikatRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/obrisi-certifikat/{id}")
    public ResponseEntity<Response> obrisiCertifikat(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        Response response = certifikatService.obrisiCertifikat(headers, id);
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
