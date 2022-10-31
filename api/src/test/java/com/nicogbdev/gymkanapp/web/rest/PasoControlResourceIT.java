package com.nicogbdev.gymkanapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nicogbdev.gymkanapp.IntegrationTest;
import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.domain.PasoControl;
import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.repository.PasoControlRepository;
import com.nicogbdev.gymkanapp.service.criteria.PasoControlCriteria;
import com.nicogbdev.gymkanapp.service.dto.PasoControlDTO;
import com.nicogbdev.gymkanapp.service.mapper.PasoControlMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PasoControlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PasoControlResourceIT {

    private static final Instant DEFAULT_FECHA_HORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_HORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/paso-controls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PasoControlRepository pasoControlRepository;

    @Autowired
    private PasoControlMapper pasoControlMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPasoControlMockMvc;

    private PasoControl pasoControl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasoControl createEntity(EntityManager em) {
        PasoControl pasoControl = new PasoControl().fechaHora(DEFAULT_FECHA_HORA);
        return pasoControl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasoControl createUpdatedEntity(EntityManager em) {
        PasoControl pasoControl = new PasoControl().fechaHora(UPDATED_FECHA_HORA);
        return pasoControl;
    }

    @BeforeEach
    public void initTest() {
        pasoControl = createEntity(em);
    }

    @Test
    @Transactional
    void createPasoControl() throws Exception {
        int databaseSizeBeforeCreate = pasoControlRepository.findAll().size();
        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);
        restPasoControlMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeCreate + 1);
        PasoControl testPasoControl = pasoControlList.get(pasoControlList.size() - 1);
        assertThat(testPasoControl.getFechaHora()).isEqualTo(DEFAULT_FECHA_HORA);
    }

    @Test
    @Transactional
    void createPasoControlWithExistingId() throws Exception {
        // Create the PasoControl with an existing ID
        pasoControl.setId(1L);
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        int databaseSizeBeforeCreate = pasoControlRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPasoControlMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPasoControls() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        // Get all the pasoControlList
        restPasoControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasoControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA.toString())));
    }

    @Test
    @Transactional
    void getPasoControl() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        // Get the pasoControl
        restPasoControlMockMvc
            .perform(get(ENTITY_API_URL_ID, pasoControl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pasoControl.getId().intValue()))
            .andExpect(jsonPath("$.fechaHora").value(DEFAULT_FECHA_HORA.toString()));
    }

    @Test
    @Transactional
    void getPasoControlsByIdFiltering() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        Long id = pasoControl.getId();

        defaultPasoControlShouldBeFound("id.equals=" + id);
        defaultPasoControlShouldNotBeFound("id.notEquals=" + id);

        defaultPasoControlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPasoControlShouldNotBeFound("id.greaterThan=" + id);

        defaultPasoControlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPasoControlShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPasoControlsByFechaHoraIsEqualToSomething() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        // Get all the pasoControlList where fechaHora equals to DEFAULT_FECHA_HORA
        defaultPasoControlShouldBeFound("fechaHora.equals=" + DEFAULT_FECHA_HORA);

        // Get all the pasoControlList where fechaHora equals to UPDATED_FECHA_HORA
        defaultPasoControlShouldNotBeFound("fechaHora.equals=" + UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void getAllPasoControlsByFechaHoraIsInShouldWork() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        // Get all the pasoControlList where fechaHora in DEFAULT_FECHA_HORA or UPDATED_FECHA_HORA
        defaultPasoControlShouldBeFound("fechaHora.in=" + DEFAULT_FECHA_HORA + "," + UPDATED_FECHA_HORA);

        // Get all the pasoControlList where fechaHora equals to UPDATED_FECHA_HORA
        defaultPasoControlShouldNotBeFound("fechaHora.in=" + UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void getAllPasoControlsByFechaHoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        // Get all the pasoControlList where fechaHora is not null
        defaultPasoControlShouldBeFound("fechaHora.specified=true");

        // Get all the pasoControlList where fechaHora is null
        defaultPasoControlShouldNotBeFound("fechaHora.specified=false");
    }

    @Test
    @Transactional
    void getAllPasoControlsByEquipoIsEqualToSomething() throws Exception {
        Equipo equipo;
        if (TestUtil.findAll(em, Equipo.class).isEmpty()) {
            pasoControlRepository.saveAndFlush(pasoControl);
            equipo = EquipoResourceIT.createEntity(em);
        } else {
            equipo = TestUtil.findAll(em, Equipo.class).get(0);
        }
        em.persist(equipo);
        em.flush();
        pasoControl.setEquipo(equipo);
        pasoControlRepository.saveAndFlush(pasoControl);
        Long equipoId = equipo.getId();

        // Get all the pasoControlList where equipo equals to equipoId
        defaultPasoControlShouldBeFound("equipoId.equals=" + equipoId);

        // Get all the pasoControlList where equipo equals to (equipoId + 1)
        defaultPasoControlShouldNotBeFound("equipoId.equals=" + (equipoId + 1));
    }

    @Test
    @Transactional
    void getAllPasoControlsByPuntoControlIsEqualToSomething() throws Exception {
        PuntoControl puntoControl;
        if (TestUtil.findAll(em, PuntoControl.class).isEmpty()) {
            pasoControlRepository.saveAndFlush(pasoControl);
            puntoControl = PuntoControlResourceIT.createEntity(em);
        } else {
            puntoControl = TestUtil.findAll(em, PuntoControl.class).get(0);
        }
        em.persist(puntoControl);
        em.flush();
        pasoControl.setPuntoControl(puntoControl);
        pasoControlRepository.saveAndFlush(pasoControl);
        Long puntoControlId = puntoControl.getId();

        // Get all the pasoControlList where puntoControl equals to puntoControlId
        defaultPasoControlShouldBeFound("puntoControlId.equals=" + puntoControlId);

        // Get all the pasoControlList where puntoControl equals to (puntoControlId + 1)
        defaultPasoControlShouldNotBeFound("puntoControlId.equals=" + (puntoControlId + 1));
    }

    @Test
    @Transactional
    void getAllPasoControlsByValidadoPorIsEqualToSomething() throws Exception {
        Voluntario validadoPor;
        if (TestUtil.findAll(em, Voluntario.class).isEmpty()) {
            pasoControlRepository.saveAndFlush(pasoControl);
            validadoPor = VoluntarioResourceIT.createEntity(em);
        } else {
            validadoPor = TestUtil.findAll(em, Voluntario.class).get(0);
        }
        em.persist(validadoPor);
        em.flush();
        pasoControl.setValidadoPor(validadoPor);
        pasoControlRepository.saveAndFlush(pasoControl);
        Long validadoPorId = validadoPor.getId();

        // Get all the pasoControlList where validadoPor equals to validadoPorId
        defaultPasoControlShouldBeFound("validadoPorId.equals=" + validadoPorId);

        // Get all the pasoControlList where validadoPor equals to (validadoPorId + 1)
        defaultPasoControlShouldNotBeFound("validadoPorId.equals=" + (validadoPorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPasoControlShouldBeFound(String filter) throws Exception {
        restPasoControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasoControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA.toString())));

        // Check, that the count call also returns 1
        restPasoControlMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPasoControlShouldNotBeFound(String filter) throws Exception {
        restPasoControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPasoControlMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPasoControl() throws Exception {
        // Get the pasoControl
        restPasoControlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPasoControl() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();

        // Update the pasoControl
        PasoControl updatedPasoControl = pasoControlRepository.findById(pasoControl.getId()).get();
        // Disconnect from session so that the updates on updatedPasoControl are not directly saved in db
        em.detach(updatedPasoControl);
        updatedPasoControl.fechaHora(UPDATED_FECHA_HORA);
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(updatedPasoControl);

        restPasoControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pasoControlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isOk());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
        PasoControl testPasoControl = pasoControlList.get(pasoControlList.size() - 1);
        assertThat(testPasoControl.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void putNonExistingPasoControl() throws Exception {
        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();
        pasoControl.setId(count.incrementAndGet());

        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasoControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pasoControlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPasoControl() throws Exception {
        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();
        pasoControl.setId(count.incrementAndGet());

        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPasoControl() throws Exception {
        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();
        pasoControl.setId(count.incrementAndGet());

        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoControlMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pasoControlDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePasoControlWithPatch() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();

        // Update the pasoControl using partial update
        PasoControl partialUpdatedPasoControl = new PasoControl();
        partialUpdatedPasoControl.setId(pasoControl.getId());

        partialUpdatedPasoControl.fechaHora(UPDATED_FECHA_HORA);

        restPasoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasoControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPasoControl))
            )
            .andExpect(status().isOk());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
        PasoControl testPasoControl = pasoControlList.get(pasoControlList.size() - 1);
        assertThat(testPasoControl.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void fullUpdatePasoControlWithPatch() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();

        // Update the pasoControl using partial update
        PasoControl partialUpdatedPasoControl = new PasoControl();
        partialUpdatedPasoControl.setId(pasoControl.getId());

        partialUpdatedPasoControl.fechaHora(UPDATED_FECHA_HORA);

        restPasoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasoControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPasoControl))
            )
            .andExpect(status().isOk());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
        PasoControl testPasoControl = pasoControlList.get(pasoControlList.size() - 1);
        assertThat(testPasoControl.getFechaHora()).isEqualTo(UPDATED_FECHA_HORA);
    }

    @Test
    @Transactional
    void patchNonExistingPasoControl() throws Exception {
        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();
        pasoControl.setId(count.incrementAndGet());

        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pasoControlDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPasoControl() throws Exception {
        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();
        pasoControl.setId(count.incrementAndGet());

        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPasoControl() throws Exception {
        int databaseSizeBeforeUpdate = pasoControlRepository.findAll().size();
        pasoControl.setId(count.incrementAndGet());

        // Create the PasoControl
        PasoControlDTO pasoControlDTO = pasoControlMapper.toDto(pasoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoControlMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pasoControlDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PasoControl in the database
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePasoControl() throws Exception {
        // Initialize the database
        pasoControlRepository.saveAndFlush(pasoControl);

        int databaseSizeBeforeDelete = pasoControlRepository.findAll().size();

        // Delete the pasoControl
        restPasoControlMockMvc
            .perform(delete(ENTITY_API_URL_ID, pasoControl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PasoControl> pasoControlList = pasoControlRepository.findAll();
        assertThat(pasoControlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
