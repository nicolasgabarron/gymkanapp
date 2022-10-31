package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.domain.*; // for static metamodels
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.repository.VoluntarioRepository;
import com.nicogbdev.gymkanapp.service.criteria.VoluntarioCriteria;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
import com.nicogbdev.gymkanapp.service.mapper.VoluntarioMapper;
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
 * Service for executing complex queries for {@link Voluntario} entities in the database.
 * The main input is a {@link VoluntarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VoluntarioDTO} or a {@link Page} of {@link VoluntarioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoluntarioQueryService extends QueryService<Voluntario> {

    private final Logger log = LoggerFactory.getLogger(VoluntarioQueryService.class);

    private final VoluntarioRepository voluntarioRepository;

    private final VoluntarioMapper voluntarioMapper;

    public VoluntarioQueryService(VoluntarioRepository voluntarioRepository, VoluntarioMapper voluntarioMapper) {
        this.voluntarioRepository = voluntarioRepository;
        this.voluntarioMapper = voluntarioMapper;
    }

    /**
     * Return a {@link List} of {@link VoluntarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VoluntarioDTO> findByCriteria(VoluntarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Voluntario> specification = createSpecification(criteria);
        return voluntarioMapper.toDto(voluntarioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VoluntarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VoluntarioDTO> findByCriteria(VoluntarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Voluntario> specification = createSpecification(criteria);
        return voluntarioRepository.findAll(specification, page).map(voluntarioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VoluntarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Voluntario> specification = createSpecification(criteria);
        return voluntarioRepository.count(specification);
    }

    /**
     * Function to convert {@link VoluntarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Voluntario> createSpecification(VoluntarioCriteria criteria) {
        Specification<Voluntario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Voluntario_.id));
            }
            if (criteria.getDni() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDni(), Voluntario_.dni));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Voluntario_.nombre));
            }
            if (criteria.getApellidos() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellidos(), Voluntario_.apellidos));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), Voluntario_.fechaNacimiento));
            }
            if (criteria.getUsuarioAppId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioAppId(),
                            root -> root.join(Voluntario_.usuarioApp, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getPuntoControlId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPuntoControlId(),
                            root -> root.join(Voluntario_.puntoControl, JoinType.LEFT).get(PuntoControl_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
