package com.nicogbdev.gymkanapp.web.rest;

import com.nicogbdev.gymkanapp.repository.PasoControlRepository;
import com.nicogbdev.gymkanapp.service.PasoControlQueryService;
import com.nicogbdev.gymkanapp.service.PasoControlService;
import com.nicogbdev.gymkanapp.service.criteria.PasoControlCriteria;
import com.nicogbdev.gymkanapp.service.dto.PasoControlDTO;
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
 * REST controller for managing {@link com.nicogbdev.gymkanapp.domain.PasoControl}.
 */
@RestController
@RequestMapping("/api")
public class PasoControlResource {

    private final Logger log = LoggerFactory.getLogger(PasoControlResource.class);

    private static final String ENTITY_NAME = "pasoControl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasoControlService pasoControlService;

    private final PasoControlRepository pasoControlRepository;

    private final PasoControlQueryService pasoControlQueryService;

    public PasoControlResource(
        PasoControlService pasoControlService,
        PasoControlRepository pasoControlRepository,
        PasoControlQueryService pasoControlQueryService
    ) {
        this.pasoControlService = pasoControlService;
        this.pasoControlRepository = pasoControlRepository;
        this.pasoControlQueryService = pasoControlQueryService;
    }

    /**
     * {@code POST  /paso-controls} : Create a new pasoControl.
     *
     * @param pasoControlDTO the pasoControlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pasoControlDTO, or with status {@code 400 (Bad Request)} if the pasoControl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paso-controls")
    public ResponseEntity<PasoControlDTO> createPasoControl(@RequestBody PasoControlDTO pasoControlDTO) throws URISyntaxException {
        log.debug("REST request to save PasoControl : {}", pasoControlDTO);
        if (pasoControlDTO.getId() != null) {
            throw new BadRequestAlertException("A new pasoControl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PasoControlDTO result = pasoControlService.save(pasoControlDTO);
        return ResponseEntity
            .created(new URI("/api/paso-controls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paso-controls/:id} : Updates an existing pasoControl.
     *
     * @param id the id of the pasoControlDTO to save.
     * @param pasoControlDTO the pasoControlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasoControlDTO,
     * or with status {@code 400 (Bad Request)} if the pasoControlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pasoControlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paso-controls/{id}")
    public ResponseEntity<PasoControlDTO> updatePasoControl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PasoControlDTO pasoControlDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PasoControl : {}, {}", id, pasoControlDTO);
        if (pasoControlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasoControlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasoControlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PasoControlDTO result = pasoControlService.update(pasoControlDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasoControlDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paso-controls/:id} : Partial updates given fields of an existing pasoControl, field will ignore if it is null
     *
     * @param id the id of the pasoControlDTO to save.
     * @param pasoControlDTO the pasoControlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasoControlDTO,
     * or with status {@code 400 (Bad Request)} if the pasoControlDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pasoControlDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pasoControlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paso-controls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PasoControlDTO> partialUpdatePasoControl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PasoControlDTO pasoControlDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PasoControl partially : {}, {}", id, pasoControlDTO);
        if (pasoControlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasoControlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasoControlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PasoControlDTO> result = pasoControlService.partialUpdate(pasoControlDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasoControlDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paso-controls} : get all the pasoControls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pasoControls in body.
     */
    @GetMapping("/paso-controls")
    public ResponseEntity<List<PasoControlDTO>> getAllPasoControls(
        PasoControlCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PasoControls by criteria: {}", criteria);
        Page<PasoControlDTO> page = pasoControlQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paso-controls/count} : count all the pasoControls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/paso-controls/count")
    public ResponseEntity<Long> countPasoControls(PasoControlCriteria criteria) {
        log.debug("REST request to count PasoControls by criteria: {}", criteria);
        return ResponseEntity.ok().body(pasoControlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /paso-controls/:id} : get the "id" pasoControl.
     *
     * @param id the id of the pasoControlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pasoControlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paso-controls/{id}")
    public ResponseEntity<PasoControlDTO> getPasoControl(@PathVariable Long id) {
        log.debug("REST request to get PasoControl : {}", id);
        Optional<PasoControlDTO> pasoControlDTO = pasoControlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pasoControlDTO);
    }

    /**
     * {@code DELETE  /paso-controls/:id} : delete the "id" pasoControl.
     *
     * @param id the id of the pasoControlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paso-controls/{id}")
    public ResponseEntity<Void> deletePasoControl(@PathVariable Long id) {
        log.debug("REST request to delete PasoControl : {}", id);
        pasoControlService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
