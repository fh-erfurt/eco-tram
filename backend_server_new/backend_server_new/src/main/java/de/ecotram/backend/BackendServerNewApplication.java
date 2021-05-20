package de.ecotram.backend;

import de.ecotram.backend.entity.Line;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@SpringBootApplication
public class BackendServerNewApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendServerNewApplication.class, args);

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ecotram");
        EntityManager entityManager = factory.createEntityManager();

        Line line = new Line();
        line.setName("Testlinie");

        Line line2 = new Line();
        line2.setName("Testlinie2");

        entityManager.getTransaction().begin();
        entityManager.persist(line);
        entityManager.persist(line2);
        entityManager.getTransaction().commit();

        System.out.println(line.getId());
        System.out.println(line2.getId());
    }

}
