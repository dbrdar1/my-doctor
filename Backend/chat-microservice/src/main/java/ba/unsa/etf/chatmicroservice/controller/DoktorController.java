package ba.unsa.etf.chatmicroservice.controller;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Doktor;
import ba.unsa.etf.chatmicroservice.repository.DoktorRepository;
import ba.unsa.etf.chatmicroservice.response.DoktorResponse;
import ba.unsa.etf.chatmicroservice.response.Response;
import ba.unsa.etf.chatmicroservice.service.DoktorService;
import ba.unsa.etf.chatmicroservice.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@AllArgsConstructor
@RestController
public class DoktorController {

    private final DoktorRepository doktorRepository;

    private final DoktorService doktorService;

    @GetMapping("/doktori")
    List<Doktor> all() {
        return doktorRepository.findAll();
    }

    @GetMapping("/doktori/{id}")
    public ResponseEntity<DoktorResponse> one(@PathVariable Long id) {
        DoktorResponse doktorResponse = doktorService.dajDoktora(id);
        return ResponseEntity.ok(doktorResponse);
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

