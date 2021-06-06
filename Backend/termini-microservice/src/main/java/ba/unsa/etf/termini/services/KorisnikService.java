package ba.unsa.etf.termini.services;

import ba.unsa.etf.termini.Requests.AsyncRequest;
import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.models.Korisnik;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import ba.unsa.etf.termini.repositories.KorisnikRepository;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final DoktorRepository doktorRepository;
    private final PacijentRepository pacijentRepository;

    public String spasiKorisnike(List<Korisnik> korisnici) {
        korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();
        korisnikRepository.saveAll(korisnici);
        return "Spremljeno!";
    }

    public String asyncKorisnici(AsyncRequest asyncRequest) throws ParseException {
        System.out.println ("USAO U SERVIS");
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(asyncRequest.getDatumRodjenja());

        if(asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("DOKTOR")){
            Optional<Doktor> doktor = doktorRepository.findById(asyncRequest.getId());
            if(doktor.isPresent()) return "Async...";

            Doktor novi = new Doktor(asyncRequest.getId(), asyncRequest.getIme(),
                    asyncRequest.getPrezime(), date, asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(), asyncRequest.getEmail());
            doktorRepository.save(novi);
            System.out.println("post DOKTORA");

        }
        if(asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("PACIJENT")){
            Optional<Pacijent> pacijent = pacijentRepository.findById(asyncRequest.getId());
            if(pacijent.isPresent()) return "Async...";

            Pacijent novi = new Pacijent(asyncRequest.getId(), asyncRequest.getIme(),
                    asyncRequest.getPrezime(), date, asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(), asyncRequest.getEmail());
            pacijentRepository.save(novi);
        }
        if(asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("DOKTOR")){
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
            System.out.println("ID ZA DOKTORA");
        }
        if(asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("PACIJENT")){
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
