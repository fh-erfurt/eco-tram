package de.ecotram.backend.repository;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.Station;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
}
