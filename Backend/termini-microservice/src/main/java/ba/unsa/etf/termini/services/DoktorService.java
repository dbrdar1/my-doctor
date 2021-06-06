package ba.unsa.etf.termini.services;

import ba.unsa.etf.termini.Responses.DoktorResponse;
import ba.unsa.etf.termini.models.Doktor;
import ba.unsa.etf.termini.repositories.DoktorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DoktorService {
    private DoktorRepository doktorRepository;

    public String spasiDoktore(List<Doktor> korisnici) {
        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();
        doktorRepository.saveAll(korisnici);
        return "Spremljeni doktori!";
    }

    public DoktorResponse dajDoktora(Long id) {
        Doktor d = doktorRepository.findById(id).orElseThrow(() -> new ba.unsa.etf.termini.exceptions.ResourceNotFoundException("Ne postoji doktor s ovim id-om!"));
        return new DoktorResponse(d.getIme(),d.getPrezime(),d.getTitula(),d.getAdresa(),d.getBrojTelefona(),d.getEmail());
    }
}
