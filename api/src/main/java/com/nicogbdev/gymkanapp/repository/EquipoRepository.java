package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.Equipo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Equipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long>, JpaSpecificationExecutor<Equipo> {}
