package de.ecotram.backend;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendServerNewApplication {

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(9092);

        SocketIOServer socketIOServer = new SocketIOServer(configuration);
        socketIOServer.start();

        return socketIOServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendServerNewApplication.class, args);
    }
}