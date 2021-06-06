package ba.unsa.etf.chatmicroservice.controller;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Pacijent;
import ba.unsa.etf.chatmicroservice.repository.PacijentRepository;
import ba.unsa.etf.chatmicroservice.response.PacijentResponse;
import ba.unsa.etf.chatmicroservice.response.Response;
import ba.unsa.etf.chatmicroservice.service.PacijentService;
import ba.unsa.etf.chatmicroservice.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@AllArgsConstructor
@RestController
public class PacijentController {

    private final PacijentService pacijentService;

    private final PacijentRepository pacijentRepository;

    @GetMapping("/pacijenti")
    List<Pacijent> all() {
        return pacijentRepository.findAll();
    }

    @GetMapping("/pacijenti/{id}")
    public ResponseEntity<PacijentResponse> one(@PathVariable Long id) {
        PacijentResponse pacijentResponse = pacijentService.dajPacijenta(id);
        return ResponseEntity.ok(pacijentResponse);
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
}

