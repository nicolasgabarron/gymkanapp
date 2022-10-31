package com.nicogbdev.gymkanapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nicogbdev.gymkanapp.IntegrationTest;
import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.repository.PuntoControlRepository;
import com.nicogbdev.gymkanapp.service.criteria.PuntoControlCriteria;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import com.nicogbdev.gymkanapp.service.mapper.PuntoControlMapper;
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
 * Integration tests for the {@link PuntoControlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PuntoControlResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;
    private static final Integer SMALLER_ORDEN = 1 - 1;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/punto-controls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PuntoControlRepository puntoControlRepository;

    @Autowired
    private PuntoControlMapper puntoControlMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPuntoControlMockMvc;

    private PuntoControl puntoControl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoControl createEntity(EntityManager em) {
        PuntoControl puntoControl = new PuntoControl()
            .identificador(DEFAULT_IDENTIFICADOR)
            .orden(DEFAULT_ORDEN)
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION);
        return puntoControl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoControl createUpdatedEntity(EntityManager em) {
        PuntoControl puntoControl = new PuntoControl()
            .identificador(UPDATED_IDENTIFICADOR)
            .orden(UPDATED_ORDEN)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION);
        return puntoControl;
    }

    @BeforeEach
    public void initTest() {
        puntoControl = createEntity(em);
    }

    @Test
    @Transactional
    void createPuntoControl() throws Exception {
        int databaseSizeBeforeCreate = puntoControlRepository.findAll().size();
        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);
        restPuntoControlMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeCreate + 1);
        PuntoControl testPuntoControl = puntoControlList.get(puntoControlList.size() - 1);
        assertThat(testPuntoControl.getIdentificador()).isEqualTo(DEFAULT_IDENTIFICADOR);
        assertThat(testPuntoControl.getOrden()).isEqualTo(DEFAULT_ORDEN);
        assertThat(testPuntoControl.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPuntoControl.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void createPuntoControlWithExistingId() throws Exception {
        // Create the PuntoControl with an existing ID
        puntoControl.setId(1L);
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        int databaseSizeBeforeCreate = puntoControlRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPuntoControlMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPuntoControls() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList
        restPuntoControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(puntoControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));
    }

    @Test
    @Transactional
    void getPuntoControl() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get the puntoControl
        restPuntoControlMockMvc
            .perform(get(ENTITY_API_URL_ID, puntoControl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(puntoControl.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION));
    }

    @Test
    @Transactional
    void getPuntoControlsByIdFiltering() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        Long id = puntoControl.getId();

        defaultPuntoControlShouldBeFound("id.equals=" + id);
        defaultPuntoControlShouldNotBeFound("id.notEquals=" + id);

        defaultPuntoControlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPuntoControlShouldNotBeFound("id.greaterThan=" + id);

        defaultPuntoControlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPuntoControlShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByIdentificadorIsEqualToSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where identificador equals to DEFAULT_IDENTIFICADOR
        defaultPuntoControlShouldBeFound("identificador.equals=" + DEFAULT_IDENTIFICADOR);

        // Get all the puntoControlList where identificador equals to UPDATED_IDENTIFICADOR
        defaultPuntoControlShouldNotBeFound("identificador.equals=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByIdentificadorIsInShouldWork() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where identificador in DEFAULT_IDENTIFICADOR or UPDATED_IDENTIFICADOR
        defaultPuntoControlShouldBeFound("identificador.in=" + DEFAULT_IDENTIFICADOR + "," + UPDATED_IDENTIFICADOR);

        // Get all the puntoControlList where identificador equals to UPDATED_IDENTIFICADOR
        defaultPuntoControlShouldNotBeFound("identificador.in=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByIdentificadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where identificador is not null
        defaultPuntoControlShouldBeFound("identificador.specified=true");

        // Get all the puntoControlList where identificador is null
        defaultPuntoControlShouldNotBeFound("identificador.specified=false");
    }

    @Test
    @Transactional
    void getAllPuntoControlsByIdentificadorContainsSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where identificador contains DEFAULT_IDENTIFICADOR
        defaultPuntoControlShouldBeFound("identificador.contains=" + DEFAULT_IDENTIFICADOR);

        // Get all the puntoControlList where identificador contains UPDATED_IDENTIFICADOR
        defaultPuntoControlShouldNotBeFound("identificador.contains=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByIdentificadorNotContainsSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where identificador does not contain DEFAULT_IDENTIFICADOR
        defaultPuntoControlShouldNotBeFound("identificador.doesNotContain=" + DEFAULT_IDENTIFICADOR);

        // Get all the puntoControlList where identificador does not contain UPDATED_IDENTIFICADOR
        defaultPuntoControlShouldBeFound("identificador.doesNotContain=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsEqualToSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden equals to DEFAULT_ORDEN
        defaultPuntoControlShouldBeFound("orden.equals=" + DEFAULT_ORDEN);

        // Get all the puntoControlList where orden equals to UPDATED_ORDEN
        defaultPuntoControlShouldNotBeFound("orden.equals=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsInShouldWork() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden in DEFAULT_ORDEN or UPDATED_ORDEN
        defaultPuntoControlShouldBeFound("orden.in=" + DEFAULT_ORDEN + "," + UPDATED_ORDEN);

        // Get all the puntoControlList where orden equals to UPDATED_ORDEN
        defaultPuntoControlShouldNotBeFound("orden.in=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsNullOrNotNull() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden is not null
        defaultPuntoControlShouldBeFound("orden.specified=true");

        // Get all the puntoControlList where orden is null
        defaultPuntoControlShouldNotBeFound("orden.specified=false");
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden is greater than or equal to DEFAULT_ORDEN
        defaultPuntoControlShouldBeFound("orden.greaterThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the puntoControlList where orden is greater than or equal to UPDATED_ORDEN
        defaultPuntoControlShouldNotBeFound("orden.greaterThanOrEqual=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden is less than or equal to DEFAULT_ORDEN
        defaultPuntoControlShouldBeFound("orden.lessThanOrEqual=" + DEFAULT_ORDEN);

        // Get all the puntoControlList where orden is less than or equal to SMALLER_ORDEN
        defaultPuntoControlShouldNotBeFound("orden.lessThanOrEqual=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsLessThanSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden is less than DEFAULT_ORDEN
        defaultPuntoControlShouldNotBeFound("orden.lessThan=" + DEFAULT_ORDEN);

        // Get all the puntoControlList where orden is less than UPDATED_ORDEN
        defaultPuntoControlShouldBeFound("orden.lessThan=" + UPDATED_ORDEN);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByOrdenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where orden is greater than DEFAULT_ORDEN
        defaultPuntoControlShouldNotBeFound("orden.greaterThan=" + DEFAULT_ORDEN);

        // Get all the puntoControlList where orden is greater than SMALLER_ORDEN
        defaultPuntoControlShouldBeFound("orden.greaterThan=" + SMALLER_ORDEN);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where nombre equals to DEFAULT_NOMBRE
        defaultPuntoControlShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the puntoControlList where nombre equals to UPDATED_NOMBRE
        defaultPuntoControlShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPuntoControlShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the puntoControlList where nombre equals to UPDATED_NOMBRE
        defaultPuntoControlShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where nombre is not null
        defaultPuntoControlShouldBeFound("nombre.specified=true");

        // Get all the puntoControlList where nombre is null
        defaultPuntoControlShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPuntoControlsByNombreContainsSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where nombre contains DEFAULT_NOMBRE
        defaultPuntoControlShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the puntoControlList where nombre contains UPDATED_NOMBRE
        defaultPuntoControlShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where nombre does not contain DEFAULT_NOMBRE
        defaultPuntoControlShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the puntoControlList where nombre does not contain UPDATED_NOMBRE
        defaultPuntoControlShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where direccion equals to DEFAULT_DIRECCION
        defaultPuntoControlShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the puntoControlList where direccion equals to UPDATED_DIRECCION
        defaultPuntoControlShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultPuntoControlShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the puntoControlList where direccion equals to UPDATED_DIRECCION
        defaultPuntoControlShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where direccion is not null
        defaultPuntoControlShouldBeFound("direccion.specified=true");

        // Get all the puntoControlList where direccion is null
        defaultPuntoControlShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllPuntoControlsByDireccionContainsSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where direccion contains DEFAULT_DIRECCION
        defaultPuntoControlShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the puntoControlList where direccion contains UPDATED_DIRECCION
        defaultPuntoControlShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        // Get all the puntoControlList where direccion does not contain DEFAULT_DIRECCION
        defaultPuntoControlShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the puntoControlList where direccion does not contain UPDATED_DIRECCION
        defaultPuntoControlShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllPuntoControlsByVoluntariosIsEqualToSomething() throws Exception {
        Voluntario voluntarios;
        if (TestUtil.findAll(em, Voluntario.class).isEmpty()) {
            puntoControlRepository.saveAndFlush(puntoControl);
            voluntarios = VoluntarioResourceIT.createEntity(em);
        } else {
            voluntarios = TestUtil.findAll(em, Voluntario.class).get(0);
        }
        em.persist(voluntarios);
        em.flush();
        puntoControl.addVoluntarios(voluntarios);
        puntoControlRepository.saveAndFlush(puntoControl);
        Long voluntariosId = voluntarios.getId();

        // Get all the puntoControlList where voluntarios equals to voluntariosId
        defaultPuntoControlShouldBeFound("voluntariosId.equals=" + voluntariosId);

        // Get all the puntoControlList where voluntarios equals to (voluntariosId + 1)
        defaultPuntoControlShouldNotBeFound("voluntariosId.equals=" + (voluntariosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPuntoControlShouldBeFound(String filter) throws Exception {
        restPuntoControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(puntoControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));

        // Check, that the count call also returns 1
        restPuntoControlMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPuntoControlShouldNotBeFound(String filter) throws Exception {
        restPuntoControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPuntoControlMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPuntoControl() throws Exception {
        // Get the puntoControl
        restPuntoControlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPuntoControl() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();

        // Update the puntoControl
        PuntoControl updatedPuntoControl = puntoControlRepository.findById(puntoControl.getId()).get();
        // Disconnect from session so that the updates on updatedPuntoControl are not directly saved in db
        em.detach(updatedPuntoControl);
        updatedPuntoControl.identificador(UPDATED_IDENTIFICADOR).orden(UPDATED_ORDEN).nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION);
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(updatedPuntoControl);

        restPuntoControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, puntoControlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isOk());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
        PuntoControl testPuntoControl = puntoControlList.get(puntoControlList.size() - 1);
        assertThat(testPuntoControl.getIdentificador()).isEqualTo(UPDATED_IDENTIFICADOR);
        assertThat(testPuntoControl.getOrden()).isEqualTo(UPDATED_ORDEN);
        assertThat(testPuntoControl.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPuntoControl.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void putNonExistingPuntoControl() throws Exception {
        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();
        puntoControl.setId(count.incrementAndGet());

        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPuntoControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, puntoControlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPuntoControl() throws Exception {
        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();
        puntoControl.setId(count.incrementAndGet());

        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPuntoControl() throws Exception {
        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();
        puntoControl.setId(count.incrementAndGet());

        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoControlMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePuntoControlWithPatch() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();

        // Update the puntoControl using partial update
        PuntoControl partialUpdatedPuntoControl = new PuntoControl();
        partialUpdatedPuntoControl.setId(puntoControl.getId());

        partialUpdatedPuntoControl
            .identificador(UPDATED_IDENTIFICADOR)
            .orden(UPDATED_ORDEN)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION);

        restPuntoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPuntoControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPuntoControl))
            )
            .andExpect(status().isOk());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
        PuntoControl testPuntoControl = puntoControlList.get(puntoControlList.size() - 1);
        assertThat(testPuntoControl.getIdentificador()).isEqualTo(UPDATED_IDENTIFICADOR);
        assertThat(testPuntoControl.getOrden()).isEqualTo(UPDATED_ORDEN);
        assertThat(testPuntoControl.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPuntoControl.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void fullUpdatePuntoControlWithPatch() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();

        // Update the puntoControl using partial update
        PuntoControl partialUpdatedPuntoControl = new PuntoControl();
        partialUpdatedPuntoControl.setId(puntoControl.getId());

        partialUpdatedPuntoControl
            .identificador(UPDATED_IDENTIFICADOR)
            .orden(UPDATED_ORDEN)
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION);

        restPuntoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPuntoControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPuntoControl))
            )
            .andExpect(status().isOk());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
        PuntoControl testPuntoControl = puntoControlList.get(puntoControlList.size() - 1);
        assertThat(testPuntoControl.getIdentificador()).isEqualTo(UPDATED_IDENTIFICADOR);
        assertThat(testPuntoControl.getOrden()).isEqualTo(UPDATED_ORDEN);
        assertThat(testPuntoControl.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPuntoControl.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void patchNonExistingPuntoControl() throws Exception {
        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();
        puntoControl.setId(count.incrementAndGet());

        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPuntoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, puntoControlDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPuntoControl() throws Exception {
        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();
        puntoControl.setId(count.incrementAndGet());

        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPuntoControl() throws Exception {
        int databaseSizeBeforeUpdate = puntoControlRepository.findAll().size();
        puntoControl.setId(count.incrementAndGet());

        // Create the PuntoControl
        PuntoControlDTO puntoControlDTO = puntoControlMapper.toDto(puntoControl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPuntoControlMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(puntoControlDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PuntoControl in the database
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePuntoControl() throws Exception {
        // Initialize the database
        puntoControlRepository.saveAndFlush(puntoControl);

        int databaseSizeBeforeDelete = puntoControlRepository.findAll().size();

        // Delete the puntoControl
        restPuntoControlMockMvc
            .perform(delete(ENTITY_API_URL_ID, puntoControl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PuntoControl> puntoControlList = puntoControlRepository.findAll();
        assertThat(puntoControlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
