package ba.unsa.etf.doktordetalji.services;

import ba.unsa.etf.doktordetalji.exceptions.ResourceNotFoundException;
import ba.unsa.etf.doktordetalji.models.*;
import ba.unsa.etf.doktordetalji.repositories.CertifikatRepository;
import ba.unsa.etf.doktordetalji.repositories.DoktorRepository;
import ba.unsa.etf.doktordetalji.repositories.EdukacijaRepository;
import ba.unsa.etf.doktordetalji.requests.AsyncRequest;
import ba.unsa.etf.doktordetalji.requests.FilterRequest;
import ba.unsa.etf.doktordetalji.requests.OcjenaRequest;
import ba.unsa.etf.doktordetalji.requests.UrediPodatkeDoktoraRequest;
import ba.unsa.etf.doktordetalji.responses.*;
import ba.unsa.etf.doktordetalji.security.TrenutniKorisnikSecurity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DoktorService {

    private final DoktorRepository doktorRepository;
    private final EdukacijaRepository edukacijaRepository;
    private final CertifikatRepository certifikatRepository;
    private final TrenutniKorisnikSecurity trenutniKorisnikSecurity;

    public List<Doktor> getDoktori(FilterRequest filterRequest) {
        List<Doktor> doktori = doktorRepository.findByFilter(filterRequest);
        if (doktori.size() == 0) throw new ResourceNotFoundException("Doktor nije pronađen!");
        return doktori;
    }

    public DoktorCVResponse getDoktorCV(Long id) {

        Doktor d = doktorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ne postoji doktor s ovim id-om!"));
        List<Edukacija> edukacije = edukacijaRepository.findByDoktor(d);
        List<Certifikat> certifikati = certifikatRepository.findByDoktor(d);

        return new DoktorCVResponse(
                d.getIme(),
                d.getPrezime(),
                d.getAdresa(),
                d.getEmail(),
                d.getTitula(),
                d.getBiografija(),
                prosjecnaOcjena(d),
                edukacije
                        .stream()
                        .map(e -> new EdukacijeResponse(e.getInstitucija(), e.getOdsjek(), e.getStepen(), e.getGodinaPocetka(), e.getGodinaZavrsetka()))
                        .collect(Collectors.toList()),
                certifikati
                        .stream()
                        .map(c -> new CertifikatiResponse(c.getInstitucija(), c.getNaziv(), c.getGodinaIzdavanja()))
                        .collect(Collectors.toList())
        );
    }

    public Response ocjeni(OcjenaRequest ocjenaRequest) {
        Optional<Doktor> d = doktorRepository.findById(ocjenaRequest.getId());
        if (!d.isPresent()) return new Response("Id doktora nije postojeći!", 400);
        Ocjena ocjena = new Ocjena(ocjenaRequest.getOcjena());
        ocjena.setDoktor(d.get());
        d.get().getOcjene().add(ocjena);
        doktorRepository.save(d.get());
        return new Response("Uspješno ste ocijenili doktora!", 200);
    }

    public Double prosjecnaOcjena(Doktor doktor) {
        List<Ocjena> ocjene = doktor.getOcjene();
        Double suma = 0.0;
        for (Ocjena ocjena : ocjene) {
            suma += ocjena.getVrijednost();
        }
        return suma / ocjene.size();
    }

    public Response urediPodatkeDoktora(UrediPodatkeDoktoraRequest urediPodatkeDoktoraRequest) {
        Optional<Doktor> d = doktorRepository.findById(urediPodatkeDoktoraRequest.getIdDoktora());
        if (!d.isPresent()) return new Response("Id doktora nije postojeći!", 400);
        d.get().setTitula(urediPodatkeDoktoraRequest.getTitula());
        d.get().setBiografija(urediPodatkeDoktoraRequest.getBiografija());
        doktorRepository.save(d.get());
        return new Response("Uspješno ste uredili osnovne podatke doktora!", 200);
    }


    public String azurirajPodatke(List<KorisnikResponse> korisnikResponses) {
        List<Korisnik> korisnici = korisnikResponses
                .stream()
                .map(korisnikResponse -> new Korisnik(
                        korisnikResponse.getId(),
                        korisnikResponse.getIme(),
                        korisnikResponse.getPrezime(),
                        korisnikResponse.getDatumRodjenja(),
                        korisnikResponse.getAdresa(),
                        korisnikResponse.getBrojTelefona(),
                        korisnikResponse.getEmail()
                )).collect(Collectors.toList());
        for (Korisnik korisnik : korisnici) {
            Optional<Doktor> d = doktorRepository.findById(korisnik.getId());
            if (!d.isPresent()) {
                Doktor novi = new Doktor(korisnik.getId(), korisnik.getIme(),
                        korisnik.getPrezime(), korisnik.getDatumRodjenja(), korisnik.getAdresa(),
                        korisnik.getBrojTelefona(), korisnik.getEmail(), "", "");
                doktorRepository.save(novi);
            } else {
                d.get().setIme(korisnik.getIme());
                d.get().setPrezime(korisnik.getPrezime());
                d.get().setDatumRodjenja(korisnik.getDatumRodjenja());
                d.get().setAdresa(korisnik.getAdresa());
                d.get().setEmail(korisnik.getEmail());
                doktorRepository.save(d.get());
            }
        }
        System.out.println("Sync...");
        return "Sync...";
    }

    public String asyncKorisnici(AsyncRequest asyncRequest) throws ParseException {

        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(asyncRequest.getDatumRodjenja());

        if(asyncRequest.getAkcija().equals("POST") && asyncRequest.getUloga().equals("DOKTOR")){
            Optional<Doktor> doktor = doktorRepository.findById(asyncRequest.getId());
            if(doktor.isPresent()) return "Async...";

            Doktor novi = new Doktor(asyncRequest.getId(), asyncRequest.getIme(),
                    asyncRequest.getPrezime(), date, asyncRequest.getAdresa(),
                    asyncRequest.getBrojTelefona(), asyncRequest.getEmail(), "", "");
            doktorRepository.save(novi);
        }
        else if(asyncRequest.getAkcija().equals("PUT") && asyncRequest.getUloga().equals("DOKTOR")){
            Optional<Doktor> doktor = doktorRepository.findById(asyncRequest.getId());
            if(doktor.isPresent()){
                doktor.get().setIme(asyncRequest.getIme());
                doktor.get().setPrezime(asyncRequest.getPrezime());
                doktor.get().setDatumRodjenja(date);
                doktor.get().setAdresa(asyncRequest.getAdresa());
                doktor.get().setEmail(asyncRequest.getEmail());
                doktor.get().setBrojTelefona(asyncRequest.getBrojTelefona());
                doktorRepository.save(doktor.get());
            }
        }
        System.out.println("Async...");
        return "Async...";
    }
}

