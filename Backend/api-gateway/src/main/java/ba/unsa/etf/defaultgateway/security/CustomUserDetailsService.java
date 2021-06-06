package ba.unsa.etf.defaultgateway.security;

import ba.unsa.etf.defaultgateway.models.Korisnik;
import ba.unsa.etf.defaultgateway.repositories.KorisnikRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final KorisnikRepository korisnikRepository;

    public CustomUserDetailsService(final KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Korisnik user = korisnikRepository.findByKorisnickoIme(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username : " + username)
                );

        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        Korisnik user = korisnikRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}
