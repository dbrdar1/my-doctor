package ba.unsa.etf.doktordetalji.services;

import ba.unsa.etf.doktordetalji.exceptions.UnauthorizedException;
import ba.unsa.etf.doktordetalji.models.Doktor;
import ba.unsa.etf.doktordetalji.models.Edukacija;
import ba.unsa.etf.doktordetalji.repositories.DoktorRepository;
import ba.unsa.etf.doktordetalji.repositories.EdukacijaRepository;
import ba.unsa.etf.doktordetalji.requests.DodajEdukacijuRequest;
import ba.unsa.etf.doktordetalji.requests.UrediEdukacijuRequest;
import ba.unsa.etf.doktordetalji.responses.Response;
import ba.unsa.etf.doktordetalji.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class EdukacijaService {

    private final DoktorRepository doktorRepository;
    private final EdukacijaRepository edukacijaRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    public Response dodajEdukaciju(DodajEdukacijuRequest dodajEdukacijuRequest) {
        Optional<Doktor> d = doktorRepository.findById(dodajEdukacijuRequest.getIdDoktora());
        if (!d.isPresent()) return new Response("Id doktora nije postojeći!", 400);
        Edukacija c = new Edukacija
                (
                        dodajEdukacijuRequest.getInstitucija(),
                        dodajEdukacijuRequest.getOdsjek(),
                        dodajEdukacijuRequest.getStepen(),
                        dodajEdukacijuRequest.getGodinaPocetka(),
                        dodajEdukacijuRequest.getGodinaZavrsetka(),
                        dodajEdukacijuRequest.getGrad(),
                        dodajEdukacijuRequest.getDrzava()
                );
        c.setDoktor(d.get());
        d.get().getEdukacije().add(c);
        doktorRepository.save(d.get());
        return new Response("Uspješno ste dodali edukaciju!", 200);
    }

    public Response urediEdukaciju(HttpHeaders headers, UrediEdukacijuRequest urediEdukacijuRequest) {
        Optional<Edukacija> c = edukacijaRepository.findById(urediEdukacijuRequest.getId());
        if (!c.isPresent()) return new Response("Nepostojeći Id edukacije!", 400);

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, c.get().getDoktor().getId()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        c.get().setInstitucija(urediEdukacijuRequest.getInstitucija());
        c.get().setOdsjek(urediEdukacijuRequest.getOdsjek());
        c.get().setStepen(urediEdukacijuRequest.getStepen());
        c.get().setGodinaPocetka(urediEdukacijuRequest.getGodinaPocetka());
        c.get().setGodinaZavrsetka(urediEdukacijuRequest.getGodinaZavrsetka());
        c.get().setGrad(urediEdukacijuRequest.getGrad());
        c.get().setDrzava(urediEdukacijuRequest.getDrzava());
        edukacijaRepository.save(c.get());
        return new Response("Uspješno ste uredili edukaciju!", 200);
    }

    public Response obrisiEdukaciju(HttpHeaders headers, Long id) {
        Optional<Edukacija> c = edukacijaRepository.findById(id);
        if (!c.isPresent()) return new Response("Nepostojeći Id edukacije!", 400);

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, c.get().getDoktor().getId()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        edukacijaRepository.delete(c.get());
        return new Response("Uspješno ste obrisali edukaciju!", 200);
    }
}
