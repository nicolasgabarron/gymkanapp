package com.nicogbdev.gymkanapp.web.rest;

import com.nicogbdev.gymkanapp.repository.PuntoControlRepository;
import com.nicogbdev.gymkanapp.service.PuntoControlQueryService;
import com.nicogbdev.gymkanapp.service.PuntoControlService;
import com.nicogbdev.gymkanapp.service.criteria.PuntoControlCriteria;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import com.nicogbdev.gymkanapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nicogbdev.gymkanapp.domain.PuntoControl}.
 */
@RestController
@RequestMapping("/api")
public class PuntoControlResource {

    private final Logger log = LoggerFactory.getLogger(PuntoControlResource.class);

    private static final String ENTITY_NAME = "puntoControl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PuntoControlService puntoControlService;

    private final PuntoControlRepository puntoControlRepository;

    private final PuntoControlQueryService puntoControlQueryService;

    public PuntoControlResource(
        PuntoControlService puntoControlService,
        PuntoControlRepository puntoControlRepository,
        PuntoControlQueryService puntoControlQueryService
    ) {
        this.puntoControlService = puntoControlService;
        this.puntoControlRepository = puntoControlRepository;
        this.puntoControlQueryService = puntoControlQueryService;
    }

    /**
     * {@code POST  /punto-controls} : Create a new puntoControl.
     *
     * @param puntoControlDTO the puntoControlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new puntoControlDTO, or with status {@code 400 (Bad Request)} if the puntoControl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punto-controls")
    public ResponseEntity<PuntoControlDTO> createPuntoControl(@RequestBody PuntoControlDTO puntoControlDTO) throws URISyntaxException {
        log.debug("REST request to save PuntoControl : {}", puntoControlDTO);
        if (puntoControlDTO.getId() != null) {
            throw new BadRequestAlertException("A new puntoControl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PuntoControlDTO result = puntoControlService.save(puntoControlDTO);
        return ResponseEntity
            .created(new URI("/api/punto-controls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punto-controls/:id} : Updates an existing puntoControl.
     *
     * @param id the id of the puntoControlDTO to save.
     * @param puntoControlDTO the puntoControlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated puntoControlDTO,
     * or with status {@code 400 (Bad Request)} if the puntoControlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the puntoControlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punto-controls/{id}")
    public ResponseEntity<PuntoControlDTO> updatePuntoControl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PuntoControlDTO puntoControlDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PuntoControl : {}, {}", id, puntoControlDTO);
        if (puntoControlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, puntoControlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!puntoControlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PuntoControlDTO result = puntoControlService.update(puntoControlDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, puntoControlDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /punto-controls/:id} : Partial updates given fields of an existing puntoControl, field will ignore if it is null
     *
     * @param id the id of the puntoControlDTO to save.
     * @param puntoControlDTO the puntoControlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated puntoControlDTO,
     * or with status {@code 400 (Bad Request)} if the puntoControlDTO is not valid,
     * or with status {@code 404 (Not Found)} if the puntoControlDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the puntoControlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/punto-controls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PuntoControlDTO> partialUpdatePuntoControl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PuntoControlDTO puntoControlDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PuntoControl partially : {}, {}", id, puntoControlDTO);
        if (puntoControlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, puntoControlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!puntoControlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PuntoControlDTO> result = puntoControlService.partialUpdate(puntoControlDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, puntoControlDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /punto-controls} : get all the puntoControls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of puntoControls in body.
     */
    @GetMapping("/punto-controls")
    public ResponseEntity<List<PuntoControlDTO>> getAllPuntoControls(
        PuntoControlCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PuntoControls by criteria: {}", criteria);
        Page<PuntoControlDTO> page = puntoControlQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /punto-controls/count} : count all the puntoControls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/punto-controls/count")
    public ResponseEntity<Long> countPuntoControls(PuntoControlCriteria criteria) {
        log.debug("REST request to count PuntoControls by criteria: {}", criteria);
        return ResponseEntity.ok().body(puntoControlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /punto-controls/:id} : get the "id" puntoControl.
     *
     * @param id the id of the puntoControlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the puntoControlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punto-controls/{id}")
    public ResponseEntity<PuntoControlDTO> getPuntoControl(@PathVariable Long id) {
        log.debug("REST request to get PuntoControl : {}", id);
        Optional<PuntoControlDTO> puntoControlDTO = puntoControlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(puntoControlDTO);
    }

    /**
     * {@code DELETE  /punto-controls/:id} : delete the "id" puntoControl.
     *
     * @param id the id of the puntoControlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punto-controls/{id}")
    public ResponseEntity<Void> deletePuntoControl(@PathVariable Long id) {
        log.debug("REST request to delete PuntoControl : {}", id);
        puntoControlService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
