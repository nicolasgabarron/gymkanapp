package com.nicogbdev.gymkanapp.service;

import com.nicogbdev.gymkanapp.domain.*; // for static metamodels
import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.repository.EquipoRepository;
import com.nicogbdev.gymkanapp.service.criteria.EquipoCriteria;
import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import com.nicogbdev.gymkanapp.service.mapper.EquipoMapper;
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
 * Service for executing complex queries for {@link Equipo} entities in the database.
 * The main input is a {@link EquipoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipoDTO} or a {@link Page} of {@link EquipoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipoQueryService extends QueryService<Equipo> {

    private final Logger log = LoggerFactory.getLogger(EquipoQueryService.class);

    private final EquipoRepository equipoRepository;

    private final EquipoMapper equipoMapper;

    public EquipoQueryService(EquipoRepository equipoRepository, EquipoMapper equipoMapper) {
        this.equipoRepository = equipoRepository;
        this.equipoMapper = equipoMapper;
    }

    /**
     * Return a {@link List} of {@link EquipoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipoDTO> findByCriteria(EquipoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Equipo> specification = createSpecification(criteria);
        return equipoMapper.toDto(equipoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipoDTO> findByCriteria(EquipoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Equipo> specification = createSpecification(criteria);
        return equipoRepository.findAll(specification, page).map(equipoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Equipo> specification = createSpecification(criteria);
        return equipoRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Equipo> createSpecification(EquipoCriteria criteria) {
        Specification<Equipo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Equipo_.id));
            }
            if (criteria.getIdentificador() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentificador(), Equipo_.identificador));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Equipo_.nombre));
            }
            if (criteria.getCantidadIntegrantes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidadIntegrantes(), Equipo_.cantidadIntegrantes));
            }
            if (criteria.getParticipantesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParticipantesId(),
                            root -> root.join(Equipo_.participantes, JoinType.LEFT).get(Participante_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
