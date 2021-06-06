package ba.unsa.etf.defaultgateway.configurations;

import ba.unsa.etf.defaultgateway.security.CustomUserDetailsService;
import ba.unsa.etf.defaultgateway.security.JwtAuthenticationEntryPoint;
import ba.unsa.etf.defaultgateway.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final String[] unprotectedRoutes = {
            "/message",
            "/actuator/refresh",
            "/actuator/**",
            "/inicijalizacija-baze",
            "/registracija",
            "/prijava",
            "/korisnici",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs",
            "/uredjivanje_lozinke",
            "/verifikacijski-podaci",
            "/reset-token",
    };

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/swagger-config")
                .permitAll()
                .antMatchers(unprotectedRoutes)
                .permitAll()
                .antMatchers("/doktor-detalji/doktori").hasRole("PACIJENT")
                .antMatchers("/doktor-detalji/ocijeni-doktora").hasRole("PACIJENT")
                .antMatchers("/doktor-detalji/dodaj-certifikat").hasRole("DOKTOR")
                .antMatchers("/doktor-detalji/uredi-certifikat").hasRole("DOKTOR")
                .antMatchers("/doktor-detalji/obrisi-certifikat/{id}").hasRole("DOKTOR")
                .antMatchers("/doktor-detalji/dodaj-edukaciju").hasRole("DOKTOR")
                .antMatchers("/doktor-detalji/uredi-edukaciju").hasRole("DOKTOR")
                .antMatchers("/doktor-detalji/obrisi-edukaciju/{id}").hasRole("DOKTOR")
                .antMatchers("/doktor-detalji/uredi-biografiju-titulu").hasRole("DOKTOR")
                .antMatchers("/termini/dodaj-termin").hasRole("DOKTOR")
                .antMatchers("/termini/uredi-termin/{id}").hasRole("DOKTOR")
                .antMatchers("/termini/termini-pacijenta/{id}").hasRole("PACIJENT")
                .antMatchers("/termini/termini-doktora/{id}").hasRole("DOKTOR")
                .antMatchers("/termini/{id}").hasRole("PACIJENT")
                .antMatchers("/termini/{id}").hasRole("PACIJENT")
                .antMatchers("/pregledi-kartoni/pacijenti-doktora/{idDoktor}").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/pacijenti-doktora-filtrirano/{idDoktor}").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/kartoni-uloga-pacijent/{idPacijent}").hasRole("PACIJENT")
                .antMatchers("/pregledi-kartoni/kartoni-uloga-doktor/{idPacijent}").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/uredi-karton/{id}").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/obrisi-pregled/{idPregleda}").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/dodaj-pregled").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/dodaj-vezu-pacijent-doktor").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/obrisi-termin/{idTermina}").hasRole("DOKTOR")
                .antMatchers("/pregledi-kartoni/dodaj-termin").hasRole("DOKTOR")

                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}