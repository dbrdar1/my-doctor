package ba.unsa.etf.chatmicroservice.service;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Pacijent;
import ba.unsa.etf.chatmicroservice.repository.PacijentRepository;
import ba.unsa.etf.chatmicroservice.response.PacijentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PacijentService {

    private final PacijentRepository pacijentRepository;

    public PacijentResponse dajPacijenta(Long id) {
        Pacijent pacijent = pacijentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji pacijent s ovim id-om!"));
        return new PacijentResponse(
                pacijent.getIme(),
                pacijent.getPrezime(),
                pacijent.getAdresa(),
                pacijent.getBrojTelefona(),
                pacijent.getEmail()
        );
    }
}
