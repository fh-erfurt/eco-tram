package de.ecotram.backend.repository;

import de.ecotram.backend.entity.LineEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineEntryRepository extends JpaRepository<LineEntry, Long> {
}