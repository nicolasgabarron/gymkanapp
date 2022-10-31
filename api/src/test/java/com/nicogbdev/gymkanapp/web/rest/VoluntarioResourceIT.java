package com.nicogbdev.gymkanapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nicogbdev.gymkanapp.IntegrationTest;
import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.domain.User;
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.repository.VoluntarioRepository;
import com.nicogbdev.gymkanapp.service.criteria.VoluntarioCriteria;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
import com.nicogbdev.gymkanapp.service.mapper.VoluntarioMapper;
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
 * Integration tests for the {@link VoluntarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoluntarioResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_NACIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/voluntarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private VoluntarioMapper voluntarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoluntarioMockMvc;

    private Voluntario voluntario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voluntario createEntity(EntityManager em) {
        Voluntario voluntario = new Voluntario()
            .dni(DEFAULT_DNI)
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        return voluntario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voluntario createUpdatedEntity(EntityManager em) {
        Voluntario voluntario = new Voluntario()
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        return voluntario;
    }

    @BeforeEach
    public void initTest() {
        voluntario = createEntity(em);
    }

    @Test
    @Transactional
    void createVoluntario() throws Exception {
        int databaseSizeBeforeCreate = voluntarioRepository.findAll().size();
        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);
        restVoluntarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voluntarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeCreate + 1);
        Voluntario testVoluntario = voluntarioList.get(voluntarioList.size() - 1);
        assertThat(testVoluntario.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testVoluntario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVoluntario.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testVoluntario.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void createVoluntarioWithExistingId() throws Exception {
        // Create the Voluntario with an existing ID
        voluntario.setId(1L);
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        int databaseSizeBeforeCreate = voluntarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoluntarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voluntarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoluntarios() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList
        restVoluntarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voluntario.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @Test
    @Transactional
    void getVoluntario() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get the voluntario
        restVoluntarioMockMvc
            .perform(get(ENTITY_API_URL_ID, voluntario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voluntario.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getVoluntariosByIdFiltering() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        Long id = voluntario.getId();

        defaultVoluntarioShouldBeFound("id.equals=" + id);
        defaultVoluntarioShouldNotBeFound("id.notEquals=" + id);

        defaultVoluntarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVoluntarioShouldNotBeFound("id.greaterThan=" + id);

        defaultVoluntarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVoluntarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVoluntariosByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where dni equals to DEFAULT_DNI
        defaultVoluntarioShouldBeFound("dni.equals=" + DEFAULT_DNI);

        // Get all the voluntarioList where dni equals to UPDATED_DNI
        defaultVoluntarioShouldNotBeFound("dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllVoluntariosByDniIsInShouldWork() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where dni in DEFAULT_DNI or UPDATED_DNI
        defaultVoluntarioShouldBeFound("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI);

        // Get all the voluntarioList where dni equals to UPDATED_DNI
        defaultVoluntarioShouldNotBeFound("dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllVoluntariosByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where dni is not null
        defaultVoluntarioShouldBeFound("dni.specified=true");

        // Get all the voluntarioList where dni is null
        defaultVoluntarioShouldNotBeFound("dni.specified=false");
    }

    @Test
    @Transactional
    void getAllVoluntariosByDniContainsSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where dni contains DEFAULT_DNI
        defaultVoluntarioShouldBeFound("dni.contains=" + DEFAULT_DNI);

        // Get all the voluntarioList where dni contains UPDATED_DNI
        defaultVoluntarioShouldNotBeFound("dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllVoluntariosByDniNotContainsSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where dni does not contain DEFAULT_DNI
        defaultVoluntarioShouldNotBeFound("dni.doesNotContain=" + DEFAULT_DNI);

        // Get all the voluntarioList where dni does not contain UPDATED_DNI
        defaultVoluntarioShouldBeFound("dni.doesNotContain=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllVoluntariosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where nombre equals to DEFAULT_NOMBRE
        defaultVoluntarioShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the voluntarioList where nombre equals to UPDATED_NOMBRE
        defaultVoluntarioShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVoluntariosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultVoluntarioShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the voluntarioList where nombre equals to UPDATED_NOMBRE
        defaultVoluntarioShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVoluntariosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where nombre is not null
        defaultVoluntarioShouldBeFound("nombre.specified=true");

        // Get all the voluntarioList where nombre is null
        defaultVoluntarioShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllVoluntariosByNombreContainsSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where nombre contains DEFAULT_NOMBRE
        defaultVoluntarioShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the voluntarioList where nombre contains UPDATED_NOMBRE
        defaultVoluntarioShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVoluntariosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where nombre does not contain DEFAULT_NOMBRE
        defaultVoluntarioShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the voluntarioList where nombre does not contain UPDATED_NOMBRE
        defaultVoluntarioShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllVoluntariosByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where apellidos equals to DEFAULT_APELLIDOS
        defaultVoluntarioShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the voluntarioList where apellidos equals to UPDATED_APELLIDOS
        defaultVoluntarioShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllVoluntariosByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultVoluntarioShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the voluntarioList where apellidos equals to UPDATED_APELLIDOS
        defaultVoluntarioShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllVoluntariosByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where apellidos is not null
        defaultVoluntarioShouldBeFound("apellidos.specified=true");

        // Get all the voluntarioList where apellidos is null
        defaultVoluntarioShouldNotBeFound("apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllVoluntariosByApellidosContainsSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where apellidos contains DEFAULT_APELLIDOS
        defaultVoluntarioShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the voluntarioList where apellidos contains UPDATED_APELLIDOS
        defaultVoluntarioShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllVoluntariosByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where apellidos does not contain DEFAULT_APELLIDOS
        defaultVoluntarioShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the voluntarioList where apellidos does not contain UPDATED_APELLIDOS
        defaultVoluntarioShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento equals to DEFAULT_FECHA_NACIMIENTO
        defaultVoluntarioShouldBeFound("fechaNacimiento.equals=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the voluntarioList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.equals=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsInShouldWork() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento in DEFAULT_FECHA_NACIMIENTO or UPDATED_FECHA_NACIMIENTO
        defaultVoluntarioShouldBeFound("fechaNacimiento.in=" + DEFAULT_FECHA_NACIMIENTO + "," + UPDATED_FECHA_NACIMIENTO);

        // Get all the voluntarioList where fechaNacimiento equals to UPDATED_FECHA_NACIMIENTO
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.in=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento is not null
        defaultVoluntarioShouldBeFound("fechaNacimiento.specified=true");

        // Get all the voluntarioList where fechaNacimiento is null
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento is greater than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultVoluntarioShouldBeFound("fechaNacimiento.greaterThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the voluntarioList where fechaNacimiento is greater than or equal to UPDATED_FECHA_NACIMIENTO
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.greaterThanOrEqual=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento is less than or equal to DEFAULT_FECHA_NACIMIENTO
        defaultVoluntarioShouldBeFound("fechaNacimiento.lessThanOrEqual=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the voluntarioList where fechaNacimiento is less than or equal to SMALLER_FECHA_NACIMIENTO
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.lessThanOrEqual=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento is less than DEFAULT_FECHA_NACIMIENTO
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.lessThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the voluntarioList where fechaNacimiento is less than UPDATED_FECHA_NACIMIENTO
        defaultVoluntarioShouldBeFound("fechaNacimiento.lessThan=" + UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllVoluntariosByFechaNacimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        // Get all the voluntarioList where fechaNacimiento is greater than DEFAULT_FECHA_NACIMIENTO
        defaultVoluntarioShouldNotBeFound("fechaNacimiento.greaterThan=" + DEFAULT_FECHA_NACIMIENTO);

        // Get all the voluntarioList where fechaNacimiento is greater than SMALLER_FECHA_NACIMIENTO
        defaultVoluntarioShouldBeFound("fechaNacimiento.greaterThan=" + SMALLER_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void getAllVoluntariosByUsuarioAppIsEqualToSomething() throws Exception {
        User usuarioApp;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            voluntarioRepository.saveAndFlush(voluntario);
            usuarioApp = UserResourceIT.createEntity(em);
        } else {
            usuarioApp = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuarioApp);
        em.flush();
        voluntario.setUsuarioApp(usuarioApp);
        voluntarioRepository.saveAndFlush(voluntario);
        Long usuarioAppId = usuarioApp.getId();

        // Get all the voluntarioList where usuarioApp equals to usuarioAppId
        defaultVoluntarioShouldBeFound("usuarioAppId.equals=" + usuarioAppId);

        // Get all the voluntarioList where usuarioApp equals to (usuarioAppId + 1)
        defaultVoluntarioShouldNotBeFound("usuarioAppId.equals=" + (usuarioAppId + 1));
    }

    @Test
    @Transactional
    void getAllVoluntariosByPuntoControlIsEqualToSomething() throws Exception {
        PuntoControl puntoControl;
        if (TestUtil.findAll(em, PuntoControl.class).isEmpty()) {
            voluntarioRepository.saveAndFlush(voluntario);
            puntoControl = PuntoControlResourceIT.createEntity(em);
        } else {
            puntoControl = TestUtil.findAll(em, PuntoControl.class).get(0);
        }
        em.persist(puntoControl);
        em.flush();
        voluntario.setPuntoControl(puntoControl);
        voluntarioRepository.saveAndFlush(voluntario);
        Long puntoControlId = puntoControl.getId();

        // Get all the voluntarioList where puntoControl equals to puntoControlId
        defaultVoluntarioShouldBeFound("puntoControlId.equals=" + puntoControlId);

        // Get all the voluntarioList where puntoControl equals to (puntoControlId + 1)
        defaultVoluntarioShouldNotBeFound("puntoControlId.equals=" + (puntoControlId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVoluntarioShouldBeFound(String filter) throws Exception {
        restVoluntarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voluntario.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));

        // Check, that the count call also returns 1
        restVoluntarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVoluntarioShouldNotBeFound(String filter) throws Exception {
        restVoluntarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVoluntarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVoluntario() throws Exception {
        // Get the voluntario
        restVoluntarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoluntario() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();

        // Update the voluntario
        Voluntario updatedVoluntario = voluntarioRepository.findById(voluntario.getId()).get();
        // Disconnect from session so that the updates on updatedVoluntario are not directly saved in db
        em.detach(updatedVoluntario);
        updatedVoluntario.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).apellidos(UPDATED_APELLIDOS).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(updatedVoluntario);

        restVoluntarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voluntarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voluntarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
        Voluntario testVoluntario = voluntarioList.get(voluntarioList.size() - 1);
        assertThat(testVoluntario.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVoluntario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVoluntario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testVoluntario.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void putNonExistingVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();
        voluntario.setId(count.incrementAndGet());

        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voluntarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voluntarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();
        voluntario.setId(count.incrementAndGet());

        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voluntarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();
        voluntario.setId(count.incrementAndGet());

        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voluntarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoluntarioWithPatch() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();

        // Update the voluntario using partial update
        Voluntario partialUpdatedVoluntario = new Voluntario();
        partialUpdatedVoluntario.setId(voluntario.getId());

        partialUpdatedVoluntario.dni(UPDATED_DNI).apellidos(UPDATED_APELLIDOS);

        restVoluntarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoluntario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoluntario))
            )
            .andExpect(status().isOk());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
        Voluntario testVoluntario = voluntarioList.get(voluntarioList.size() - 1);
        assertThat(testVoluntario.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVoluntario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVoluntario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testVoluntario.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void fullUpdateVoluntarioWithPatch() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();

        // Update the voluntario using partial update
        Voluntario partialUpdatedVoluntario = new Voluntario();
        partialUpdatedVoluntario.setId(voluntario.getId());

        partialUpdatedVoluntario
            .dni(UPDATED_DNI)
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restVoluntarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoluntario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoluntario))
            )
            .andExpect(status().isOk());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
        Voluntario testVoluntario = voluntarioList.get(voluntarioList.size() - 1);
        assertThat(testVoluntario.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVoluntario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVoluntario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testVoluntario.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void patchNonExistingVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();
        voluntario.setId(count.incrementAndGet());

        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voluntarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voluntarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();
        voluntario.setId(count.incrementAndGet());

        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voluntarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoluntario() throws Exception {
        int databaseSizeBeforeUpdate = voluntarioRepository.findAll().size();
        voluntario.setId(count.incrementAndGet());

        // Create the Voluntario
        VoluntarioDTO voluntarioDTO = voluntarioMapper.toDto(voluntario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoluntarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voluntarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voluntario in the database
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoluntario() throws Exception {
        // Initialize the database
        voluntarioRepository.saveAndFlush(voluntario);

        int databaseSizeBeforeDelete = voluntarioRepository.findAll().size();

        // Delete the voluntario
        restVoluntarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, voluntario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voluntario> voluntarioList = voluntarioRepository.findAll();
        assertThat(voluntarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
