package ba.unsa.etf.pregledi_i_kartoni.services;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.repositories.*;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPregledRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.DoktorResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.PacijentResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.PregledResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.*;

@AllArgsConstructor
@Service
public class PregledService {

    private final PregledRepository pregledRepository;
    private final DoktorRepository doktorRepository;
    private final PacijentRepository pacijentRepository;
    private final KorisnikRepository korisnikRepository;
    private final PacijentDoktorRepository pacijentDoktorRepository;
    private final TerminRepository terminRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    public String incijalizirajBazu() {

 /*       korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();

        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();

        pacijentRepository.deleteAllInBatch();
        pacijentRepository.flush();

        pacijentDoktorRepository.deleteAllInBatch();
        pacijentDoktorRepository.flush();

        terminRepository.deleteAllInBatch();
        terminRepository.flush();

        pregledRepository.deleteAllInBatch();
        pregledRepository.flush();

        Doktor doktor1 = new Doktor("ImeDoktora1", "PrezimeDoktora1", new Date(), "AdresaDoktora1", "061-123-123", "nekimaildoktora1@gmail.com");
        Pacijent pacijent1 = new Pacijent( "ImePacijenta1", "PrezimePacijenta1", new Date(), "Adresa1", "061-456-456", "nekimailpacijenta1@gmail.com",
                "zenski", 1.85, 68.3, "0+", "/", "/");

        Doktor doktor2 = new Doktor("ImeDoktora2", "PrezimeDoktora2", new Date(), "AdresaDoktora2", "061-723-723", "nekimaildoktora2@gmail.com");
        Pacijent pacijent2 = new Pacijent( "ImePacijenta2", "PrezimePacijenta2", new Date(), "Adresa2", "061-323-323", "nekimailpacijenta2@gmail.com",
                "zenski", 1.34, 56.3, "A-", "/", "/");


        doktorRepository.save(doktor1);
        doktorRepository.save(doktor2);
        pacijentRepository.save(pacijent1);
        pacijentRepository.save(pacijent2);

        PacijentDoktor pd1 = new PacijentDoktor();
        pd1.setDoktor(doktor1);
        pd1.setPacijent(pacijent1);
        PacijentDoktor pd2 = new PacijentDoktor();
        pd2.setDoktor(doktor2);
        pd2.setPacijent(pacijent2);

        pacijentDoktorRepository.save(pd1);
        pacijentDoktorRepository.save(pd2);

        Termin termin1 = new Termin(new Date(), "15:20", pd1);
        Termin termin2 = new Termin(new Date(), "16:30", pd2);
        Termin termin3 = new Termin(new Date(2020, Calendar.MAY, 14),  "13:40", pd1);


        terminRepository.save(termin1);
        terminRepository.save(termin2);
        terminRepository.save(termin3);


        Pregled pregled1 = new Pregled("neki simptomi1", "neki fiz pregled1", "neka dijagnoza1",
                "neki tretman1", "neki komentar1", termin1);

        Pregled pregled2 = new Pregled("neki simptomi2", "neki fiz pregled2", "neka dijagnoza2",
                "neki tretman2", "neki komentar2", termin2);


        pregledRepository.save(pregled1);
        pregledRepository.save(pregled2);*/

        return "Inicijalizacija baze podataka završena!";
    }

    public PregledResponse dajPregledNaOsonvuId(Long idPregleda) {

        String errorMessagePregled = String.format("Ne postoji pregled sa id = '%d'!", idPregleda);
        Pregled trazeniPregled = pregledRepository.findById(idPregleda).orElseThrow(() -> new ResourceNotFoundException(errorMessagePregled));
        return new PregledResponse(trazeniPregled.getId(), trazeniPregled.getSimptomi(), trazeniPregled.getFizikalniPregled(),
                                   trazeniPregled.getDijagnoza(), trazeniPregled.getTretman(), trazeniPregled.getKomentar(),
                                   trazeniPregled.getTermin().getId()
        );



    }


    public List<PregledResponse> dajSvePreglede() {
        List<Pregled> pregledi = pregledRepository.findAll();
        List<PregledResponse> listaPregledResponse = new ArrayList<>();
        for (Pregled pregled : pregledi) {
            listaPregledResponse.add(new PregledResponse(pregled.getId(), pregled.getSimptomi(), pregled.getFizikalniPregled(),
                    pregled.getDijagnoza(), pregled.getTretman(), pregled.getKomentar(),
                    pregled.getTermin().getId()
                    )
            );
        }

        return listaPregledResponse;

    }


    public List<PregledResponse> filtrirajPreglede(Long idDoktor, Long idPacijent, Long idTermin) {

        Doktor trazeniDoktor = null;
        Pacijent trazeniPacijent = null;
        Termin trazeniTermin = null;


        if(idDoktor != null) {
            Optional<Doktor> trazeniDoktorOptional = doktorRepository.findById(idDoktor);
            if (trazeniDoktorOptional.isPresent()) {
                trazeniDoktor = trazeniDoktorOptional.get();
            }
        }

        if(idPacijent != null) {
            Optional<Pacijent> trazeniPacijentOptional = pacijentRepository.findById(idPacijent);
            if (trazeniPacijentOptional.isPresent()) {
                trazeniPacijent = trazeniPacijentOptional.get();
            }
        }

        if(idTermin != null) {
            Optional<Termin> trazeniTerminOptional = terminRepository.findById(idTermin);
            if (trazeniTerminOptional.isPresent()) {
                trazeniTermin = trazeniTerminOptional.get();
            }
        }



        List<Pregled> trazeniPregledi = pregledRepository.findByQueryPregled(trazeniDoktor, trazeniPacijent, trazeniTermin).get();

        List<PregledResponse> listaPregledResponse = new ArrayList<>();
        for (Pregled pregled : trazeniPregledi) {
            listaPregledResponse.add(new PregledResponse(pregled.getId(), pregled.getSimptomi(), pregled.getFizikalniPregled(),
                    pregled.getDijagnoza(), pregled.getTretman(), pregled.getKomentar(),
                    pregled.getTermin().getId()
                    )
            );

        }

        return listaPregledResponse;

    }



    public Response dodajPregled(HttpHeaders headers, DodajPregledRequest dodajPregledRequest) {


        if(dodajPregledRequest.getTerminId() == null) {
            throw new ResourceNotFoundException("Nije moguce dodati pregled bez termina!");
        }
        Optional<Termin> terminPregleda = terminRepository.findById(dodajPregledRequest.getTerminId());
        if(!terminPregleda.isPresent())
            throw new ResourceNotFoundException("Ne postoji zakazani termin za dati pregled!");

        Optional<List<Pregled>> pregledi = pregledRepository.findByQueryPregled(null, null, terminPregleda.get());
        if(!pregledi.get().isEmpty()) throw new ResourceNotFoundException("Već postoji pregled za dati termin!");

        Doktor trazeniDoktor = terminPregleda.get().getPacijentDoktor().getDoktor();
        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, trazeniDoktor.getId())) {
            throw new UnauthorizedException("Neovlašten pristup resursima!");
        }

        Pregled noviPregled = new Pregled(
                dodajPregledRequest.getSimptomi(), dodajPregledRequest.getFizikalniPregled(), dodajPregledRequest.getDijagnoza(),
                dodajPregledRequest.getTretman(), dodajPregledRequest.getKomentar(), terminPregleda.get()
        );
        pregledRepository.save(noviPregled);
        return new Response("Uspješno ste dodali pregled!", 200);
    }

    public Response obrisiPregled(HttpHeaders headers, Long id) {
        String errorBrisanjePregleda = String.format("Ne postoji pregled sa id = '%d'", id);
        Optional<Pregled> trazeniPregled = pregledRepository.findById(id);
        if(!trazeniPregled.isPresent()) {
            throw new ResourceNotFoundException(errorBrisanjePregleda);
        }

        Doktor trazeniDoktor = trazeniPregled.get().getTermin().getPacijentDoktor().getDoktor();
        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, trazeniDoktor.getId())) {
            throw new UnauthorizedException("Neovlašten pristup resursima!");
        }


        pregledRepository.deleteById(id);
        return new Response("Uspješno ste obrisali pregled!", 200);
    }


}
