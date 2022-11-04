package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.PuntoControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PuntoControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PuntoControlRepository extends JpaRepository<PuntoControl, Long>, JpaSpecificationExecutor<PuntoControl> {

    @Override
    @EntityGraph("punto-control-entity-graph")
    Page<PuntoControl> findAll(Specification<PuntoControl> spec, Pageable pageable);
}
