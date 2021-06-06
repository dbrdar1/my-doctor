package ba.unsa.etf.pregledi_i_kartoni.services;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Pacijent;
import ba.unsa.etf.pregledi_i_kartoni.models.PacijentDoktor;
import ba.unsa.etf.pregledi_i_kartoni.repositories.DoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.PacijentDoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.PacijentRepository;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PacijentDoktorService {

    private final PacijentDoktorRepository pacijentDoktorRepository;
    private final PacijentRepository pacijentRepository;
    private final DoktorRepository doktorRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;


    public Response spasiVezuPacijentDoktor(HttpHeaders headers, Long idPacijenta, Long idDoktora) {

        String errorMessageDoktor = String.format("Ne postoji doktor sa id = '%d'!", idDoktora);
        String errorMessagePacijent = String.format("Ne postoji pacijent sa id = '%d'!", idPacijenta);

        Optional<Pacijent> trazeniPacijent = pacijentRepository.findById(idPacijenta);
        if(!trazeniPacijent.isPresent()) throw new ResourceNotFoundException(errorMessagePacijent);
        Optional<Doktor> trazeniDoktor = doktorRepository.findById(idDoktora);
        if(!trazeniDoktor.isPresent()) throw new ResourceNotFoundException(errorMessageDoktor);

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, trazeniDoktor.get().getId())) {
            throw new UnauthorizedException("Neovlašten pristup resursima!");
        }

        PacijentDoktor pacijentDoktorVeza = new PacijentDoktor(trazeniDoktor.get(), trazeniPacijent.get());
        //PacijentDoktor pacijentDoktorVeza = new PacijentDoktor();
        //trazeniPacijent.get().getVezeSaDoktorima().add(pacijentDoktorVeza);
        //pacijentRepository.save(trazeniPacijent.get());
        //pacijentDoktorVeza.setDoktor(trazeniDoktor.get());
        //pacijentDoktorVeza.setPacijent(trazeniPacijent.get());
        pacijentDoktorRepository.save(pacijentDoktorVeza);
        return new Response("Uspješno ste dodali vezu pacijent-doktor!", 200);
    }
}
