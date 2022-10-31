package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.Voluntario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Voluntario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long>, JpaSpecificationExecutor<Voluntario> {

    @Override
    @EntityGraph("voluntario-entity-graph")
    Page<Voluntario> findAll(Specification<Voluntario> spec, Pageable pageable);
}
