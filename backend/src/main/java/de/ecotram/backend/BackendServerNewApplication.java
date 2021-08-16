package de.ecotram.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: add sql script + update documentation to include in installation steps
// TODO(erik): unit tests

@SpringBootApplication
public class BackendServerNewApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendServerNewApplication.class, args);
    }
}