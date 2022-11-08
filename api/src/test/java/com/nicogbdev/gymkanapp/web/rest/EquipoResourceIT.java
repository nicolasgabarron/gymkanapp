package com.nicogbdev.gymkanapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nicogbdev.gymkanapp.IntegrationTest;
import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.domain.Participante;
import com.nicogbdev.gymkanapp.repository.EquipoRepository;
import com.nicogbdev.gymkanapp.service.criteria.EquipoCriteria;
import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import com.nicogbdev.gymkanapp.service.mapper.EquipoMapper;
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
 * Integration tests for the {@link EquipoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipoResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_INTEGRANTES = 1;
    private static final Integer UPDATED_CANTIDAD_INTEGRANTES = 2;
    private static final Integer SMALLER_CANTIDAD_INTEGRANTES = 1 - 1;

    private static final String ENTITY_API_URL = "/api/equipos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EquipoMapper equipoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipoMockMvc;

    private Equipo equipo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipo createEntity(EntityManager em) {
        Equipo equipo = new Equipo()
            .identificador(DEFAULT_IDENTIFICADOR)
            .nombre(DEFAULT_NOMBRE)
            .cantidadIntegrantes(DEFAULT_CANTIDAD_INTEGRANTES);
        return equipo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipo createUpdatedEntity(EntityManager em) {
        Equipo equipo = new Equipo()
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .cantidadIntegrantes(UPDATED_CANTIDAD_INTEGRANTES);
        return equipo;
    }

    @BeforeEach
    public void initTest() {
        equipo = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipo() throws Exception {
        int databaseSizeBeforeCreate = equipoRepository.findAll().size();
        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);
        restEquipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipoDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeCreate + 1);
        Equipo testEquipo = equipoList.get(equipoList.size() - 1);
        assertThat(testEquipo.getIdentificador()).isEqualTo(DEFAULT_IDENTIFICADOR);
        assertThat(testEquipo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEquipo.getCantidadIntegrantes()).isEqualTo(DEFAULT_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void createEquipoWithExistingId() throws Exception {
        // Create the Equipo with an existing ID
        equipo.setId(1L);
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        int databaseSizeBeforeCreate = equipoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIntegrantesIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipoRepository.findAll().size();
        // set the field null
        equipo.setCantidadIntegrantes(null);

        // Create the Equipo, which fails.
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        restEquipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipoDTO)))
            .andExpect(status().isBadRequest());

        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquipos() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList
        restEquipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidadIntegrantes").value(hasItem(DEFAULT_CANTIDAD_INTEGRANTES)));
    }

    @Test
    @Transactional
    void getEquipo() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get the equipo
        restEquipoMockMvc
            .perform(get(ENTITY_API_URL_ID, equipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipo.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.cantidadIntegrantes").value(DEFAULT_CANTIDAD_INTEGRANTES));
    }

    @Test
    @Transactional
    void getEquiposByIdFiltering() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        Long id = equipo.getId();

        defaultEquipoShouldBeFound("id.equals=" + id);
        defaultEquipoShouldNotBeFound("id.notEquals=" + id);

        defaultEquipoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipoShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEquiposByIdentificadorIsEqualToSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where identificador equals to DEFAULT_IDENTIFICADOR
        defaultEquipoShouldBeFound("identificador.equals=" + DEFAULT_IDENTIFICADOR);

        // Get all the equipoList where identificador equals to UPDATED_IDENTIFICADOR
        defaultEquipoShouldNotBeFound("identificador.equals=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllEquiposByIdentificadorIsInShouldWork() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where identificador in DEFAULT_IDENTIFICADOR or UPDATED_IDENTIFICADOR
        defaultEquipoShouldBeFound("identificador.in=" + DEFAULT_IDENTIFICADOR + "," + UPDATED_IDENTIFICADOR);

        // Get all the equipoList where identificador equals to UPDATED_IDENTIFICADOR
        defaultEquipoShouldNotBeFound("identificador.in=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllEquiposByIdentificadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where identificador is not null
        defaultEquipoShouldBeFound("identificador.specified=true");

        // Get all the equipoList where identificador is null
        defaultEquipoShouldNotBeFound("identificador.specified=false");
    }

    @Test
    @Transactional
    void getAllEquiposByIdentificadorContainsSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where identificador contains DEFAULT_IDENTIFICADOR
        defaultEquipoShouldBeFound("identificador.contains=" + DEFAULT_IDENTIFICADOR);

        // Get all the equipoList where identificador contains UPDATED_IDENTIFICADOR
        defaultEquipoShouldNotBeFound("identificador.contains=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllEquiposByIdentificadorNotContainsSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where identificador does not contain DEFAULT_IDENTIFICADOR
        defaultEquipoShouldNotBeFound("identificador.doesNotContain=" + DEFAULT_IDENTIFICADOR);

        // Get all the equipoList where identificador does not contain UPDATED_IDENTIFICADOR
        defaultEquipoShouldBeFound("identificador.doesNotContain=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllEquiposByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where nombre equals to DEFAULT_NOMBRE
        defaultEquipoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the equipoList where nombre equals to UPDATED_NOMBRE
        defaultEquipoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEquiposByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultEquipoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the equipoList where nombre equals to UPDATED_NOMBRE
        defaultEquipoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEquiposByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where nombre is not null
        defaultEquipoShouldBeFound("nombre.specified=true");

        // Get all the equipoList where nombre is null
        defaultEquipoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllEquiposByNombreContainsSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where nombre contains DEFAULT_NOMBRE
        defaultEquipoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the equipoList where nombre contains UPDATED_NOMBRE
        defaultEquipoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEquiposByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where nombre does not contain DEFAULT_NOMBRE
        defaultEquipoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the equipoList where nombre does not contain UPDATED_NOMBRE
        defaultEquipoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsEqualToSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes equals to DEFAULT_CANTIDAD_INTEGRANTES
        defaultEquipoShouldBeFound("cantidadIntegrantes.equals=" + DEFAULT_CANTIDAD_INTEGRANTES);

        // Get all the equipoList where cantidadIntegrantes equals to UPDATED_CANTIDAD_INTEGRANTES
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.equals=" + UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsInShouldWork() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes in DEFAULT_CANTIDAD_INTEGRANTES or UPDATED_CANTIDAD_INTEGRANTES
        defaultEquipoShouldBeFound("cantidadIntegrantes.in=" + DEFAULT_CANTIDAD_INTEGRANTES + "," + UPDATED_CANTIDAD_INTEGRANTES);

        // Get all the equipoList where cantidadIntegrantes equals to UPDATED_CANTIDAD_INTEGRANTES
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.in=" + UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes is not null
        defaultEquipoShouldBeFound("cantidadIntegrantes.specified=true");

        // Get all the equipoList where cantidadIntegrantes is null
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.specified=false");
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes is greater than or equal to DEFAULT_CANTIDAD_INTEGRANTES
        defaultEquipoShouldBeFound("cantidadIntegrantes.greaterThanOrEqual=" + DEFAULT_CANTIDAD_INTEGRANTES);

        // Get all the equipoList where cantidadIntegrantes is greater than or equal to UPDATED_CANTIDAD_INTEGRANTES
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.greaterThanOrEqual=" + UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes is less than or equal to DEFAULT_CANTIDAD_INTEGRANTES
        defaultEquipoShouldBeFound("cantidadIntegrantes.lessThanOrEqual=" + DEFAULT_CANTIDAD_INTEGRANTES);

        // Get all the equipoList where cantidadIntegrantes is less than or equal to SMALLER_CANTIDAD_INTEGRANTES
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.lessThanOrEqual=" + SMALLER_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsLessThanSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes is less than DEFAULT_CANTIDAD_INTEGRANTES
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.lessThan=" + DEFAULT_CANTIDAD_INTEGRANTES);

        // Get all the equipoList where cantidadIntegrantes is less than UPDATED_CANTIDAD_INTEGRANTES
        defaultEquipoShouldBeFound("cantidadIntegrantes.lessThan=" + UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void getAllEquiposByCantidadIntegrantesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        // Get all the equipoList where cantidadIntegrantes is greater than DEFAULT_CANTIDAD_INTEGRANTES
        defaultEquipoShouldNotBeFound("cantidadIntegrantes.greaterThan=" + DEFAULT_CANTIDAD_INTEGRANTES);

        // Get all the equipoList where cantidadIntegrantes is greater than SMALLER_CANTIDAD_INTEGRANTES
        defaultEquipoShouldBeFound("cantidadIntegrantes.greaterThan=" + SMALLER_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void getAllEquiposByParticipantesIsEqualToSomething() throws Exception {
        Participante participantes;
        if (TestUtil.findAll(em, Participante.class).isEmpty()) {
            equipoRepository.saveAndFlush(equipo);
            participantes = ParticipanteResourceIT.createEntity(em);
        } else {
            participantes = TestUtil.findAll(em, Participante.class).get(0);
        }
        em.persist(participantes);
        em.flush();
        equipo.addParticipantes(participantes);
        equipoRepository.saveAndFlush(equipo);
        Long participantesId = participantes.getId();

        // Get all the equipoList where participantes equals to participantesId
        defaultEquipoShouldBeFound("participantesId.equals=" + participantesId);

        // Get all the equipoList where participantes equals to (participantesId + 1)
        defaultEquipoShouldNotBeFound("participantesId.equals=" + (participantesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipoShouldBeFound(String filter) throws Exception {
        restEquipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].cantidadIntegrantes").value(hasItem(DEFAULT_CANTIDAD_INTEGRANTES)));

        // Check, that the count call also returns 1
        restEquipoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipoShouldNotBeFound(String filter) throws Exception {
        restEquipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEquipo() throws Exception {
        // Get the equipo
        restEquipoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEquipo() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();

        // Update the equipo
        Equipo updatedEquipo = equipoRepository.findById(equipo.getId()).get();
        // Disconnect from session so that the updates on updatedEquipo are not directly saved in db
        em.detach(updatedEquipo);
        updatedEquipo.identificador(UPDATED_IDENTIFICADOR).nombre(UPDATED_NOMBRE).cantidadIntegrantes(UPDATED_CANTIDAD_INTEGRANTES);
        EquipoDTO equipoDTO = equipoMapper.toDto(updatedEquipo);

        restEquipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
        Equipo testEquipo = equipoList.get(equipoList.size() - 1);
        assertThat(testEquipo.getIdentificador()).isEqualTo(UPDATED_IDENTIFICADOR);
        assertThat(testEquipo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEquipo.getCantidadIntegrantes()).isEqualTo(UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void putNonExistingEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();
        equipo.setId(count.incrementAndGet());

        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();
        equipo.setId(count.incrementAndGet());

        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();
        equipo.setId(count.incrementAndGet());

        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipoWithPatch() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();

        // Update the equipo using partial update
        Equipo partialUpdatedEquipo = new Equipo();
        partialUpdatedEquipo.setId(equipo.getId());

        partialUpdatedEquipo.cantidadIntegrantes(UPDATED_CANTIDAD_INTEGRANTES);

        restEquipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipo))
            )
            .andExpect(status().isOk());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
        Equipo testEquipo = equipoList.get(equipoList.size() - 1);
        assertThat(testEquipo.getIdentificador()).isEqualTo(DEFAULT_IDENTIFICADOR);
        assertThat(testEquipo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEquipo.getCantidadIntegrantes()).isEqualTo(UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void fullUpdateEquipoWithPatch() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();

        // Update the equipo using partial update
        Equipo partialUpdatedEquipo = new Equipo();
        partialUpdatedEquipo.setId(equipo.getId());

        partialUpdatedEquipo.identificador(UPDATED_IDENTIFICADOR).nombre(UPDATED_NOMBRE).cantidadIntegrantes(UPDATED_CANTIDAD_INTEGRANTES);

        restEquipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipo))
            )
            .andExpect(status().isOk());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
        Equipo testEquipo = equipoList.get(equipoList.size() - 1);
        assertThat(testEquipo.getIdentificador()).isEqualTo(UPDATED_IDENTIFICADOR);
        assertThat(testEquipo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEquipo.getCantidadIntegrantes()).isEqualTo(UPDATED_CANTIDAD_INTEGRANTES);
    }

    @Test
    @Transactional
    void patchNonExistingEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();
        equipo.setId(count.incrementAndGet());

        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();
        equipo.setId(count.incrementAndGet());

        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipo() throws Exception {
        int databaseSizeBeforeUpdate = equipoRepository.findAll().size();
        equipo.setId(count.incrementAndGet());

        // Create the Equipo
        EquipoDTO equipoDTO = equipoMapper.toDto(equipo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(equipoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipo in the database
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipo() throws Exception {
        // Initialize the database
        equipoRepository.saveAndFlush(equipo);

        int databaseSizeBeforeDelete = equipoRepository.findAll().size();

        // Delete the equipo
        restEquipoMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipo> equipoList = equipoRepository.findAll();
        assertThat(equipoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
