package ba.unsa.etf.termini.services;

import ba.unsa.etf.termini.Responses.PacijentKartonDoktorResponse;
import ba.unsa.etf.termini.Responses.Response;
import ba.unsa.etf.termini.exceptions.ResourceNotFoundException;
import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.models.Pacijent;
import ba.unsa.etf.termini.models.PacijentKartonDoktor;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentKartonDoktorRepository;
import ba.unsa.etf.termini.repositories.PacijentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class PacijentKartonDoktorService {
    private DoktorRepository doktorRepository;
    private PacijentRepository pacijentRepository;

    private PacijentKartonDoktorRepository pacijentKartonDoktorRepository;

    public Response spasiVezuDoktorPacijent(Long pacijent, Long doktor) {
        Optional<Pacijent> p = pacijentRepository.findById(pacijent);
        if(!p.isPresent()) return new Response("Id pacijenta nije postojeći!", 400);
        Optional<Doktor> d = doktorRepository.findById(doktor);
        if(!d.isPresent()) return new Response("Id doktora nije postojeći!", 400);
        PacijentKartonDoktor pkd = new PacijentKartonDoktor(d.get(),p.get());
        p.get().getVezeSaDoktorima().add(pkd);
        pacijentRepository.save(p.get());
        return new Response("Uspješno ste dodali vezu pacijent-doktor!", 200);
    }

    public PacijentKartonDoktorResponse dajVezuDoktorPacijent(Long idDoktora, Long idPacijenta) {
        Pacijent p = pacijentRepository.findById(idPacijenta)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji pacijent s ovim id-om!"));
        Doktor d = doktorRepository.findById(idDoktora)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji doktor s ovim id-om!"));
        Optional<PacijentKartonDoktor> pkd = pacijentKartonDoktorRepository.findByPacijentAndDoktor(p,d);
        if (pkd.isPresent())
           return new PacijentKartonDoktorResponse(pkd.get().getId());
        else {
            spasiVezuDoktorPacijent(idPacijenta, idDoktora);
            Optional<PacijentKartonDoktor> veza = pacijentKartonDoktorRepository.findByPacijentAndDoktor(p,d);
            return new PacijentKartonDoktorResponse(veza.get().getId());
        }
    }
}
