package de.ecotram.backend.repository;

import de.ecotram.backend.entity.network.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long>{
}
