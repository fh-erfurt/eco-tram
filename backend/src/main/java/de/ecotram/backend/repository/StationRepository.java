package de.ecotram.backend.repository;

import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.pagination.PaginationRequestHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Long>, PaginationRequestHelper<Station> {
}