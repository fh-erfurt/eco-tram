package de.ecotram.backend.repository;

import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.pagination.PaginationRequestHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerTramRepository extends JpaRepository<PassengerTram, Long>, PaginationRequestHelper<PassengerTram> {
}
