package ba.unsa.etf.pregledi_i_kartoni.services;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.models.Korisnik;
import ba.unsa.etf.pregledi_i_kartoni.models.Korisnik;
import ba.unsa.etf.pregledi_i_kartoni.repositories.DoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.KorisnikRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.PacijentRepository;
import ba.unsa.etf.pregledi_i_kartoni.requests.AsyncRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajKorisnikaRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajKorisnikaRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.KorisnikResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.KorisnikResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.KorisnikResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final DoktorRepository doktorRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;
    private final PacijentRepository pacijentRepository;

    public Response dodajKorisnika(DodajKorisnikaRequest dodajKorisnikaRequest) {
        Korisnik noviKorisnik = new Korisnik(
                dodajKorisnikaRequest.getIme(), dodajKorisnikaRequest.getPrezime(), dodajKorisnikaRequest.getDatumRodjenja(),
                dodajKorisnikaRequest.getAdresa(), dodajKorisnikaRequest.getBrojTelefona(), dodajKorisnikaRequest.getEmail()
        );
        korisnikRepository.save(noviKorisnik);
        return new Response("Uspješno ste dodali korisnika!", 200);
    }


    public KorisnikResponse dajKorisnikaNaOsnovuId(Long idKorisnika) {
        String errorMessageKorisnik = String.format("Ne postoji korisnik sa id = '%d'!", idKorisnika);
        Korisnik trazeniKorisnik = korisnikRepository.findById(idKorisnika).orElseThrow(() -> new ResourceNotFoundException(errorMessageKorisnik));
        return new KorisnikResponse(trazeniKorisnik.getId(), trazeniKorisnik.getIme(), trazeniKorisnik.getPrezime(),
                trazeniKorisnik.getDatumRodjenja(), trazeniKorisnik.getAdresa(), trazeniKorisnik.getBrojTelefona(),
                trazeniKorisnik.getEmail()
        );

    }

    public List<KorisnikResponse> dajSveKorisnike() {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        List<KorisnikResponse> listaKorisnikResponse = new ArrayList<>();
        for (Korisnik korisnik : korisnici) {
            listaKorisnikResponse.add(new KorisnikResponse(korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(),
                            korisnik.getDatumRodjenja(), korisnik.getAdresa(), korisnik.getBrojTelefona(),
                            korisnik.getEmail()
                    )
            );
        }

        return listaKorisnikResponse;
    }

    public String asyncKorisnici(AsyncRequest asyncRequest) throws ParseException {

        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(asyncRequest.getDatumRodjenja());

        if(asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("DOKTOR")){
            Optional<Doktor> doktor = doktorRepository.findById(asyncRequest.getId());
            if(doktor.isPresent()) return "Async...";

            Doktor noviDoktor = new Doktor(asyncRequest.getId(), asyncRequest.getIme(),
                    asyncRequest.getPrezime(), date, asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(), asyncRequest.getEmail());
            doktorRepository.save(noviDoktor);
        }
        else if(asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("PACIJENT")){
            Optional<Pacijent> pacijent = pacijentRepository.findById(asyncRequest.getId());
            if(pacijent.isPresent()) return "Async...";

                Pacijent noviPacijent = new Pacijent(asyncRequest.getId(), asyncRequest.getIme(),
                    asyncRequest.getPrezime(), date, asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(), asyncRequest.getEmail());
            pacijentRepository.save(noviPacijent);
        }
        else if(asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("DOKTOR")){
            Optional<Doktor> doktor = doktorRepository.findById(asyncRequest.getId());
            if(doktor.isPresent()){
                doktor.get().setIme(asyncRequest.getIme());
                doktor.get().setPrezime(asyncRequest.getPrezime());
                doktor.get().setDatumRodjenja(date);
                doktor.get().setAdresa(asyncRequest.getAdresa());
                doktor.get().setEmail(asyncRequest.getEmail());
                doktor.get().setBrojTelefona(asyncRequest.getBrojTelefona());
                doktorRepository.save(doktor.get());
            }
        }
        else if(asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("PACIJENT")){
            Optional<Pacijent> pacijent = pacijentRepository.findById(asyncRequest.getId());
            if(pacijent.isPresent()){
                pacijent.get().setIme(asyncRequest.getIme());
                pacijent.get().setPrezime(asyncRequest.getPrezime());
                pacijent.get().setDatumRodjenja(date);
                pacijent.get().setAdresa(asyncRequest.getAdresa());
                pacijent.get().setEmail(asyncRequest.getEmail());
                pacijent.get().setBrojTelefona(asyncRequest.getBrojTelefona());
                pacijentRepository.save(pacijent.get());
            }
        }
        System.out.println("Async...");
        return "Async...";
    }
}
