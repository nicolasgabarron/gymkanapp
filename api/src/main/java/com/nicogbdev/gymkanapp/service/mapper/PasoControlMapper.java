package com.nicogbdev.gymkanapp.service.mapper;

import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.domain.PasoControl;
import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import com.nicogbdev.gymkanapp.service.dto.PasoControlDTO;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PasoControl} and its DTO {@link PasoControlDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PasoControlMapper extends EntityMapper<PasoControlDTO, PasoControl> {
    @Mapping(target = "equipo", source = "equipo", qualifiedByName = "equipoId")
    @Mapping(target = "puntoControl", source = "puntoControl", qualifiedByName = "puntoControlId")
    @Mapping(target = "validadoPor", source = "validadoPor", qualifiedByName = "voluntarioId")
    PasoControlDTO toDto(PasoControl s);

    @Named("equipoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identificador", source = "identificador")
    EquipoDTO toDtoEquipoId(Equipo equipo);

    @Named("puntoControlId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    PuntoControlDTO toDtoPuntoControlId(PuntoControl puntoControl);

    @Named("voluntarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dni", source = "dni")
    VoluntarioDTO toDtoVoluntarioId(Voluntario voluntario);
}
