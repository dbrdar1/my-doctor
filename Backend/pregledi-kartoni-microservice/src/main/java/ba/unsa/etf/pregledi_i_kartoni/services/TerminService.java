package ba.unsa.etf.pregledi_i_kartoni.services;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.models.*;
import ba.unsa.etf.pregledi_i_kartoni.models.Termin;
import ba.unsa.etf.pregledi_i_kartoni.repositories.DoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.PacijentDoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.PacijentRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.TerminRepository;
import ba.unsa.etf.pregledi_i_kartoni.requests.AsyncObrisiTerminRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.AsyncTerminiRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajTerminRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.*;
import ba.unsa.etf.pregledi_i_kartoni.responses.TerminResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TerminService {

    private final TerminRepository terminRepository;
    private final PacijentDoktorRepository pacijentDoktorRepository;
    private final PacijentRepository pacijentRepository;
    private final DoktorRepository doktorRepository;


    @Autowired
    private RestTemplate restTemplate;

    public String spasiTermine(List<Termin> termini) {
        terminRepository.deleteAllInBatch();
        terminRepository.flush();
        terminRepository.saveAll(termini);
        return "Spremljeno!";
    }

    public TerminResponse dajTerminNaOsnovuId(Long idTermina) {

        String errorMessageTermin = String.format("Ne postoji termin sa id = '%d'!", idTermina);
        Termin trazeniTermin = terminRepository.findById(idTermina).orElseThrow(() -> new ResourceNotFoundException(errorMessageTermin));
        return new TerminResponse(trazeniTermin.getId(), trazeniTermin.getDatumPregleda(), trazeniTermin.getVrijemePregleda(),
                trazeniTermin.getPacijentDoktor().getId()
        );

    }


    public List<TerminResponse> dajSveTermine() {
        List<Termin> termini = terminRepository.findAll();
        List<TerminResponse> listaTerminResponse = new ArrayList<>();
        for (Termin termin : termini) {
            listaTerminResponse.add(new TerminResponse(termin.getId(), termin.getDatumPregleda(), termin.getVrijemePregleda(),
                    termin.getPacijentDoktor().getId()
                    )
            );
        }

        return listaTerminResponse;

    }


    public Response dodajTermin(DodajTerminRequest dodajTerminRequest) {
        Optional<PacijentDoktor> pacijentDoktorVeza = pacijentDoktorRepository.findById(dodajTerminRequest.getPacijentDoktorId());
        if(!pacijentDoktorVeza.isPresent()) throw new ResourceNotFoundException("Ne postoji veza pacijent-doktor za dati termin!");
        Termin noviTermin = new Termin(1L,
                dodajTerminRequest.getDatumPregleda(), dodajTerminRequest.getVrijemePregleda(), pacijentDoktorVeza.get()
        );
        pacijentDoktorVeza.get().getTermini().add(noviTermin);
        pacijentDoktorRepository.save(pacijentDoktorVeza.get());
        //terminRepository.save(noviTermin);
        return new Response("Uspješno ste dodali termin pregleda!", 200);
    }

    public Response obrisiTermin(Long id) {

        String errorBrisanjeTermina = String.format("Ne postoji termin sa id = '%d'", id);
        Optional<Termin> trazeniTermin = terminRepository.findById(id);
        if(!trazeniTermin.isPresent()) {
            throw new ResourceNotFoundException(errorBrisanjeTermina);
        }
        terminRepository.deleteById(id);
        return new Response("Uspješno ste obrisali pregled!", 200);

    }

    // Komunikacija sa "termini" mikroservisom
    public String dajTermin(Long idTermina) {

        String fooResourceUrl = "http://termini/dobavi-termin/" + idTermina.toString();
        ResponseEntity<String> responseString = restTemplate.getForEntity(fooResourceUrl, String.class);
        ResponseEntity<ListaTerminaResponse> response = restTemplate.getForEntity(fooResourceUrl, ListaTerminaResponse.class);
        return responseString.getStatusCode() == HttpStatus.OK ? responseString.getBody() : null;

    }

    public List<TerminResponse> dajTerminePacijenta(Long id) {
        Pacijent p = pacijentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Ne postoji pacijent sa id = " + id.toString() + "!"));
        if(!terminRepository.findByPacijent(p).isPresent()) {
            throw new ResourceNotFoundException("Pacijent nema nijedan zakazan termin!");
        }
        List<Termin> terminiPacijenta = terminRepository.findByPacijent(p).get();
        List<TerminResponse> listaTerminResponse = new ArrayList<>();
        for (Termin t : terminiPacijenta) {
            listaTerminResponse.add(new TerminResponse(t.getId(), t.getDatumPregleda(), t.getVrijemePregleda(), t.getPacijentDoktor().getId()));
        }
        return listaTerminResponse;

    }

    public String asyncTermini(AsyncTerminiRequest asyncRequest) throws ParseException {

        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(asyncRequest.getDatum());

        Pacijent p = pacijentRepository.findById(asyncRequest.getIdPacijenta())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji pacijent s ovim id-om!"));
        Doktor d = doktorRepository.findById(asyncRequest.getIdDoktora())
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji doktor s ovim id-om!"));

        Response response = spasiVezuDoktorPacijent(asyncRequest.getIdDoktora(), asyncRequest.getIdPacijenta());
        System.out.println(response.getPoruka());

        Optional<PacijentDoktor> pkd = pacijentDoktorRepository.findByPacijentAndDoktor(p,d);
        if(!pkd.isPresent()) System.out.println("Greska. Id veze nije postojeći!");

        Termin termin= new Termin(asyncRequest.getId(), date, asyncRequest.getVrijeme(), pkd.get());
        pkd.get().getTermini().add(termin);
        pacijentDoktorRepository.save(pkd.get());

        System.out.println("Async...");
        return "Async...";
    }

    public Response spasiVezuDoktorPacijent(Long doktor, Long pacijent) {
        Optional<Pacijent> p = pacijentRepository.findById(pacijent);
        if(!p.isPresent()) return new Response("Id pacijenta nije postojeći!", 400);
        Optional<Doktor> d = doktorRepository.findById(doktor);
        if(!d.isPresent()) return new Response("Id doktora nije postojeći!", 400);

        PacijentDoktor pkd1;
        Optional<PacijentDoktor> pkd = pacijentDoktorRepository.findByPacijentAndDoktor(p.get(),d.get());
        if (!pkd.isPresent()) {
            pkd1 = new PacijentDoktor(d.get(), p.get());
            p.get().getVezeSaDoktorima().add(pkd1);
            pacijentRepository.save(p.get());
        }
        return new Response("Uspješno ste dodali vezu pacijent-doktor!", 200);
    }

    public String asyncObrisiTermin(AsyncObrisiTerminRequest asyncRequest) {
        Optional<Termin> termin = terminRepository.findById(asyncRequest.getId());
        if(!termin.isPresent()) return "Nepostojeći termin!";
        terminRepository.delete(termin.get());
        return "Termin uspješno obrisan!";
    }
}
