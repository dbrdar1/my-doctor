package ba.unsa.etf.doktordetalji.services;

import ba.unsa.etf.doktordetalji.exceptions.UnauthorizedException;
import ba.unsa.etf.doktordetalji.models.Certifikat;
import ba.unsa.etf.doktordetalji.models.Doktor;
import ba.unsa.etf.doktordetalji.repositories.CertifikatRepository;
import ba.unsa.etf.doktordetalji.repositories.DoktorRepository;
import ba.unsa.etf.doktordetalji.requests.DodajCertifikatRequest;
import ba.unsa.etf.doktordetalji.requests.UrediCertifikatRequest;
import ba.unsa.etf.doktordetalji.responses.Response;
import ba.unsa.etf.doktordetalji.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CertifikatService {

    private final DoktorRepository doktorRepository;
    private final CertifikatRepository certifikatRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    public Response dodajCertifikat(DodajCertifikatRequest dodajCertifikatRequest) {
        Optional<Doktor> d = doktorRepository.findById(dodajCertifikatRequest.getIdDoktora());
        if (!d.isPresent()) return new Response("Id doktora nije postojeći!", 400);
        Certifikat c = new Certifikat(dodajCertifikatRequest.getInstitucija(), dodajCertifikatRequest.getNaziv(), dodajCertifikatRequest.getGodinaIzdavanja());
        c.setDoktor(d.get());
        d.get().getCertifikati().add(c);
        doktorRepository.save(d.get());
        return new Response("Uspješno ste dodali certifikat!", 200);

    }

    public Response urediCertifikat(HttpHeaders headers, UrediCertifikatRequest urediCertifikatRequest) {
        Optional<Certifikat> c = certifikatRepository.findById(urediCertifikatRequest.getId());
        if (!c.isPresent()) return new Response("Nepostojeći Id certifikata!", 400);

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, c.get().getDoktor().getId()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        c.get().setInstitucija(urediCertifikatRequest.getInstitucija());
        c.get().setNaziv(urediCertifikatRequest.getNaziv());
        c.get().setGodinaIzdavanja(urediCertifikatRequest.getGodinaIzdavanja());
        certifikatRepository.save(c.get());
        return new Response("Uspješno ste uredili certifikat!", 200);
    }

    public Response obrisiCertifikat(HttpHeaders headers, Long id) {
        Optional<Certifikat> c = certifikatRepository.findById(id);
        if (!c.isPresent()) return new Response("Nepostojeći Id certifikata!", 400);

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, c.get().getDoktor().getId()))
            throw new UnauthorizedException("Neovlašten pristup resursima!");

        certifikatRepository.delete(c.get());
        return new Response("Uspješno ste obrisali certifikat!", 200);
    }
}
