package ba.unsa.etf.pregledi_i_kartoni.controllers;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.requests.AsyncObrisiTerminRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.AsyncTerminiRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajTerminRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.ListaTerminaResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.responses.TerminResponse;
import ba.unsa.etf.pregledi_i_kartoni.services.DoktorService;
import ba.unsa.etf.pregledi_i_kartoni.services.PacijentDoktorService;
import ba.unsa.etf.pregledi_i_kartoni.services.PacijentService;
import ba.unsa.etf.pregledi_i_kartoni.services.TerminService;
import ba.unsa.etf.pregledi_i_kartoni.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
public class TerminController {

    private final TerminService terminService;

    @PostMapping("/asyncTermini")
    public ResponseEntity<Response> asyncTermini(@RequestBody AsyncTerminiRequest asyncRequest) throws ParseException {
        String poruka = terminService.asyncTermini(asyncRequest);
        return ResponseEntity.ok(new Response(poruka));
    }


    @PostMapping("/asyncObrisiTermin")
    public ResponseEntity<Response> asyncObrisiTermin(@RequestBody AsyncObrisiTerminRequest asyncRequest) throws ParseException {
        String poruka = terminService.asyncObrisiTermin(asyncRequest);
        return ResponseEntity.ok(new Response(poruka));
    }


    // prikaz svih termina
    @GetMapping("/svi-termini")
    public ResponseEntity<List<TerminResponse>> dajSveTermine(){
        List<TerminResponse> trazeniTermini = terminService.dajSveTermine();
        return ResponseEntity.ok(trazeniTermini);
    }

    /*
    // prikaz termina na osnovu id
    @GetMapping("/termini/{idTermina}")
        public ResponseEntity<TerminResponse> dajTermin(@PathVariable(value = "idTermina") Long idTermina){
        TerminResponse trazeniTermin = terminService.dajTerminNaOsnovuId(idTermina);
        return ResponseEntity.ok(trazeniTermin);
    }
     */


    // pohrana termina
    @PostMapping("/dodaj-termin")
    public ResponseEntity<Response> dodajTermin(@RequestBody DodajTerminRequest dodajTerminRequest) {
        Response response = terminService.dodajTermin(dodajTerminRequest);
        return ResponseEntity.ok(response);
    }

    // brisanje termina
    @DeleteMapping("/obrisi-termin/{idTermina}")
    public ResponseEntity<Response> obrisiTermin(@PathVariable(value = "idTermina") Long idTermina) {
        Response response = terminService.obrisiTermin(idTermina);
        return ResponseEntity.ok(response);
    }

    // Ruta koja poziva "termini" mikroservis
    @GetMapping("/termini/{idTermin}")
    public ResponseEntity<String> dajTermine(@PathVariable(value = "idTermin") Long idTermin) {
        String trazeniTermin = terminService.dajTermin(idTermin);
        return ResponseEntity.ok(trazeniTermin);

    }

    @GetMapping("/termini-pacijenta/{id}")
    public  ResponseEntity<List<TerminResponse>> dajTerminePacijenta(@PathVariable Long id){
        List<TerminResponse> trazeniTermini = terminService.dajTerminePacijenta(id);
        return ResponseEntity.ok(trazeniTermini);
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

