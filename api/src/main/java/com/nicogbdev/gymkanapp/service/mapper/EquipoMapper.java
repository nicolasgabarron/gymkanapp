package com.nicogbdev.gymkanapp.service.mapper;

import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Equipo} and its DTO {@link EquipoDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EquipoMapper extends EntityMapper<EquipoDTO, Equipo> {}
