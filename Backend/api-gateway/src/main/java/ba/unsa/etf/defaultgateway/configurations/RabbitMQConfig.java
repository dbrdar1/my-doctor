package ba.unsa.etf.defaultgateway.configurations;

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
    public Queue queue1() {
        return new Queue("korisnikQueue1");
    }

    @Bean
    public Queue queue2() {
        return new Queue("korisnikQueue2");
    }

    @Bean
    public Queue queue3() {
        return new Queue("korisnikQueue3");
    }

    @Bean
    public Queue queue4() {
        return new Queue("korisnikQueue4");
    }
}
