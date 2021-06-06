package ba.unsa.etf.termini.configurations;

import org.springframework.amqp.core.Queue;
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
    public Queue terminiDodavanje() {
        return new Queue("terminiDodavanje");
    }

    @Bean
    public Queue terminiBrisanje() {
        return new Queue("terminiBrisanje");
    }

    @Bean
    public NoviKorisnikMessageReceiver receiver() {
        return new NoviKorisnikMessageReceiver();
    }
}
