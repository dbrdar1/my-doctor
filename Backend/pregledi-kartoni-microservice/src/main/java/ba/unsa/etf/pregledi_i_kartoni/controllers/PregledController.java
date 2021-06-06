package ba.unsa.etf.pregledi_i_kartoni.controllers;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPacijentaRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPregledRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.PregledResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.services.PregledService;
import ba.unsa.etf.pregledi_i_kartoni.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import java.util.List;

@AllArgsConstructor
@RestController
public class PregledController {

    private final PregledService pregledService;


    @GetMapping("/inicijaliziraj-bazu")
    public ResponseEntity<Response> inicijalizacija() {

        String poruka = pregledService.incijalizirajBazu();
        return ResponseEntity.ok(new Response(poruka));

    }


    @GetMapping("/svi-pregledi")
    public ResponseEntity<List<PregledResponse>> getSviPregledi(){
        List<PregledResponse> trazeniPregledi = pregledService.dajSvePreglede();
        return ResponseEntity.ok(trazeniPregledi);
    }

    // Prikaz pregleda na osnovu id pregleda
    @GetMapping("/pregledi/{idPregleda}")
    public ResponseEntity<PregledResponse> dajPregled(@PathVariable(value = "idPregleda") Long idPregleda){
        PregledResponse trazeniPregled = pregledService.dajPregledNaOsonvuId(idPregleda);
        return ResponseEntity.ok(trazeniPregled);
    }

    // filtriranje pregleda (na osnovu id doktora, id pacijenta, id termina)
    @GetMapping("/pregledi-filtrirano")
    public ResponseEntity<List<PregledResponse>> filtrirajPreglede(@RequestParam(value = "idDoktor", required = false) Long idDoktor, @RequestParam(value = "idPacijent", required = false) Long idPacijent, @RequestParam(value = "idTermin", required = false) Long idTermin) {
        List<PregledResponse> trazeniPregledi = pregledService.filtrirajPreglede(idDoktor, idPacijent, idTermin);
        return ResponseEntity.ok(trazeniPregledi);
    }

    // pohrana pregleda
    @PostMapping("/dodaj-pregled")
    public ResponseEntity<Response> dodajPregled(@RequestHeader HttpHeaders headers, @RequestBody DodajPregledRequest dodajPregledRequest) {
        Response response = pregledService.dodajPregled(headers, dodajPregledRequest);
        return ResponseEntity.ok(response);
    }

    // brisanje pregleda
    @DeleteMapping("/obrisi-pregled/{idPregleda}")
    public ResponseEntity<Response> obrisiPregled(@RequestHeader HttpHeaders headers, @PathVariable(value = "idPregleda") Long idPregleda) {
        Response response = pregledService.obrisiPregled(headers, idPregleda);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleNoSuchElementFoundException(ConstraintViolationException exception) {
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

