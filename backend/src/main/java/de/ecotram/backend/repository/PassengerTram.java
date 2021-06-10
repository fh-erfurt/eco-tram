package de.ecotram.backend.repository;

import de.ecotram.backend.entity.network.PassengerTram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerTram extends JpaRepository<PassengerTram, Long> {
}
