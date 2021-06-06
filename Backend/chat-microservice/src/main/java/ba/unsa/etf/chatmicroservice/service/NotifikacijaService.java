package ba.unsa.etf.chatmicroservice.service;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Korisnik;
import ba.unsa.etf.chatmicroservice.model.Notifikacija;
import ba.unsa.etf.chatmicroservice.dto.NotifikacijaProjection;
import ba.unsa.etf.chatmicroservice.repository.KorisnikRepository;
import ba.unsa.etf.chatmicroservice.repository.NotifikacijaRepository;
import ba.unsa.etf.chatmicroservice.request.DodajNotifikacijuRequest;
import ba.unsa.etf.chatmicroservice.response.NotifikacijaResponse;
import ba.unsa.etf.chatmicroservice.response.NotifikacijeKorisnikaResponse;
import ba.unsa.etf.chatmicroservice.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class NotifikacijaService {

    private final NotifikacijaRepository notifikacijaRepository;
    private final KorisnikRepository korisnikRepository;

    public NotifikacijaResponse dajNotifikaciju(Long id) {
        Notifikacija notifikacija = notifikacijaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji notifikacija s ovim id-om!"));
        return new NotifikacijaResponse(
                notifikacija.getId(),
                notifikacija.getNaslov(),
                notifikacija.getTekst(),
                notifikacija.getDatum(),
                notifikacija.getVrijeme(),
                notifikacija.getKorisnik().getId()
        );
    }

    public NotifikacijeKorisnikaResponse dajNotifikacijeKorisnika(Long idKorisnika) {
        Korisnik korisnik = korisnikRepository.findById(idKorisnika)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji korisnik s ovim id-om!"));
        List<NotifikacijaProjection> notifikacije = notifikacijaRepository.findAllByKorisnik(korisnik);
        return new NotifikacijeKorisnikaResponse(notifikacije);
    }

    public Response dodajNotifikaciju(DodajNotifikacijuRequest dodajNotifikacijuRequest) {
        Korisnik korisnik = korisnikRepository.findById(dodajNotifikacijuRequest.getIdKorisnika())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji korisnik s ovim id-om!"));
        Notifikacija notifikacija = new Notifikacija(
                dodajNotifikacijuRequest.getNaslov(),
                dodajNotifikacijuRequest.getTekst(),
                dodajNotifikacijuRequest.getDatum(),
                dodajNotifikacijuRequest.getVrijeme(),
                korisnik
        );
        notifikacijaRepository.save(notifikacija);
        return new Response("Uspješno ste dodali notifikaciju!", 200);
    }

    public Response obrisiNotifikaciju(Long id) {
        Notifikacija notifikacija = notifikacijaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji notifikacija s ovim id-om!"));
        notifikacijaRepository.deleteById(id);
        return new Response("Uspješno ste obrisali notifikaciju!", 200);
    }
}
