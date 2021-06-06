package ba.unsa.etf.defaultgateway.controllers;

import ba.unsa.etf.defaultgateway.exceptions.ResourceNotFoundException;
import ba.unsa.etf.defaultgateway.exceptions.UnauthorizedException;
import ba.unsa.etf.defaultgateway.models.Korisnik;
import ba.unsa.etf.defaultgateway.requests.*;
import ba.unsa.etf.defaultgateway.responses.KorisnikResponse;
import ba.unsa.etf.defaultgateway.responses.LoginResponse;
import ba.unsa.etf.defaultgateway.responses.Response;
import ba.unsa.etf.defaultgateway.security.CurrentUser;
import ba.unsa.etf.defaultgateway.security.TrenutniKorisnikSecurity;
import ba.unsa.etf.defaultgateway.security.UserPrincipal;
import ba.unsa.etf.defaultgateway.services.KorisnikService;
import ba.unsa.etf.defaultgateway.util.ErrorHandlingHelper;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
public class KorisnikController {

    private final KorisnikService korisnikService;

    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    @GetMapping("/")
    public ResponseEntity<Response> pocetna() {
        String poruka = korisnikService.pripremiUloge();
        return ResponseEntity.ok(new Response(poruka));
    }

    @GetMapping("/inicijalizacija-baze")
    public ResponseEntity<Response> inicijalizacija() {
        String poruka = korisnikService.inicijalizirajBazu();
        return ResponseEntity.ok(new Response(poruka));
    }

    @PostMapping("/registracija")
    public ResponseEntity<Response> registracijaKorisnika(@Valid @RequestBody RegistracijaRequest registracijaRequest){
        Response odgovor = korisnikService.registrujKorisnika(registracijaRequest);
        return ResponseEntity.ok(odgovor);
    }

    @PutMapping("/uredjivanje-profila")
    public ResponseEntity<Response> urediProfil(@RequestHeader HttpHeaders headers, @Valid @RequestBody UredjivanjeProfilaRequest uredjivanjeProfilaRequest){

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, uredjivanjeProfilaRequest.getId()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        Response odgovor = korisnikService.urediProfil(uredjivanjeProfilaRequest);
        return ResponseEntity.ok(odgovor);
    }

    @PostMapping("/prijava")
    public ResponseEntity<LoginResponse> autentificirajKorisnika(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = korisnikService.autentificirajKorisnika(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profil")
    public ResponseEntity<Korisnik> getKorisnickiProfil(@CurrentUser UserPrincipal userPrincipal) {
        Korisnik korisnik = korisnikService.getKorisnikByKorisnickoIme(userPrincipal.getUsername());
        return ResponseEntity.ok(korisnik);
    }

    @PostMapping("/reset-token")
    public ResponseEntity<Response> getResetToken(@Valid @RequestBody GetResetTokenRequest getResetTokenRequest) throws MessagingException, IOException, TemplateException, javax.mail.MessagingException {
        Response response = korisnikService.generirajResetToken(getResetTokenRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifikacijski-podaci")
    public ResponseEntity<Response> verificiraj(@Valid @RequestBody VerificirajPodatkeRequest verificirajPodatkeRequest) throws MessagingException {
        Response response = korisnikService.verificirajPodatke(verificirajPodatkeRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/uredjivanje_lozinke")
    public ResponseEntity<Response> spasiLozinku(@RequestHeader HttpHeaders headers, @Valid @RequestBody SpasiLozinkuRequest spasiLozinkuRequest) throws MessagingException {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        Response response = korisnikService.promijeniLozinku(spasiLozinkuRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/korisnici")
    public List<KorisnikResponse> getKorisnici(@RequestParam(required = false) String uloga) {
        if (uloga == null) uloga = "";
        return korisnikService.getKorisnici(uloga);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final Response handleException(Exception e) {
        return ErrorHandlingHelper.handleException(e);
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