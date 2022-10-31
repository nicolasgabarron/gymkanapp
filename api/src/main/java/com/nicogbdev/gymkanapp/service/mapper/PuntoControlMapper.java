package com.nicogbdev.gymkanapp.service.mapper;

import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PuntoControl} and its DTO {@link PuntoControlDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PuntoControlMapper extends EntityMapper<PuntoControlDTO, PuntoControl> {}
