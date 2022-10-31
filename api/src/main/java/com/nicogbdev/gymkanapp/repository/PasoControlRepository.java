package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.PasoControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PasoControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasoControlRepository extends JpaRepository<PasoControl, Long>, JpaSpecificationExecutor<PasoControl> {

    @Override
    @EntityGraph("paso-control-entity-graph")
    Page<PasoControl> findAll(Specification<PasoControl> spec, Pageable pageable);
}
