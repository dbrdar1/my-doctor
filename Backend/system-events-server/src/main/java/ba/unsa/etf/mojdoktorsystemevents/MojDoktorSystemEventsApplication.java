package ba.unsa.etf.mojdoktorsystemevents;

import ba.unsa.etf.mojdoktorsystemevents.grpc.server.SystemEventsServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MojDoktorSystemEventsApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(MojDoktorSystemEventsApplication.class, args);
        Server server = ServerBuilder.forPort(8866).addService(new SystemEventsServiceImpl()).build();
        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();
    }

}
