package ba.unsa.etf.chatmicroservice.service;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.model.Doktor;
import ba.unsa.etf.chatmicroservice.repository.DoktorRepository;
import ba.unsa.etf.chatmicroservice.response.DoktorResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DoktorService {

    private final DoktorRepository doktorRepository;

    public DoktorResponse dajDoktora(Long id) {
        Doktor doktor = doktorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ne postoji doktor s ovim id-om!"));
        return new DoktorResponse(
                doktor.getIme(),
                doktor.getPrezime(),
                doktor.getAdresa(),
                doktor.getBrojTelefona(),
                doktor.getEmail()
        );
    }
}
