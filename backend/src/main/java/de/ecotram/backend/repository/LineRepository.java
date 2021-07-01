package de.ecotram.backend.repository;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.pagination.PaginationRequestHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends JpaRepository<Line, Long>, PaginationRequestHelper<Line> {}