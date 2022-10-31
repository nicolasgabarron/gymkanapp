package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.Voluntario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Voluntario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long>, JpaSpecificationExecutor<Voluntario> {}
