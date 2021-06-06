package ba.unsa.etf.chatmicroservice.controller;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Korisnik;
import ba.unsa.etf.chatmicroservice.repository.KorisnikRepository;
import ba.unsa.etf.chatmicroservice.request.AsyncRequest;
import ba.unsa.etf.chatmicroservice.response.KorisnikResponse;
import ba.unsa.etf.chatmicroservice.response.Response;
import ba.unsa.etf.chatmicroservice.service.KorisnikService;
import ba.unsa.etf.chatmicroservice.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.List;

@AllArgsConstructor
@RestController
public class KorisnikController {

    private final KorisnikRepository korisnikRepository;

    private final KorisnikService korisnikService;

    @GetMapping("/")
    public @ResponseBody String inicijalizacija(){
        return korisnikService.inicijalizirajBazu();
    }

    @GetMapping("/korisnici")
    List<Korisnik> all() {
        return korisnikRepository.findAll();
    }

    @GetMapping("/korisnici/{id}")
    public ResponseEntity<KorisnikResponse> one(@PathVariable Long id) {
        KorisnikResponse korisnikResponse = korisnikService.dajKorisnika(id);
        return ResponseEntity.ok(korisnikResponse);
    }

    @PostMapping("/async")
    public ResponseEntity<Response> asyncKorisnici(@RequestBody AsyncRequest asyncRequest) throws ParseException {
        String poruka = korisnikService.asyncKorisnici(asyncRequest);
        return ResponseEntity.ok(new Response(poruka));
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

