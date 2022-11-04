package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nicogbdev.gymkanapp.domain.Equipo}.
 */
public interface EquipoService {
    /**
     * Save a equipo.
     *
     * @param equipoDTO the entity to save.
     * @return the persisted entity.
     */
    EquipoDTO save(EquipoDTO equipoDTO);

    /**
     * Updates a equipo.
     *
     * @param equipoDTO the entity to update.
     * @return the persisted entity.
     */
    EquipoDTO update(EquipoDTO equipoDTO);

    /**
     * Partially updates a equipo.
     *
     * @param equipoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquipoDTO> partialUpdate(EquipoDTO equipoDTO);

    /**
     * Get all the equipos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EquipoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" equipo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquipoDTO> findOne(Long id);

    /**
     * Delete the "id" equipo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
