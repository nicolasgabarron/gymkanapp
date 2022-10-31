package com.nicogbdev.gymkanapp.web.rest;

import com.nicogbdev.gymkanapp.repository.VoluntarioRepository;
import com.nicogbdev.gymkanapp.service.VoluntarioQueryService;
import com.nicogbdev.gymkanapp.service.VoluntarioService;
import com.nicogbdev.gymkanapp.service.criteria.VoluntarioCriteria;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
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
 * REST controller for managing {@link com.nicogbdev.gymkanapp.domain.Voluntario}.
 */
@RestController
@RequestMapping("/api")
public class VoluntarioResource {

    private final Logger log = LoggerFactory.getLogger(VoluntarioResource.class);

    private static final String ENTITY_NAME = "voluntario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoluntarioService voluntarioService;

    private final VoluntarioRepository voluntarioRepository;

    private final VoluntarioQueryService voluntarioQueryService;

    public VoluntarioResource(
        VoluntarioService voluntarioService,
        VoluntarioRepository voluntarioRepository,
        VoluntarioQueryService voluntarioQueryService
    ) {
        this.voluntarioService = voluntarioService;
        this.voluntarioRepository = voluntarioRepository;
        this.voluntarioQueryService = voluntarioQueryService;
    }

    /**
     * {@code POST  /voluntarios} : Create a new voluntario.
     *
     * @param voluntarioDTO the voluntarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voluntarioDTO, or with status {@code 400 (Bad Request)} if the voluntario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voluntarios")
    public ResponseEntity<VoluntarioDTO> createVoluntario(@RequestBody VoluntarioDTO voluntarioDTO) throws URISyntaxException {
        log.debug("REST request to save Voluntario : {}", voluntarioDTO);
        if (voluntarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new voluntario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoluntarioDTO result = voluntarioService.save(voluntarioDTO);
        return ResponseEntity
            .created(new URI("/api/voluntarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voluntarios/:id} : Updates an existing voluntario.
     *
     * @param id the id of the voluntarioDTO to save.
     * @param voluntarioDTO the voluntarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voluntarioDTO,
     * or with status {@code 400 (Bad Request)} if the voluntarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voluntarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voluntarios/{id}")
    public ResponseEntity<VoluntarioDTO> updateVoluntario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoluntarioDTO voluntarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Voluntario : {}, {}", id, voluntarioDTO);
        if (voluntarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voluntarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voluntarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoluntarioDTO result = voluntarioService.update(voluntarioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voluntarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /voluntarios/:id} : Partial updates given fields of an existing voluntario, field will ignore if it is null
     *
     * @param id the id of the voluntarioDTO to save.
     * @param voluntarioDTO the voluntarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voluntarioDTO,
     * or with status {@code 400 (Bad Request)} if the voluntarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the voluntarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the voluntarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voluntarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoluntarioDTO> partialUpdateVoluntario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoluntarioDTO voluntarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Voluntario partially : {}, {}", id, voluntarioDTO);
        if (voluntarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voluntarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voluntarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoluntarioDTO> result = voluntarioService.partialUpdate(voluntarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voluntarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /voluntarios} : get all the voluntarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voluntarios in body.
     */
    @GetMapping("/voluntarios")
    public ResponseEntity<List<VoluntarioDTO>> getAllVoluntarios(
        VoluntarioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Voluntarios by criteria: {}", criteria);
        Page<VoluntarioDTO> page = voluntarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /voluntarios/count} : count all the voluntarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/voluntarios/count")
    public ResponseEntity<Long> countVoluntarios(VoluntarioCriteria criteria) {
        log.debug("REST request to count Voluntarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(voluntarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /voluntarios/:id} : get the "id" voluntario.
     *
     * @param id the id of the voluntarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voluntarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voluntarios/{id}")
    public ResponseEntity<VoluntarioDTO> getVoluntario(@PathVariable Long id) {
        log.debug("REST request to get Voluntario : {}", id);
        Optional<VoluntarioDTO> voluntarioDTO = voluntarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voluntarioDTO);
    }

    /**
     * {@code DELETE  /voluntarios/:id} : delete the "id" voluntario.
     *
     * @param id the id of the voluntarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voluntarios/{id}")
    public ResponseEntity<Void> deleteVoluntario(@PathVariable Long id) {
        log.debug("REST request to delete Voluntario : {}", id);
        voluntarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
