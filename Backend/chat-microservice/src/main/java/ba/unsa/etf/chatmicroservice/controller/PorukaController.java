package ba.unsa.etf.chatmicroservice.controller;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Poruka;
import ba.unsa.etf.chatmicroservice.repository.PorukaRepository;
import ba.unsa.etf.chatmicroservice.request.DodajPorukuRequest;
import ba.unsa.etf.chatmicroservice.response.PorukaResponse;
import ba.unsa.etf.chatmicroservice.response.PorukePoUcesnicimaResponse;
import ba.unsa.etf.chatmicroservice.response.PorukePosiljaocaIPrimaocaResponse;
import ba.unsa.etf.chatmicroservice.response.Response;
import ba.unsa.etf.chatmicroservice.service.PorukaService;
import ba.unsa.etf.chatmicroservice.util.ErrorHandlingHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@AllArgsConstructor
@RestController
public class PorukaController {

    private final PorukaRepository porukaRepository;

    private final PorukaService porukaService;

    @GetMapping("/poruke")
    List<Poruka> all() {
        return porukaRepository.findAll();
    }

    @GetMapping("/poruke/{id}")
    public ResponseEntity<PorukaResponse> one(@PathVariable Long id) {
        PorukaResponse porukaResponse = porukaService.dajPoruku(id);
        return ResponseEntity.ok(porukaResponse);
    }

    @GetMapping("/poruke-po-posiljaocu-i-primaocu")
    public ResponseEntity<PorukePosiljaocaIPrimaocaResponse> dajPorukePoPosiljaocuIPrimaocu
            (@RequestParam Long idPosiljaoca, @RequestParam Long idPrimaoca) {
        PorukePosiljaocaIPrimaocaResponse porukePosiljaocaIPrimaocaResponse =
                porukaService.dajPorukePosiljaocaIPrimaoca(idPosiljaoca, idPrimaoca);
        return ResponseEntity.ok(porukePosiljaocaIPrimaocaResponse);
    }

    @GetMapping("/poruke-po-ucesnicima")
    public ResponseEntity<PorukePoUcesnicimaResponse> dajPorukePoUcesnicima
            (@RequestParam Long idPrvogUcesnika, @RequestParam Long idDrugogUcesnika) {
        PorukePoUcesnicimaResponse porukePoUcesnicimaResponse =
                porukaService.dajPorukePoUcesnicima(idPrvogUcesnika, idDrugogUcesnika);
        return ResponseEntity.ok(porukePoUcesnicimaResponse);
    }

    @PostMapping("/dodaj-poruku")
    public ResponseEntity<Response> dodajPoruku(@RequestBody DodajPorukuRequest dodajPorukuRequest) {
        Response response = porukaService.dodajPoruku(dodajPorukuRequest);
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
}

