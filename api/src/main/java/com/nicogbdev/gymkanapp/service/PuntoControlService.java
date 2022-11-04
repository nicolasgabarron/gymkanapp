package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nicogbdev.gymkanapp.domain.PuntoControl}.
 */
public interface PuntoControlService {
    /**
     * Save a puntoControl.
     *
     * @param puntoControlDTO the entity to save.
     * @return the persisted entity.
     */
    PuntoControlDTO save(PuntoControlDTO puntoControlDTO);

    /**
     * Updates a puntoControl.
     *
     * @param puntoControlDTO the entity to update.
     * @return the persisted entity.
     */
    PuntoControlDTO update(PuntoControlDTO puntoControlDTO);

    /**
     * Partially updates a puntoControl.
     *
     * @param puntoControlDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PuntoControlDTO> partialUpdate(PuntoControlDTO puntoControlDTO);

    /**
     * Get all the puntoControls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PuntoControlDTO> findAll(Pageable pageable);

    /**
     * Get the "id" puntoControl.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PuntoControlDTO> findOne(Long id);

    /**
     * Delete the "id" puntoControl.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
