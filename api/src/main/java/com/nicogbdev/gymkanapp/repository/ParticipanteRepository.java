package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.Participante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Participante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long>, JpaSpecificationExecutor<Participante> {

    @Override
    @EntityGraph("participante-entity-graph")
    Page<Participante> findAll(Specification<Participante> spec, Pageable pageable);
}
