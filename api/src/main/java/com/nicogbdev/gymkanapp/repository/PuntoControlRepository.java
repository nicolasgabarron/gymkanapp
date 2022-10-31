package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.PuntoControl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PuntoControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PuntoControlRepository extends JpaRepository<PuntoControl, Long>, JpaSpecificationExecutor<PuntoControl> {}
