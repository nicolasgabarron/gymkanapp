package com.nicogbdev.gymkanapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nicogbdev.gymkanapp.IntegrationTest;
import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.domain.Participante;
import com.nicogbdev.gymkanapp.domain.User;
import com.nicogbdev.gymkanapp.repository.ParticipanteRepository;
import com.nicogbdev.gymkanapp.service.criteria.ParticipanteCriteria;
import com.nicogbdev.gymkanapp.service.dto.ParticipanteDTO;
import com.nicogbdev.gymkanapp.service.mapper.ParticipanteMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ParticipanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParticipanteResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/participantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private ParticipanteMapper participanteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticipanteMockMvc;

    private Participante participante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participante createEntity(EntityManager em) {
        Participante participante = new Participante()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        return participante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participante createUpdatedEntity(EntityManager em) {
        Participante participante = new Participante()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        return participante;
    }

    @BeforeEach
    public void initTest() {
        participante = createEntity(em);
    }

    @Test
    @Transactional
    void createParticipante() throws Exception {
        int databaseSizeBeforeCreate = participanteRepository.findAll().size();
        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);
        restParticipanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeCreate + 1);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testParticipante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParticipante.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testParticipante.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void createParticipanteWithExistingId() throws Exception {
        // Create the Participante with an existing ID
        participante.setId(1L);
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        int databaseSizeBeforeCreate = participanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParticipantes() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participante.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @Test
    @Transactional
    void getParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get the participante
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL_ID, participante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participante.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getParticipantesByIdFiltering() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        Long id = participante.getId();

        defaultParticipanteShouldBeFound("id.equals=" + id);
        defaultParticipanteShouldNotBeFound("id.notEquals=" + id);

        defaultParticipanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParticipanteShouldNotBeFound("id.greaterThan=" + id);

        defaultParticipanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParticipanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParticipantesByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where dni equals to DEFAULT_DNI
        defaultParticipanteShouldBeFound("dni.equals=" + DEFAULT_DNI);

        // Get all the participanteList where dni equals to UPDATED_DNI
        defaultParticipanteShouldNotBeFound("dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllParticipantesByDniIsInShouldWork() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where dni in DEFAULT_DNI or UPDATED_DNI
        defaultParticipanteShouldBeFound("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI);

        // Get all the participanteList where dni equals to UPDATED_DNI
        defaultParticipanteShouldNotBeFound("dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllParticipantesByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where dni is not null
        defaultParticipanteShouldBeFound("dni.specified=true");

        // Get all the participanteList where dni is null
        defaultParticipanteShouldNotBeFound("dni.specified=false");
    }

    @Test
    @Transactional
    void getAllParticipantesByDniContainsSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where dni contains DEFAULT_DNI
        defaultParticipanteShouldBeFound("dni.contains=" + DEFAULT_DNI);

        // Get all the participanteList where dni contains UPDATED_DNI
        defaultParticipanteShouldNotBeFound("dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllParticipantesByDniNotContainsSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where dni does not contain DEFAULT_DNI
        defaultParticipanteShouldNotBeFound("dni.doesNotContain=" + DEFAULT_DNI);

        // Get all the participanteList where dni does not contain UPDATED_DNI
        defaultParticipanteShouldBeFound("dni.doesNotContain=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllParticipantesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where nombre equals to DEFAULT_NOMBRE
        defaultParticipanteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the participanteList where nombre equals to UPDATED_NOMBRE
        defaultParticipanteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllParticipantesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultParticipanteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the participanteList where nombre equals to UPDATED_NOMBRE
        defaultParticipanteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllParticipantesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where nombre is not null
        defaultParticipanteShouldBeFound("nombre.specified=true");

        // Get all the participanteList where nombre is null
        defaultParticipanteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllParticipantesByNombreContainsSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where nombre contains DEFAULT_NOMBRE
        defaultParticipanteShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the participanteList where nombre contains UPDATED_NOMBRE
        defaultParticipanteShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllParticipantesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where nombre does not contain DEFAULT_NOMBRE
        defaultParticipanteShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the participanteList where nombre does not contain UPDATED_NOMBRE
        defaultParticipanteShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllParticipantesByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where apellidos equals to DEFAULT_APELLIDOS
        defaultParticipanteShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the participanteList where apellidos equals to UPDATED_APELLIDOS
        defaultParticipanteShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllParticipantesByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultParticipanteShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the participanteList where apellidos equals to UPDATED_APELLIDOS
        defaultParticipanteShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllParticipantesByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where apellidos is not null
        defaultParticipanteShouldBeFound("apellidos.specified=true");

        // Get all the participanteList where apellidos is null
        defaultParticipanteShouldNotBeFound("apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllParticipantesByApellidosContainsSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where apellidos contains DEFAULT_APELLIDOS
        defaultParticipanteShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the participanteList where apellidos contains UPDATED_APELLIDOS
        defaultParticipanteShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllParticipantesByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where apellidos does not contain DEFAULT_APELLIDOS
        defaultParticipanteShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the participanteList where apellidos does not contain UPDATED_APELLIDOS
        defaultParticipanteShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultParticipanteShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the participanteList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultParticipanteShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultParticipanteShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the participanteList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultParticipanteShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento is not null
        defaultParticipanteShouldBeFound("fechaNacimiento.specified=true");

        // Get all the participanteList where fechaNacimiento is null
        defaultParticipanteShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultParticipanteShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the participanteList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultParticipanteShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultParticipanteShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the participanteList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultParticipanteShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultParticipanteShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the participanteList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultParticipanteShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllParticipantesByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        // Get all the participanteList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultParticipanteShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the participanteList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultParticipanteShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllParticipantesByUsuarioAppIsEqualToSomething() throws Exception {
        User usuarioApp;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            participanteRepository.saveAndFlush(participante);
            usuarioApp = UserResourceIT.createEntity(em);
        } else {
            usuarioApp = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuarioApp);
        em.flush();
        participante.setUsuarioApp(usuarioApp);
        participanteRepository.saveAndFlush(participante);
        Long usuarioAppId = usuarioApp.getId();

        // Get all the participanteList where usuarioApp equals to usuarioAppId
        defaultParticipanteShouldBeFound("usuarioAppId.equals=" + usuarioAppId);

        // Get all the participanteList where usuarioApp equals to (usuarioAppId + 1)
        defaultParticipanteShouldNotBeFound("usuarioAppId.equals=" + (usuarioAppId + 1));
    }

    @Test
    @Transactional
    void getAllParticipantesByEquipoIsEqualToSomething() throws Exception {
        Equipo equipo;
        if (TestUtil.findAll(em, Equipo.class).isEmpty()) {
            participanteRepository.saveAndFlush(participante);
            equipo = EquipoResourceIT.createEntity(em);
        } else {
            equipo = TestUtil.findAll(em, Equipo.class).get(0);
        }
        em.persist(equipo);
        em.flush();
        participante.setEquipo(equipo);
        participanteRepository.saveAndFlush(participante);
        Long equipoId = equipo.getId();

        // Get all the participanteList where equipo equals to equipoId
        defaultParticipanteShouldBeFound("equipoId.equals=" + equipoId);

        // Get all the participanteList where equipo equals to (equipoId + 1)
        defaultParticipanteShouldNotBeFound("equipoId.equals=" + (equipoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParticipanteShouldBeFound(String filter) throws Exception {
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participante.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));

        // Check, that the count call also returns 1
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParticipanteShouldNotBeFound(String filter) throws Exception {
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParticipanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParticipante() throws Exception {
        // Get the participante
        restParticipanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante
        Participante updatedParticipante = participanteRepository.findById(participante.getId()).get();
        // Disconnect from session so that the updates on updatedParticipante are not directly saved in db
        em.detach(updatedParticipante);
        updatedParticipante.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).apellidos(UPDATED_APELLIDOS).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        ParticipanteDTO participanteDTO = participanteMapper.toDto(updatedParticipante);

        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, participanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testParticipante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParticipante.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testParticipante.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void putNonExistingParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, participanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParticipanteWithPatch() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante using partial update
        Participante partialUpdatedParticipante = new Participante();
        partialUpdatedParticipante.setId(participante.getId());

        partialUpdatedParticipante.fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testParticipante.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParticipante.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testParticipante.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void fullUpdateParticipanteWithPatch() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();

        // Update the participante using partial update
        Participante partialUpdatedParticipante = new Participante();
        partialUpdatedParticipante.setId(participante.getId());

        partialUpdatedParticipante
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParticipante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParticipante))
            )
            .andExpect(status().isOk());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
        Participante testParticipante = participanteList.get(participanteList.size() - 1);
        assertThat(testParticipante.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testParticipante.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParticipante.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testParticipante.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void patchNonExistingParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, participanteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParticipante() throws Exception {
        int databaseSizeBeforeUpdate = participanteRepository.findAll().size();
        participante.setId(count.incrementAndGet());

        // Create the Participante
        ParticipanteDTO participanteDTO = participanteMapper.toDto(participante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParticipanteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(participanteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Participante in the database
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParticipante() throws Exception {
        // Initialize the database
        participanteRepository.saveAndFlush(participante);

        int databaseSizeBeforeDelete = participanteRepository.findAll().size();

        // Delete the participante
        restParticipanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, participante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participante> participanteList = participanteRepository.findAll();
        assertThat(participanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
