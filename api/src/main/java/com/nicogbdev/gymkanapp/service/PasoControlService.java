package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.service.dto.PasoControlDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nicogbdev.gymkanapp.domain.PasoControl}.
 */
public interface PasoControlService {
    /**
     * Save a pasoControl.
     *
     * @param pasoControlDTO the entity to save.
     * @return the persisted entity.
     */
    PasoControlDTO save(PasoControlDTO pasoControlDTO);

    /**
     * Updates a pasoControl.
     *
     * @param pasoControlDTO the entity to update.
     * @return the persisted entity.
     */
    PasoControlDTO update(PasoControlDTO pasoControlDTO);

    /**
     * Partially updates a pasoControl.
     *
     * @param pasoControlDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PasoControlDTO> partialUpdate(PasoControlDTO pasoControlDTO);

    /**
     * Get all the pasoControls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PasoControlDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pasoControl.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PasoControlDTO> findOne(Long id);

    /**
     * Delete the "id" pasoControl.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
