package ba.unsa.etf.pregledi_i_kartoni.controllers;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Korisnik;
import ba.unsa.etf.pregledi_i_kartoni.requests.AsyncRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajDoktoraRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajKorisnikaRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.KorisnikResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.services.DoktorService;
import ba.unsa.etf.pregledi_i_kartoni.services.KorisnikService;
import ba.unsa.etf.pregledi_i_kartoni.util.ErrorHandlingHelper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import ba.unsa.etf.grpc.ActionRequest;
import ba.unsa.etf.grpc.ActionResponse;
import ba.unsa.etf.grpc.SystemEventsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


@AllArgsConstructor
@RestController
public class KorisnikController {

    private final KorisnikService korisnikService;

    @PostMapping("/async")
    public ResponseEntity<Response> asyncKorisnici(@RequestBody AsyncRequest asyncRequest) throws ParseException {
        String poruka = korisnikService.asyncKorisnici(asyncRequest);
        return ResponseEntity.ok(new Response(poruka));

    }

    // prikaz jednog korisnika na osnovu id
    // slanje zahtjeva System Event servisu
    @GetMapping("/korisnici/{idKorisnika}")
    public ResponseEntity<KorisnikResponse> dajKorisnika(@PathVariable(value = "idKorisnika") Long idKorisnika){

        /*
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8866)
                .usePlaintext()
                .build();

        SystemEventsServiceGrpc.SystemEventsServiceBlockingStub stub =
                SystemEventsServiceGrpc.newBlockingStub(channel);

        ActionResponse actionResponse = stub.registrujAkciju(ActionRequest.newBuilder()
            .setTimestampAkcije(Timestamp.from(ZonedDateTime.now(
                    ZoneId.of("Europe/Sarajevo")
            ).toInstant()).toString())
            .setNazivMikroservisa("pregledi-i-kartoni")
            .setResurs("/korisnici/" + idKorisnika.toString())
            .setTipAkcije(ActionRequest.TipAkcije.GET)
            .setTipOdgovoraNaAkciju(ActionRequest.TipOdgovoraNaAkciju.USPJEH)
            .build());

        System.out.println("Response received from server:\n" + actionResponse);

        channel.shutdown();
        */

        KorisnikResponse trazeniKorisnik = korisnikService.dajKorisnikaNaOsnovuId(idKorisnika);
        return ResponseEntity.ok(trazeniKorisnik);
    }

    // prikaz svih korisnika
    @GetMapping("/svi-korisnici")
    public ResponseEntity<List<KorisnikResponse>> dajSveKorisnike(){
        List<KorisnikResponse> sviKorisnici = korisnikService.dajSveKorisnike();
        return ResponseEntity.ok(sviKorisnici);
    }

    // pohrana korisnika
    @PostMapping("/dodaj-korisnika")
    public ResponseEntity<Response> dodajKorisnika(@RequestBody DodajKorisnikaRequest dodajKorisnikaRequest) {
        Response response = korisnikService.dodajKorisnika(dodajKorisnikaRequest);
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

