package de.ecotram.backend.repository;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.pagination.PaginationRequestHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long>, PaginationRequestHelper<Connection> {

    @Query("select c from Connection c where c.sourceStation = ?1 and c.destinationStation = ?2")
    Connection findByLineAndTraversable(Station sourceStation, Station destinationStation);

}
