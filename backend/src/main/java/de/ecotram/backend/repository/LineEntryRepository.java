package de.ecotram.backend.repository;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.network.Traversable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LineEntryRepository extends JpaRepository<LineEntry, Long> {

    @Query("select l from LineEntry l where l.line = ?1 and l.traversable = ?2")
    LineEntry findByLineAndTraversable(Line line, Traversable traversable);

}