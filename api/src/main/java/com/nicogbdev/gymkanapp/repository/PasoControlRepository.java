package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.PasoControl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PasoControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasoControlRepository extends JpaRepository<PasoControl, Long>, JpaSpecificationExecutor<PasoControl> {}
