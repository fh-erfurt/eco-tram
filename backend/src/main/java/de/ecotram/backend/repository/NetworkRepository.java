package de.ecotram.backend.repository;

import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.pagination.PaginationRequestHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Long>, PaginationRequestHelper<Network> {
}