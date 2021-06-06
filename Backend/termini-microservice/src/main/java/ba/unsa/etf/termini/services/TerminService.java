package ba.unsa.etf.termini.services;

import ba.unsa.etf.termini.Requests.DodajTerminRequest;
import ba.unsa.etf.termini.Requests.UrediTerminRequest;
import ba.unsa.etf.termini.Responses.*;
import ba.unsa.etf.termini.dto.TerminProjection;
import ba.unsa.etf.termini.exceptions.ResourceNotFoundException;
import ba.unsa.etf.termini.models.*;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentKartonDoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import ba.unsa.etf.termini.repositories.TerminRepository;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TerminService {
    private TerminRepository terminRepository;
    private PacijentKartonDoktorRepository pacijentKartonDoktorRepository;
    private PacijentRepository pacijentRepository;
    private DoktorRepository doktorRepository;
    private PacijentKartonDoktorService pacijentKartonDoktorService;

    private final RabbitTemplate template;
    private final Queue terminiDodavanje;
    private final Queue terminiBrisanje;

    public Response dodajTermin(DodajTerminRequest dodajTerminRequest) {

        String[] dijelovi = dodajTerminRequest.getVrijeme().split(":");
        int sat = Integer.parseInt(dijelovi[0]);

        System.out.println(dijelovi[0]);

        if(sat>=22 || sat<6) return new Response("Termin nije moguće zakazati u vremenu od 22h do 6h", 400);

        if(dodajTerminRequest.getDatum().before(new Date())) return new Response("Termin nije moguće zakazati u prošlom vremenu", 400);

        PacijentKartonDoktorResponse pkdRes = pacijentKartonDoktorService.dajVezuDoktorPacijent(dodajTerminRequest.getIdDoktora(),dodajTerminRequest.getIdPacijenta());
        Optional<PacijentKartonDoktor> pkd = pacijentKartonDoktorRepository.findById(pkdRes.getId());
        if(!pkd.isPresent()) return new Response("Id veze nije postojeći!", 400);
        Termin termin= new Termin(dodajTerminRequest.getDatum(), dodajTerminRequest.getVrijeme(), pkd.get());
        pkd.get().getTermini().add(termin);
        pacijentKartonDoktorRepository.save(pkd.get());

        Termin termin1 = terminRepository.findByPacijentKartonDoktorAndDatumAndVrijeme(pkd.get(), dodajTerminRequest.getDatum(), dodajTerminRequest.getVrijeme());

        AsyncTerminiResponse asyncTerminiResponse = new AsyncTerminiResponse(
                termin1.getId(),
                dodajTerminRequest.getIdDoktora(),
                dodajTerminRequest.getIdPacijenta(),
                dodajTerminRequest.getDatum().toString(),
                dodajTerminRequest.getVrijeme());
        sendAsync(asyncTerminiResponse);

        return new Response("Uspješno ste dodali termin!", 200);
    }

    public void sendAsync(AsyncTerminiResponse response) {
        JSONObject paket = new JSONObject();
        paket.put("id", response.getId());
        paket.put("idDoktora", response.getIdDoktora());
        paket.put("idPacijenta", response.getIdPacijenta());
        paket.put("datum", response.getDatum());
        paket.put("vrijeme", response.getVrijeme());

        String message = paket.toString();
        this.template.convertAndSend(terminiDodavanje.getName(), message);

        System.out.println("Sent: " + terminiDodavanje.getName() + message);
    }

    public Response obrisiTermin(Long id) {
        Termin termin = terminRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Ne postoji termin s ovim id-om!"));
        terminRepository.deleteById(id);

        JSONObject paket = new JSONObject();
        paket.put("id", id);
        String message = paket.toString();
        this.template.convertAndSend(terminiBrisanje.getName(), message);
        System.out.println("Sent: " + terminiBrisanje.getName() + message);

        return new Response("Uspješno ste obrisali termin!", 200);
    }

    public Response urediTermin(Long id, UrediTerminRequest urediTerminRequest) {
        Termin termin = terminRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Ne postoji termin s ovim id-om!"));
        termin.setDatum(urediTerminRequest.getDatum());
        termin.setVrijeme(urediTerminRequest.getVrijeme());
        terminRepository.save(termin);
        return new Response("Uspješno ste uredili termin!",200);
    }

    public ListaTerminaResponse dajTerminePacijenta(Long id) {
        Pacijent p = pacijentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Ne postoji pacijent s ovim id-om!"));
        List<PacijentKartonDoktor> veze = pacijentKartonDoktorRepository.findAllByPacijent(p);
        List<TerminProjection> sviTermini = new ArrayList<>();
        for (int i =0; i< veze.size();i++){
            List<TerminProjection> t = terminRepository.findAllByPacijentKartonDoktor(veze.get(i));
            sviTermini.addAll(t);
        }
        return  new ListaTerminaResponse(sviTermini);
    }

    public ListaTerminaResponse dajTermineDoktora(Long id) {
        Doktor d = doktorRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Ne postoji doktor s ovim id-om!"));
        List<PacijentKartonDoktor> veze = pacijentKartonDoktorRepository.findAllByDoktor(d);
        List<TerminProjection> sviTermini = new ArrayList<>();
        for (int i =0; i< veze.size();i++){
            List<TerminProjection> t = terminRepository.findAllByPacijentKartonDoktor(veze.get(i));
            sviTermini.addAll(t);
        }
        return  new ListaTerminaResponse(sviTermini);
    }

    public TerminResponse dajTermin(Long id) {
        Termin t = terminRepository.findById(id).orElseThrow(() -> new ba.unsa.etf.termini.exceptions.ResourceNotFoundException("Ne postoji termin s ovim id-om!"));
        return new TerminResponse(t.getDatum(),t.getVrijeme());
    }

//    public Response obrisiTerminPoAtributima(ObrisiTerminRequest obrisiTerminRequest) {
//        Termin t;
//        try{
//            t= terminRepository.findByDatumAndVrijeme(obrisiTerminRequest.getDatum(), obrisiTerminRequest.getVrijeme());
//        } catch (Exception e){
//            throw new ResourceNotFoundException("Ne postoji termin s ovim datumom i vremenom!");
//        }
//        terminRepository.delete(t);
//        return new Response("Termin uspjesno obrisan!");
//    }
}
