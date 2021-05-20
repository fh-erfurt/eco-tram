package de.ecotram.backend.repository;

import de.ecotram.backend.entity.Line;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

@Slf4j
public class LineRepository extends BaseRepository<Line> {

    public LineRepository(EntityManager entityManager, Class<Line> type) {
        super(entityManager, type);
    }
}
