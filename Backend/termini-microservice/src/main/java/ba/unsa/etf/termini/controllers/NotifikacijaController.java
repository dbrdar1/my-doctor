package ba.unsa.etf.termini.controllers;

import ba.unsa.etf.termini.Requests.DodajNotifikacijuRequest;
import ba.unsa.etf.termini.Responses.NotifikacijaResponse;
import ba.unsa.etf.termini.Responses.NotifikacijeKorisnikaResponse;
import ba.unsa.etf.termini.Responses.Response;
import ba.unsa.etf.termini.exceptions.ResourceNotFoundException;
import ba.unsa.etf.termini.exceptions.UnauthorizedException;
import ba.unsa.etf.termini.models.Notifikacija;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import ba.unsa.etf.termini.services.NotifikacijaService;

import ba.unsa.etf.termini.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@RestController
public class NotifikacijaController {
    private final NotifikacijaService notifikacijaService;

    @PostMapping("/radi")
    public ResponseEntity<Response> dodajNotifikaciju(){
        System.out.println("/dodaj");

        return ResponseEntity.ok(new Response("dodanooo",200));
    }
    @PostMapping("/dodaj-notifikaciju")
    public ResponseEntity<Response> dodajNotifikaciju(@RequestBody DodajNotifikacijuRequest dodajNotifikacijuRequest){
        System.out.println("/dodaj notifikaciju");
        System.out.println(dodajNotifikacijuRequest.toString());
        Response response = null;
        try {
            response = notifikacijaService.dodajNotifikaciju(dodajNotifikacijuRequest);
        }catch (Exception e){
            throw e;
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifikacije-korisnika/{idKorisnika}")
    public ResponseEntity<NotifikacijeKorisnikaResponse> dajNotifikacijeKorisnika(@PathVariable Long idKorisnika){
        NotifikacijeKorisnikaResponse response = notifikacijaService.dajNotifikacijeKorisnika(idKorisnika);
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
