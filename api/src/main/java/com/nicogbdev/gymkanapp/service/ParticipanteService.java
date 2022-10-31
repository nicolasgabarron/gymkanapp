package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.service.dto.ParticipanteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nicogbdev.gymkanapp.domain.Participante}.
 */
public interface ParticipanteService {
    /**
     * Save a participante.
     *
     * @param participanteDTO the entity to save.
     * @return the persisted entity.
     */
    ParticipanteDTO save(ParticipanteDTO participanteDTO);

    /**
     * Updates a participante.
     *
     * @param participanteDTO the entity to update.
     * @return the persisted entity.
     */
    ParticipanteDTO update(ParticipanteDTO participanteDTO);

    /**
     * Partially updates a participante.
     *
     * @param participanteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ParticipanteDTO> partialUpdate(ParticipanteDTO participanteDTO);

    /**
     * Get all the participantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticipanteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" participante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticipanteDTO> findOne(Long id);

    /**
     * Delete the "id" participante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
