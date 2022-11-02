package com.nicogbdev.gymkanapp.service.mapper;

import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.domain.User;
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import com.nicogbdev.gymkanapp.service.dto.UserDTO;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Voluntario} and its DTO {@link VoluntarioDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VoluntarioMapper extends EntityMapper<VoluntarioDTO, Voluntario> {
    @Mapping(target = "usuarioApp", source = "usuarioApp", qualifiedByName = "userId")
    @Mapping(target = "puntoControl", source = "puntoControl", qualifiedByName = "puntoControlId")
    VoluntarioDTO toDto(Voluntario s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("puntoControlId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    PuntoControlDTO toDtoPuntoControlId(PuntoControl puntoControl);
}
