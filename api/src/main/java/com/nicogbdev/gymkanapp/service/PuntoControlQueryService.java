package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.domain.*; // for static metamodels
import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.repository.PuntoControlRepository;
import com.nicogbdev.gymkanapp.service.criteria.PuntoControlCriteria;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import com.nicogbdev.gymkanapp.service.mapper.PuntoControlMapper;
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
 * Service for executing complex queries for {@link PuntoControl} entities in the database.
 * The main input is a {@link PuntoControlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PuntoControlDTO} or a {@link Page} of {@link PuntoControlDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PuntoControlQueryService extends QueryService<PuntoControl> {

    private final Logger log = LoggerFactory.getLogger(PuntoControlQueryService.class);

    private final PuntoControlRepository puntoControlRepository;

    private final PuntoControlMapper puntoControlMapper;

    public PuntoControlQueryService(PuntoControlRepository puntoControlRepository, PuntoControlMapper puntoControlMapper) {
        this.puntoControlRepository = puntoControlRepository;
        this.puntoControlMapper = puntoControlMapper;
    }

    /**
     * Return a {@link List} of {@link PuntoControlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PuntoControlDTO> findByCriteria(PuntoControlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PuntoControl> specification = createSpecification(criteria);
        return puntoControlMapper.toDto(puntoControlRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PuntoControlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PuntoControlDTO> findByCriteria(PuntoControlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PuntoControl> specification = createSpecification(criteria);
        return puntoControlRepository.findAll(specification, page).map(puntoControlMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PuntoControlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PuntoControl> specification = createSpecification(criteria);
        return puntoControlRepository.count(specification);
    }

    /**
     * Function to convert {@link PuntoControlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PuntoControl> createSpecification(PuntoControlCriteria criteria) {
        Specification<PuntoControl> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PuntoControl_.id));
            }
            if (criteria.getIdentificador() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentificador(), PuntoControl_.identificador));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), PuntoControl_.orden));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), PuntoControl_.nombre));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), PuntoControl_.direccion));
            }
            if (criteria.getVoluntariosId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getVoluntariosId(),
                            root -> root.join(PuntoControl_.voluntarios, JoinType.LEFT).get(Voluntario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
