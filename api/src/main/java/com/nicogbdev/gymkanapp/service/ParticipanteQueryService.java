package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.domain.*; // for static metamodels
import com.nicogbdev.gymkanapp.domain.Participante;
import com.nicogbdev.gymkanapp.repository.ParticipanteRepository;
import com.nicogbdev.gymkanapp.service.criteria.ParticipanteCriteria;
import com.nicogbdev.gymkanapp.service.dto.ParticipanteDTO;
import com.nicogbdev.gymkanapp.service.mapper.ParticipanteMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Participante} entities in the database.
 * The main input is a {@link ParticipanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParticipanteDTO} or a {@link Page} of {@link ParticipanteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParticipanteQueryService extends QueryService<Participante> {

    private final Logger log = LoggerFactory.getLogger(ParticipanteQueryService.class);

    private final ParticipanteRepository participanteRepository;

    private final ParticipanteMapper participanteMapper;

    public ParticipanteQueryService(ParticipanteRepository participanteRepository, ParticipanteMapper participanteMapper) {
        this.participanteRepository = participanteRepository;
        this.participanteMapper = participanteMapper;
    }

    /**
     * Return a {@link List} of {@link ParticipanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParticipanteDTO> findByCriteria(ParticipanteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Participante> specification = createSpecification(criteria);
        return participanteMapper.toDto(participanteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParticipanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticipanteDTO> findByCriteria(ParticipanteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Participante> specification = createSpecification(criteria);
        return participanteRepository.findAll(specification, page).map(participanteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParticipanteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Participante> specification = createSpecification(criteria);
        return participanteRepository.count(specification);
    }

    /**
     * Function to convert {@link ParticipanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Participante> createSpecification(ParticipanteCriteria criteria) {
        Specification<Participante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Participante_.id));
            }
            if (criteria.getDni() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDni(), Participante_.dni));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Participante_.nombre));
            }
            if (criteria.getApellidos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellidos(), Participante_.apellidos));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Participante_.fechaNacimiento));
            }
            if (criteria.getUsuarioAppId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioAppId(),
                            root -> root.join(Participante_.usuarioApp, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getEquipoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEquipoId(), root -> root.join(Participante_.equipo, JoinType.LEFT).get(Equipo_.id))
                    );
            }
        }
        return specification;
    }
}
