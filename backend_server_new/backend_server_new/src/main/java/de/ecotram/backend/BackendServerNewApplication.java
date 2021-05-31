package de.ecotram.backend;

import de.ecotram.backend.entity.Line;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

@SpringBootApplication
public class BackendServerNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendServerNewApplication.class, args);
    }

}
