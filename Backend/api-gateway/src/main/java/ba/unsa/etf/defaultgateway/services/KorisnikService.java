package ba.unsa.etf.defaultgateway.services;

import ba.unsa.etf.defaultgateway.exceptions.ResourceNotFoundException;
import ba.unsa.etf.defaultgateway.models.*;
import ba.unsa.etf.defaultgateway.repositories.DoktorRepository;
import ba.unsa.etf.defaultgateway.repositories.KorisnickaUlogaRepository;
import ba.unsa.etf.defaultgateway.repositories.KorisnikRepository;
import ba.unsa.etf.defaultgateway.repositories.PacijentRepository;
import ba.unsa.etf.defaultgateway.requests.*;
import ba.unsa.etf.defaultgateway.responses.AsyncResponse;
import ba.unsa.etf.defaultgateway.responses.KorisnikResponse;
import ba.unsa.etf.defaultgateway.responses.LoginResponse;
import ba.unsa.etf.defaultgateway.responses.Response;
import ba.unsa.etf.defaultgateway.security.JwtTokenProvider;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnickaUlogaRepository korisnickaUlogaRepository;
    private final DoktorRepository doktorRepository;
    private final PacijentRepository pacijentRepository;


    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private final RabbitTemplate template;
    private final Queue korisnikQueue1;
    private final Queue korisnikQueue2;
    private final Queue korisnikQueue3;
    private final Queue korisnikQueue4;

    public String pripremiUloge() {
        KorisnickaUloga doktor = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_DOKTOR);
        KorisnickaUloga pacijent = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_PACIJENT);

        if(doktor == null){
            KorisnickaUloga u1 = new KorisnickaUloga(1L, NazivKorisnickeUloge.ROLE_DOKTOR);
            korisnickaUlogaRepository.save(u1);
        }
        if(pacijent == null){
            KorisnickaUloga u2 = new KorisnickaUloga(3L, NazivKorisnickeUloge.ROLE_PACIJENT);
            korisnickaUlogaRepository.save(u2);
        }
        return "Baza spremna!";
    }

    public String inicijalizirajBazu() {

        korisnikRepository.deleteAllInBatch();
        korisnikRepository.flush();

        doktorRepository.deleteAllInBatch();
        doktorRepository.flush();

        pacijentRepository.deleteAllInBatch();
        pacijentRepository.flush();

        korisnickaUlogaRepository.deleteAllInBatch();
        korisnickaUlogaRepository.flush();

        String lozinka = "password";

        Doktor d = new Doktor(
                "Samra",
                "Pusina",
                new Date(1998, 5, 21),
                "NekaAdresa",
                "061456321",
                "spusina1@etf.unsa.ba",
                "spusina1",
                "password");

        Doktor d2 = new Doktor(
                "Jusuf",
                "Delalic",
                new Date(1998, 7, 7),
                "NekaAdresa",
                "061456323",
                "jdelalic1@etf.unsa.ba",
                "jdelalic1",
                "password");

        Pacijent p = new Pacijent(
                "Esmina",
                "Radusic",
                new Date(1998, 5, 21),
                "NekaAdresa",
                "061456322",
                "eradusi1@etf.unsa.ba",
                "eradusic1",
                "password");

        Pacijent p2 = new Pacijent(
                "Dzavid",
                "Brdar",
                new Date(1998, 5, 2),
                "NekaAdresa",
                "061456324",
                "dbrdar1@etf.unsa.ba",
                "dbrdar1",
                "password");

        d.setLozinka(passwordEncoder.encode(d.getLozinka()));
        d2.setLozinka(passwordEncoder.encode(d2.getLozinka()));
        p.setLozinka(passwordEncoder.encode(p.getLozinka()));
        p2.setLozinka(passwordEncoder.encode(p2.getLozinka()));

        KorisnickaUloga doktor = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_DOKTOR);
        KorisnickaUloga pacijent = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_PACIJENT);

        if(doktor == null){
            KorisnickaUloga u1 = new KorisnickaUloga(1L, NazivKorisnickeUloge.ROLE_DOKTOR);
            korisnickaUlogaRepository.save(u1);
        }
        if(pacijent == null){
            KorisnickaUloga u2 = new KorisnickaUloga(3L, NazivKorisnickeUloge.ROLE_PACIJENT);
            korisnickaUlogaRepository.save(u2);
        }

        List<KorisnickaUloga> uloge = Collections.singletonList(korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_DOKTOR));
        d.setUloge(new HashSet<>(uloge));
        d2.setUloge(new HashSet<>(uloge));

        List<KorisnickaUloga> uloge2 = Collections.singletonList(korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_PACIJENT));
        p.setUloge(new HashSet<>(uloge2));
        p2.setUloge(new HashSet<>(uloge2));

        doktorRepository.save(d);
        doktorRepository.save(d2);
        pacijentRepository.save(p);
        pacijentRepository.save(p2);

        return "Inicijalizacija baze zavrsena!";
    }

    public LoginResponse autentificirajKorisnika(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getKorisnickoIme(),
                        loginRequest.getLozinka()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        Optional<Korisnik> k = korisnikRepository.findByKorisnickoIme(loginRequest.getKorisnickoIme());
        Set<KorisnickaUloga> korisnickeUloge = k.get().getUloge();

        KorisnickaUloga doktor = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_DOKTOR);
        KorisnickaUloga pacijent = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_PACIJENT);

        if(korisnickeUloge.contains(doktor)) return new LoginResponse(k.get().getId(), "DOKTOR", jwt);
        return new LoginResponse(k.get().getId(), "PACIJENT", jwt);
    }

    public Korisnik getKorisnikByKorisnickoIme(String username) {
        String errorMessage = String.format("Nepostojeće korisničko ime '%s'", username);
        return korisnikRepository
                .findByKorisnickoIme(username)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }

    public Response generirajResetToken(GetResetTokenRequest getResetTokenRequest) throws MessagingException, IOException, TemplateException, javax.mail.MessagingException {
        Optional<Korisnik> user = korisnikRepository.findByKorisnickoIme(getResetTokenRequest.getUserInfo());
        if (!user.isPresent()) {
            user = korisnikRepository.findByEmail(getResetTokenRequest.getUserInfo());
            if (!user.isPresent()) {
                return new Response("Korisničko ime ili email koji ste unijeli nije validan. Provjerite i pokušajte ponovo!", 400);
            }
        }
        String resetToken = UUID.randomUUID().toString();
        user.get().setResetToken(resetToken);
        korisnikRepository.save(user.get());
        System.out.println(user.get().getEmail());
        mailService.sendmail(user.get().getEmail(), user.get().getIme(), resetToken);
        return new Response("Token je poslan!", 200);

    }

    public Response verificirajPodatke(VerificirajPodatkeRequest verificirajPodatkeRequest) {

        Optional<Korisnik> user = korisnikRepository.findByKorisnickoIme(verificirajPodatkeRequest.getUserInfo());
        if (!user.isPresent()) {
            user = korisnikRepository.findByEmail(verificirajPodatkeRequest.getUserInfo());
            if (!user.isPresent()) {
                return new Response("Neispravni verifikacijski podaci!", 400);
            }
        }
        if (user.get().getResetToken().equals(verificirajPodatkeRequest.getResetToken())) {
            return new Response("OK", 200);
        }
        return new Response("Neispravni verifikacijski podaci!", 400);
    }

    public Response promijeniLozinku(SpasiLozinkuRequest spasiLozinkuRequest) {

        Optional<Korisnik> user = korisnikRepository.findByKorisnickoIme(spasiLozinkuRequest.getUserInfo());
        if (!user.isPresent()) {
            user = korisnikRepository.findByEmail(spasiLozinkuRequest.getUserInfo());
            if (!user.isPresent()) {
                return new Response("Korisničko ime ili email koji ste unijeli nije validan. Provjerite i pokušajte ponovo!", 400);
            }
        }
        user.get().setLozinka(passwordEncoder.encode(spasiLozinkuRequest.getNovaLozinka()));
        user.get().setResetToken("");
        korisnikRepository.save(user.get());
        return new Response("Lozinka uspješno promijenjena!", 200);
    }

    public List<KorisnikResponse> getKorisnici(String uloga) {
        if (uloga.equals("")) {
            List<Korisnik> korisnikList = korisnikRepository.findAll();
            return korisnikList
                    .stream()
                    .map(korisnik -> new KorisnikResponse(korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getDatumRodjenja(), korisnik.getAdresa(), korisnik.getBrojTelefona(), korisnik.getEmail()))
                    .collect(Collectors.toList());
        } else if (uloga.equalsIgnoreCase("DOKTOR")) {
            List<Doktor> korisnikList = doktorRepository.findAll();
            return korisnikList
                    .stream()
                    .map(korisnik -> new KorisnikResponse(korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getDatumRodjenja(), korisnik.getAdresa(), korisnik.getBrojTelefona(), korisnik.getEmail()))
                    .collect(Collectors.toList());
        } else if (uloga.equalsIgnoreCase("PACIJENT")) {
            List<Pacijent> korisnikList = pacijentRepository.findAll();
            return korisnikList
                    .stream()
                    .map(korisnik -> new KorisnikResponse(korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getDatumRodjenja(), korisnik.getAdresa(), korisnik.getBrojTelefona(), korisnik.getEmail()))
                    .collect(Collectors.toList());
        } else return new ArrayList<>();
    }


    public Response registrujKorisnika(RegistracijaRequest registracijaRequest) {
        Optional<Korisnik> korisnik = korisnikRepository.findByKorisnickoIme(registracijaRequest.getKorisnickoIme());
        if(korisnik.isPresent()) return new Response("Već postoji korisnik s ovim korisničkim imenom.", 409);

        Optional<Korisnik> korisnik1 = korisnikRepository.findByEmail(registracijaRequest.getEmail());
        if(korisnik1.isPresent()) return new Response("Već postoji korisnik s ovim email-om.", 409);

        Optional<Korisnik> korisnik3 = korisnikRepository.findByBrojTelefona(registracijaRequest.getBrojTelefona());
        if(korisnik3.isPresent()) return new Response("Već postoji korisnik s ovim brojem telefona.", 409);


        AsyncResponse response = new AsyncResponse();

        response.setIme(registracijaRequest.getIme());
        response.setPrezime(registracijaRequest.getPrezime());
        response.setDatumRodjenja(registracijaRequest.getDatumRodjenja().toString());
        response.setAdresa(registracijaRequest.getAdresa());
        response.setBrojTelefona(registracijaRequest.getBrojTelefona());
        response.setEmail(registracijaRequest.getEmail());

        if(registracijaRequest.getUloga().equals("DOKTOR")){
            Doktor d = new Doktor(
                    registracijaRequest.getIme(),
                    registracijaRequest.getPrezime(),
                    registracijaRequest.getDatumRodjenja(),
                    registracijaRequest.getAdresa(),
                    registracijaRequest.getBrojTelefona(),
                    registracijaRequest.getEmail(),
                    registracijaRequest.getKorisnickoIme(),
                    registracijaRequest.getLozinka());
            d.setLozinka(passwordEncoder.encode(d.getLozinka()));
            List<KorisnickaUloga> uloge = Collections.singletonList(korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_DOKTOR));
            d.setUloge(new HashSet<>(uloge));
            doktorRepository.save(d);
            response.setUloga("DOKTOR");
        }
        else{
            Pacijent p = new Pacijent(
                    registracijaRequest.getIme(),
                    registracijaRequest.getPrezime(),
                    registracijaRequest.getDatumRodjenja(),
                    registracijaRequest.getAdresa(),
                    registracijaRequest.getBrojTelefona(),
                    registracijaRequest.getEmail(),
                    registracijaRequest.getKorisnickoIme(),
                    registracijaRequest.getLozinka());
            p.setLozinka(passwordEncoder.encode(p.getLozinka()));
            List<KorisnickaUloga> uloge = Collections.singletonList(korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_PACIJENT));
            p.setUloge(new HashSet<>(uloge));
            pacijentRepository.save(p);
            response.setUloga("PACIJENT");
        }
        Optional<Korisnik> korisnik2 = korisnikRepository.findByKorisnickoIme(registracijaRequest.getKorisnickoIme());
        response.setId(korisnik2.get().getId());
        response.setAkcija("POST");

        sendAsync(response);

        return new Response("Uspješna registracija!", 200);
    }



    public Response urediProfil(UredjivanjeProfilaRequest uredjivanjeProfilaRequest) {
        Optional<Korisnik> korisnik = korisnikRepository.findByKorisnickoIme(uredjivanjeProfilaRequest.getKorisnickoIme());
        if(korisnik.isPresent() && !korisnik.get().getId().equals(uredjivanjeProfilaRequest.getId())) return new Response("Već postoji korisnik s ovim korisničkim imenom.", 409);

        Optional<Korisnik> korisnik1 = korisnikRepository.findByEmail(uredjivanjeProfilaRequest.getEmail());
        if(korisnik1.isPresent() && !korisnik1.get().getId().equals(uredjivanjeProfilaRequest.getId())) return new Response("Već postoji korisnik s ovim email-om.", 409);

        Optional<Korisnik> korisnik2 = korisnikRepository.findByBrojTelefona(uredjivanjeProfilaRequest.getBrojTelefona());
        if(korisnik2.isPresent() && !korisnik2.get().getId().equals(uredjivanjeProfilaRequest.getId())) return new Response("Već postoji korisnik s ovim telefonskim brojem.", 409);

        Optional<Korisnik> k = korisnikRepository.findById(uredjivanjeProfilaRequest.getId());

        if (!k.isPresent()) {
            return new Response("Ne postoji korisnik s ovim korisničkim imenom!", 404);
        }

        k.get().setIme(uredjivanjeProfilaRequest.getIme());
        k.get().setPrezime(uredjivanjeProfilaRequest.getPrezime());
        k.get().setKorisnickoIme(uredjivanjeProfilaRequest.getKorisnickoIme());
        k.get().setAdresa(uredjivanjeProfilaRequest.getAdresa());
        k.get().setEmail(uredjivanjeProfilaRequest.getEmail());
        k.get().setBrojTelefona(uredjivanjeProfilaRequest.getBrojTelefona());
        k.get().setDatumRodjenja(uredjivanjeProfilaRequest.getDatumRodjenja());

        KorisnickaUloga doktor = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_DOKTOR);
        KorisnickaUloga pacijent = korisnickaUlogaRepository.findByNazivKorisnickeUloge(NazivKorisnickeUloge.ROLE_PACIJENT);

        korisnikRepository.save(k.get());

        AsyncResponse response = new AsyncResponse();

        response.setId(uredjivanjeProfilaRequest.getId());
        response.setIme(uredjivanjeProfilaRequest.getIme());
        response.setPrezime(uredjivanjeProfilaRequest.getPrezime());
        response.setDatumRodjenja(uredjivanjeProfilaRequest.getDatumRodjenja().toString());
        response.setAdresa(uredjivanjeProfilaRequest.getAdresa());
        response.setBrojTelefona(uredjivanjeProfilaRequest.getBrojTelefona());
        response.setEmail(uredjivanjeProfilaRequest.getEmail());
        response.setAkcija("PUT");

        if(k.get().getUloge().contains(doktor)){
            response.setUloga("DOKTOR");
        }
        else {
            response.setUloga("PACIJENT");
        }

        sendAsync(response);

        return new Response("Uspješno ste uredili profil!", 200);
    }

    public void sendAsync(AsyncResponse response) {
        JSONObject paket = new JSONObject();

        paket.put("id", response.getId());
        paket.put("ime", response.getIme());
        paket.put("prezime", response.getPrezime());
        paket.put("datumRodjenja", response.getDatumRodjenja());
        paket.put("adresa", response.getAdresa());
        paket.put("brojTelefona", response.getBrojTelefona());
        paket.put("email", response.getEmail());
        paket.put("uloga", response.getUloga());
        paket.put("akcija", response.getAkcija());

        String message = paket.toString();
        this.template.convertAndSend(korisnikQueue1.getName(), message);
        this.template.convertAndSend(korisnikQueue2.getName(), message);
        this.template.convertAndSend(korisnikQueue3.getName(), message);
        this.template.convertAndSend(korisnikQueue4.getName(), message);

        System.out.println("Sent: " + korisnikQueue1.getName() + message);
        System.out.println("Sent: " + korisnikQueue2.getName() + message);
        System.out.println("Sent: " + korisnikQueue3.getName() + message);
        System.out.println("Sent: " + korisnikQueue4.getName() + message);
    }
}
