package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.domain.*; // for static metamodels
import com.nicogbdev.gymkanapp.domain.PasoControl;
import com.nicogbdev.gymkanapp.repository.PasoControlRepository;
import com.nicogbdev.gymkanapp.service.criteria.PasoControlCriteria;
import com.nicogbdev.gymkanapp.service.dto.PasoControlDTO;
import com.nicogbdev.gymkanapp.service.mapper.PasoControlMapper;
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
 * Service for executing complex queries for {@link PasoControl} entities in the database.
 * The main input is a {@link PasoControlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PasoControlDTO} or a {@link Page} of {@link PasoControlDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PasoControlQueryService extends QueryService<PasoControl> {

    private final Logger log = LoggerFactory.getLogger(PasoControlQueryService.class);

    private final PasoControlRepository pasoControlRepository;

    private final PasoControlMapper pasoControlMapper;

    public PasoControlQueryService(PasoControlRepository pasoControlRepository, PasoControlMapper pasoControlMapper) {
        this.pasoControlRepository = pasoControlRepository;
        this.pasoControlMapper = pasoControlMapper;
    }

    /**
     * Return a {@link List} of {@link PasoControlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PasoControlDTO> findByCriteria(PasoControlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PasoControl> specification = createSpecification(criteria);
        return pasoControlMapper.toDto(pasoControlRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PasoControlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PasoControlDTO> findByCriteria(PasoControlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PasoControl> specification = createSpecification(criteria);
        return pasoControlRepository.findAll(specification, page).map(pasoControlMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PasoControlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PasoControl> specification = createSpecification(criteria);
        return pasoControlRepository.count(specification);
    }

    /**
     * Function to convert {@link PasoControlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PasoControl> createSpecification(PasoControlCriteria criteria) {
        Specification<PasoControl> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PasoControl_.id));
            }
            if (criteria.getFechaHora() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaHora(), PasoControl_.fechaHora));
            }
            if (criteria.getEquipoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEquipoId(), root -> root.join(PasoControl_.equipo, JoinType.LEFT).get(Equipo_.id))
                    );
            }
            if (criteria.getPuntoControlId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPuntoControlId(),
                            root -> root.join(PasoControl_.puntoControl, JoinType.LEFT).get(PuntoControl_.id)
                        )
                    );
            }
            if (criteria.getValidadoPorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getValidadoPorId(),
                            root -> root.join(PasoControl_.validadoPor, JoinType.LEFT).get(Voluntario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
