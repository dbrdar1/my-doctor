package ba.unsa.etf.termini.services;

import ba.unsa.etf.termini.Requests.DodajNotifikacijuRequest;
import ba.unsa.etf.termini.Responses.NotifikacijaResponse;
import ba.unsa.etf.termini.Responses.NotifikacijeKorisnikaResponse;
import ba.unsa.etf.termini.Responses.Response;
import ba.unsa.etf.termini.exceptions.ResourceNotFoundException;
import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.models.Notifikacija;
import ba.unsa.etf.termini.repositories.KorisnikRepository;
import ba.unsa.etf.termini.repositories.NotifikacijaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NotifikacijaService {

    private final NotifikacijaRepository notifikacijaRepository;
    private KorisnikRepository korisnikRepository;

    public Response dodajNotifikaciju(DodajNotifikacijuRequest dodajNotifikacijuRequest) {
        Optional<Korisnik> k = korisnikRepository.findById(dodajNotifikacijuRequest.getIdKorisnika());
        if(!k.isPresent()) return new Response("Id korisnika nije postojeći!", 400);
        Notifikacija n = new Notifikacija(dodajNotifikacijuRequest.getTekst(),
                dodajNotifikacijuRequest.getDatum(),k.get());
        k.get().getNotifikacije().add(n);
        korisnikRepository.save(k.get());
        System.out.println("dodaje notifikaciju u bazu");
        System.out.println(k.toString());
        System.out.println(n.toString());
        return new Response("Uspješno ste dodali notifikaciju!", 200);
    }

    public NotifikacijeKorisnikaResponse dajNotifikacijeKorisnika (Long idKorisnika){
        Korisnik k = korisnikRepository.findById(idKorisnika).orElseThrow(() -> new ba.unsa.etf.termini.exceptions.ResourceNotFoundException("Ne postoji korisnik s ovim id-om!"));
        List<Notifikacija> notifikacije = notifikacijaRepository.findAllByKorisnik(k);
        return new NotifikacijeKorisnikaResponse(notifikacije);
    }

}