package com.nicogbdev.gymkanapp.service.mapper;

import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.domain.Participante;
import com.nicogbdev.gymkanapp.domain.User;
import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import com.nicogbdev.gymkanapp.service.dto.ParticipanteDTO;
import com.nicogbdev.gymkanapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participante} and its DTO {@link ParticipanteDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParticipanteMapper extends EntityMapper<ParticipanteDTO, Participante> {
    @Mapping(target = "usuarioApp", source = "usuarioApp", qualifiedByName = "userId")
    @Mapping(target = "equipo", source = "equipo", qualifiedByName = "equipoId")
    ParticipanteDTO toDto(Participante s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("equipoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EquipoDTO toDtoEquipoId(Equipo equipo);
}
