package ba.unsa.etf.pregledi_i_kartoni.configurations;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitname}")
    String rabbitname;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitname);
        return connectionFactory;
    }

    @Bean
    public NoviKorisnikMessageReceiver receiver() {
        return new NoviKorisnikMessageReceiver();
    }

    @Bean
    public TerminiDodavanjeMessageReceiver terminiDodavanjeMessageReceiver() {
        return new TerminiDodavanjeMessageReceiver();
    }

    @Bean
    public TerminiBrisanjeMessageReceiver terminiBrisanjeMessageReceiver() {
        return new TerminiBrisanjeMessageReceiver();
    }
}