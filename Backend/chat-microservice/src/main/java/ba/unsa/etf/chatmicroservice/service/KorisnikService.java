package ba.unsa.etf.chatmicroservice.service;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.*;
import ba.unsa.etf.chatmicroservice.repository.*;
import ba.unsa.etf.chatmicroservice.request.AsyncRequest;
import ba.unsa.etf.chatmicroservice.response.KorisnikResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final DoktorRepository doktorRepository;
    private final PacijentRepository pacijentRepository;
    private final NotifikacijaRepository notifikacijaRepository;
    private final PorukaRepository porukaRepository;


    public String inicijalizirajBazu() {

        korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();

        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();

        pacijentRepository.deleteAllInBatch();
        pacijentRepository.flush();

        notifikacijaRepository.deleteAllInBatch();
        notifikacijaRepository.flush();

        porukaRepository.deleteAllInBatch();
        porukaRepository.flush();

        Date date = new Date();

        Doktor doktor = new Doktor(
                "Samra",
                "Pusina",
                date,
                "NekaAdresa1",
                "spusina1@etf.unsa.ba",
                "061111222");

        Pacijent pacijent = new Pacijent(
                "Esmina",
                "Radusic",
                date,
                "NekaAdresa2",
                "eradusic1@etf.unsa.ba",
                "061111222");

        Notifikacija notifikacija = new Notifikacija(
                "naziv notifikacije",
                "imate novu poruku",
                date,
                "14:00",
                pacijent);
        String timestampAkcijeNow =
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Sarajevo")).toInstant()).toString();
        Poruka poruka = new Poruka(
                "dje si",
                timestampAkcijeNow,
                doktor,
                pacijent);

        doktorRepository.save(doktor);
        pacijentRepository.save(pacijent);
        notifikacijaRepository.save(notifikacija);
        porukaRepository.save(poruka);

        return "Inicijalizacija baze završena!";
    }

    public KorisnikResponse dajKorisnika(Long id) {
        Korisnik korisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji korisnik s ovim id-om!"));
        return new KorisnikResponse(
                korisnik.getIme(),
                korisnik.getPrezime(),
                korisnik.getAdresa(),
                korisnik.getBrojTelefona(),
                korisnik.getEmail()
        );
    }

    public String asyncKorisnici(AsyncRequest asyncRequest) throws ParseException {

        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(asyncRequest.getDatumRodjenja());

        if (asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("DOKTOR")) {
            Optional<Doktor> doktorOptional = doktorRepository.findById(asyncRequest.getId());
            if (doktorOptional.isPresent()) return "Async...";

            Doktor noviDoktor = new Doktor(
                    asyncRequest.getId(),
                    asyncRequest.getIme(),
                    asyncRequest.getPrezime(),
                    date,
                    asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(),
                    asyncRequest.getEmail()
            );
            doktorRepository.save(noviDoktor);
        }
        else if (asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("DOKTOR")) {
            Optional<Doktor> doktorOptional = doktorRepository.findById(asyncRequest.getId());
            if (doktorOptional.isPresent()) {
                doktorOptional.get().setIme(asyncRequest.getIme());
                doktorOptional.get().setPrezime(asyncRequest.getPrezime());
                doktorOptional.get().setDatumRodjenja(date);
                doktorOptional.get().setAdresa(asyncRequest.getAdresa());
                doktorOptional.get().setBrojTelefona(asyncRequest.getBrojTelefona());
                doktorOptional.get().setEmail(asyncRequest.getEmail());
                doktorRepository.save(doktorOptional.get());
            }
        }
        else if (asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("PACIJENT")) {
            Optional<Pacijent> pacijentOptional = pacijentRepository.findById(asyncRequest.getId());
            if (pacijentOptional.isPresent()) return "Async...";

            Pacijent noviPacijent = new Pacijent(
                    asyncRequest.getId(),
                    asyncRequest.getIme(),
                    asyncRequest.getPrezime(),
                    date,
                    asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(),
                    asyncRequest.getEmail()
            );
            pacijentRepository.save(noviPacijent);
        }
        else if (asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("PACIJENT")) {
        Optional<Pacijent> pacijentOptional = pacijentRepository.findById(asyncRequest.getId());
        if (pacijentOptional.isPresent()) {
            pacijentOptional.get().setIme(asyncRequest.getIme());
            pacijentOptional.get().setPrezime(asyncRequest.getPrezime());
            pacijentOptional.get().setDatumRodjenja(date);
            pacijentOptional.get().setAdresa(asyncRequest.getAdresa());
            pacijentOptional.get().setBrojTelefona(asyncRequest.getBrojTelefona());
            pacijentOptional.get().setEmail(asyncRequest.getEmail());
            pacijentRepository.save(pacijentOptional.get());
        }
    }
        System.out.println("Async...");
        return "Async...";
    }
}
