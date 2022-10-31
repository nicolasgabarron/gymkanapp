package com.nicogbdev.gymkanapp.repository;

import com.nicogbdev.gymkanapp.domain.Participante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Participante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long>, JpaSpecificationExecutor<Participante> {}
