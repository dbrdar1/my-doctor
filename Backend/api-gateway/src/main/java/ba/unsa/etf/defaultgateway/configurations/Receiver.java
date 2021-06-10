package ba.unsa.etf.defaultgateway.configurations;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "fiktivni")
public class Receiver {

    @RabbitHandler
    public void receive(String receivedMessage) {
        System.out.println("Received: " + receivedMessage);
    }
}

