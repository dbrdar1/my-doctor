package ba.unsa.etf.pregledi_i_kartoni.services;

import ba.unsa.etf.pregledi_i_kartoni.exceptions.ResourceNotFoundException;
import ba.unsa.etf.pregledi_i_kartoni.exceptions.UnauthorizedException;
import ba.unsa.etf.pregledi_i_kartoni.models.Doktor;
import ba.unsa.etf.pregledi_i_kartoni.models.Korisnik;
import ba.unsa.etf.pregledi_i_kartoni.models.Pacijent;
import ba.unsa.etf.pregledi_i_kartoni.repositories.DoktorRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.KorisnikRepository;
import ba.unsa.etf.pregledi_i_kartoni.repositories.PacijentRepository;
import ba.unsa.etf.pregledi_i_kartoni.requests.DodajPacijentaRequest;
import ba.unsa.etf.pregledi_i_kartoni.requests.UrediKartonRequest;
import ba.unsa.etf.pregledi_i_kartoni.responses.KartonResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.PacijentResponse;
import ba.unsa.etf.pregledi_i_kartoni.responses.Response;
import ba.unsa.etf.pregledi_i_kartoni.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PacijentService {

    private final PacijentRepository pacijentRepository;
    private final DoktorRepository doktorRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    public Response dodajPacijenta(DodajPacijentaRequest dodajPacijentaRequest) {

        Pacijent noviPacijent = new Pacijent(
                dodajPacijentaRequest.getIme(), dodajPacijentaRequest.getPrezime(), dodajPacijentaRequest.getDatumRodjenja(),
                dodajPacijentaRequest.getAdresa(), dodajPacijentaRequest.getBrojTelefona(), dodajPacijentaRequest.getEmail(),
                dodajPacijentaRequest.getSpol(), dodajPacijentaRequest.getVisina(), dodajPacijentaRequest.getTezina(),
                dodajPacijentaRequest.getKrvnaGrupa(), dodajPacijentaRequest.getHronicneBolesti(), dodajPacijentaRequest.getHronicnaTerapija()
        );
        pacijentRepository.save(noviPacijent);
        return new Response("Uspješno ste dodali pacijenta!", 200);
    }


    // pacijent kao korisnik
    public PacijentResponse dajPacijentaNaOsnovuId(Long id) {
        String errorMessage = String.format("Ne postoji pacijent sa id = '%d'", id);
        Pacijent trazeniPacijent = pacijentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(errorMessage));
        return new PacijentResponse(trazeniPacijent.getId(), trazeniPacijent.getIme(), trazeniPacijent.getPrezime(),
                                    trazeniPacijent.getDatumRodjenja(), trazeniPacijent.getAdresa(), trazeniPacijent.getBrojTelefona(),
                                    trazeniPacijent.getEmail()
                                   );
    }


    // svi pacijenti kao korisnici
    public List<PacijentResponse> dajSvePacijente() {
        List<Pacijent> pacijenti = pacijentRepository.findAll();
        List<PacijentResponse> listaPacijentResponse = new ArrayList<>();
        for (Pacijent pacijent : pacijenti) {
            listaPacijentResponse.add(new PacijentResponse(pacijent.getId(), pacijent.getIme(), pacijent.getPrezime(),
                                                           pacijent.getDatumRodjenja(), pacijent.getAdresa(), pacijent.getBrojTelefona(),
                                                           pacijent.getEmail()
                                                          )
                                     );
        }

        return listaPacijentResponse;
    }


    // prikaz kartona pacijenta - uloga pacijent
    public KartonResponse dajKartonNaOsnovuIdPacijentUloga(HttpHeaders headers, Long idPacijent) {

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, idPacijent)) {
            throw new UnauthorizedException("Neovlašten pristup resursima!");
        }

        String errorMessage = String.format("Ne postoji pacijent sa id = '%d'", idPacijent);
        Pacijent trazeniPacijent = pacijentRepository.findById(idPacijent).orElseThrow(() -> new ResourceNotFoundException(errorMessage));

        return new KartonResponse(trazeniPacijent.getId(), trazeniPacijent.getIme(), trazeniPacijent.getPrezime(),
                trazeniPacijent.getDatumRodjenja(), trazeniPacijent.getAdresa(), trazeniPacijent.getBrojTelefona(),
                trazeniPacijent.getEmail(), trazeniPacijent.getSpol(), trazeniPacijent.getVisina(),
                trazeniPacijent.getTezina(), trazeniPacijent.getKrvnaGrupa(), trazeniPacijent.getHronicneBolesti(),
                trazeniPacijent.getHronicnaTerapija()
        );
    }

    // prikaz kartona pacijenta - uloga doktor
    public KartonResponse dajKartonNaOsnovuIdDoktorUloga(HttpHeaders headers, Long idPacijent) {

        List<Doktor> doktoriPacijenta = doktorRepository.findDoktoriPacijenta(idPacijent).get();

        boolean doktorPacijenta = false;

        for(Doktor d : doktoriPacijenta) {
            if (trenutniKorisnikSecurity.isTrenutniKorisnik(headers, d.getId())) {
                doktorPacijenta = true;
                break;
            }
        }

        if(!doktorPacijenta)
            throw new UnauthorizedException("Neovlašten pristup resursima!");


        String errorMessage = String.format("Ne postoji pacijent sa id = '%d'", idPacijent);
        Pacijent trazeniPacijent = pacijentRepository.findById(idPacijent).orElseThrow(() -> new ResourceNotFoundException(errorMessage));

        return new KartonResponse(trazeniPacijent.getId(), trazeniPacijent.getIme(), trazeniPacijent.getPrezime(),
                trazeniPacijent.getDatumRodjenja(), trazeniPacijent.getAdresa(), trazeniPacijent.getBrojTelefona(),
                trazeniPacijent.getEmail(), trazeniPacijent.getSpol(), trazeniPacijent.getVisina(),
                trazeniPacijent.getTezina(), trazeniPacijent.getKrvnaGrupa(), trazeniPacijent.getHronicneBolesti(),
                trazeniPacijent.getHronicnaTerapija()
        );
    }


    // svi kartoni pacijenata
    public List<KartonResponse> dajSveKartone() {
        List<Pacijent> pacijenti = pacijentRepository.findAll();
        List<KartonResponse> listaKartonResponse = new ArrayList<>();
        for (Pacijent pacijent : pacijenti) {
            listaKartonResponse.add(new KartonResponse(pacijent.getId(), pacijent.getIme(), pacijent.getPrezime(),
                            pacijent.getDatumRodjenja(), pacijent.getAdresa(), pacijent.getBrojTelefona(),
                            pacijent.getEmail(), pacijent.getSpol(), pacijent.getVisina(),
                            pacijent.getTezina(), pacijent.getKrvnaGrupa(), pacijent.getHronicneBolesti(),
                            pacijent.getHronicnaTerapija()
                    )
            );
        }

        return listaKartonResponse;
    }


    public List<KartonResponse> filtrirajKartone(String ime, String prezime, String spol,
                                                 String krvnaGrupa, String hronicneBolesti, String hronicnaTerapija) {

        List<Pacijent> trazeniPacijenti = pacijentRepository.findByQueryKarton(ime, prezime, spol, krvnaGrupa, hronicneBolesti, hronicnaTerapija).get();
        List<KartonResponse> listaKartonResponse = new ArrayList<>();
        for (Pacijent pacijent : trazeniPacijenti) {
            listaKartonResponse.add(new KartonResponse(pacijent.getId(), pacijent.getIme(), pacijent.getPrezime(),
                            pacijent.getDatumRodjenja(), pacijent.getAdresa(), pacijent.getBrojTelefona(),
                            pacijent.getEmail(), pacijent.getSpol(), pacijent.getVisina(),
                            pacijent.getTezina(), pacijent.getKrvnaGrupa(), pacijent.getHronicneBolesti(),
                            pacijent.getHronicnaTerapija()
                    )
            );

        }

        return listaKartonResponse;

    }

    public List<PacijentResponse> filtrirajPacijente(String ime, String prezime) {

        List<Pacijent> trazeniPacijenti = pacijentRepository.findByQueryPacijent(ime, prezime).get();
        List<PacijentResponse> listaPacijentResponse = new ArrayList<>();
        for (Pacijent pacijent : trazeniPacijenti) {
            listaPacijentResponse.add(new PacijentResponse(pacijent.getId(), pacijent.getIme(), pacijent.getPrezime(),
                            pacijent.getDatumRodjenja(), pacijent.getAdresa(), pacijent.getBrojTelefona(),
                            pacijent.getEmail()
                    )
            );

        }

        return listaPacijentResponse;

    }

    public List<PacijentResponse> filtrirajPacijenteDoktora(HttpHeaders headers, Long idDoktor, String ime, String prezime) {

        if(!trenutniKorisnikSecurity.isTrenutniKorisnik(headers, idDoktor)) {
            throw new UnauthorizedException("Neovlašten pristup resursima!");
        }

        List<Pacijent> trazeniPacijenti = pacijentRepository.findPacijentiDoktoraFiltrirano(idDoktor, ime, prezime).get();

        List<PacijentResponse> listaPacijentResponse = new ArrayList<>();
        for (Pacijent pacijent : trazeniPacijenti) {
            listaPacijentResponse.add(new PacijentResponse(pacijent.getId(), pacijent.getIme(), pacijent.getPrezime(),
                            pacijent.getDatumRodjenja(), pacijent.getAdresa(), pacijent.getBrojTelefona(),
                            pacijent.getEmail()
                    )
            );

        }

        return listaPacijentResponse;

    }

    public List<PacijentResponse> dajPacijenteDoktora(HttpHeaders headers, Long idDoktor) {
       return filtrirajPacijenteDoktora(headers, idDoktor, null, null);
    }



    public Response urediKarton(HttpHeaders headers, Long idPacijent, UrediKartonRequest urediKartonRequest) {

        String errorNepostojeciKarton = String.format("Ne postoji pacijent sa id = '%d'", idPacijent);
        Optional<Pacijent> karton = pacijentRepository.findById(idPacijent);
        if(!karton.isPresent()) {
            throw new ResourceNotFoundException(errorNepostojeciKarton);
        }

        List<Doktor> doktoriPacijenta = doktorRepository.findDoktoriPacijenta(idPacijent).get();

        boolean doktorPacijenta = false;

        for(Doktor d : doktoriPacijenta) {
            if (trenutniKorisnikSecurity.isTrenutniKorisnik(headers, d.getId())) {
                doktorPacijenta = true;
                break;
            }
        }

        if(!doktorPacijenta)
            throw new UnauthorizedException("Neovlašten pristup resursima!");


        Pacijent trazeniKarton = karton.get();
        trazeniKarton.setIme(urediKartonRequest.getIme());
        trazeniKarton.setPrezime(urediKartonRequest.getPrezime());
        trazeniKarton.setDatumRodjenja(urediKartonRequest.getDatumRodjenja());
        trazeniKarton.setAdresa(urediKartonRequest.getAdresa());
        trazeniKarton.setBrojTelefona(urediKartonRequest.getBrojTelefona());
        trazeniKarton.setEmail(urediKartonRequest.getEmail());
        trazeniKarton.setSpol(urediKartonRequest.getSpol());
        trazeniKarton.setVisina(urediKartonRequest.getVisina());
        trazeniKarton.setTezina(urediKartonRequest.getTezina());
        trazeniKarton.setKrvnaGrupa(urediKartonRequest.getKrvnaGrupa());
        trazeniKarton.setHronicneBolesti(urediKartonRequest.getHronicneBolesti());
        trazeniKarton.setHronicnaTerapija(urediKartonRequest.getHronicnaTerapija());
        pacijentRepository.save(trazeniKarton);
        return new Response("Uspješno ste uredili karton!",200);

    }




}
