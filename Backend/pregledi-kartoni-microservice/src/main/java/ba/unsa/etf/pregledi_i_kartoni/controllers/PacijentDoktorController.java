package ba.unsa.etf.pregledi_i_kartoni.controllers;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPacijentDoktorRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.services.PacijentDoktorService;
import ba.unsa.etf.pregledi_i_kartoni.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
public class PacijentDoktorController {

    private final PacijentDoktorService pacijentDoktorService;

    private final List<PacijentDoktor> pacijentiDoktori;


    @PostMapping("/dodaj-vezu-pacijent-doktor")
    public ResponseEntity<Response> spasiVezuPacijentDoktor(@RequestHeader HttpHeaders headers, @RequestBody DodajPacijentDoktorRequest dodajPacijentDoktorRequest) {

        Response response = pacijentDoktorService.spasiVezuPacijentDoktor(headers, dodajPacijentDoktorRequest.getPacijentId(),
                                                                          dodajPacijentDoktorRequest.getDoktorId()
                                                                         );
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

