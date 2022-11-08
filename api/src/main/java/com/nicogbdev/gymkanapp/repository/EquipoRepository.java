package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Equipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long>, JpaSpecificationExecutor<Equipo> {
    @Override
    @EntityGraph("equipo-entity-graph")
    Page<Equipo> findAll(Specification<Equipo> spec, Pageable pageable);
}
