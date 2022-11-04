package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.Authority;
import com.nicogbdev.gymkanapp.domain.Participante;
import com.nicogbdev.gymkanapp.domain.User;
import com.nicogbdev.gymkanapp.repository.AuthorityRepository;
import com.nicogbdev.gymkanapp.repository.ParticipanteRepository;
import com.nicogbdev.gymkanapp.repository.UserRepository;
import com.nicogbdev.gymkanapp.security.AuthoritiesConstants;
import com.nicogbdev.gymkanapp.service.ParticipanteService;
import com.nicogbdev.gymkanapp.service.dto.ParticipanteDTO;
import com.nicogbdev.gymkanapp.service.mapper.ParticipanteMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.nicogbdev.gymkanapp.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Participante}.
 */
@Service
@Transactional
public class ParticipanteServiceImpl implements ParticipanteService {

    private final Logger log = LoggerFactory.getLogger(ParticipanteServiceImpl.class);

    private final ParticipanteRepository participanteRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final ParticipanteMapper participanteMapper;

    public ParticipanteServiceImpl(ParticipanteRepository participanteRepository, UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, ParticipanteMapper participanteMapper) {
        this.participanteRepository = participanteRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.participanteMapper = participanteMapper;
    }

    @Override
    public ParticipanteDTO save(ParticipanteDTO participanteDTO) {
        log.debug("Request to save Participante : {}", participanteDTO);

        // Authorities del voluntario
        Set<Authority> authoritySet = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authoritySet::add);

        // Creación del usuario que utilizará el voluntario
        User participanteUser = new User();
        participanteUser.setLogin(participanteDTO.getDni());
        participanteUser.setPassword(passwordEncoder.encode("SoyComoTu_" + participanteDTO.getDni()));
        participanteUser.setFirstName(participanteDTO.getNombre());
        participanteUser.setLastName(participanteDTO.getApellidos());
        participanteUser.setActivated(true);
        participanteUser.setLangKey("es");
        participanteUser.setAuthorities(authoritySet);

        participanteUser = userRepository.save(participanteUser);

        // Asigno el ID del usuario creado al Participante
        participanteDTO.setUsuarioApp(userMapper.userToUserDTO(participanteUser));

        Participante participante = participanteMapper.toEntity(participanteDTO);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    @Override
    public ParticipanteDTO update(ParticipanteDTO participanteDTO) {
        log.debug("Request to update Participante : {}", participanteDTO);
        Participante participante = participanteMapper.toEntity(participanteDTO);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    @Override
    public Optional<ParticipanteDTO> partialUpdate(ParticipanteDTO participanteDTO) {
        log.debug("Request to partially update Participante : {}", participanteDTO);

        return participanteRepository
            .findById(participanteDTO.getId())
            .map(existingParticipante -> {
                participanteMapper.partialUpdate(existingParticipante, participanteDTO);

                return existingParticipante;
            })
            .map(participanteRepository::save)
            .map(participanteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParticipanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Participantes");
        return participanteRepository.findAll(pageable).map(participanteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParticipanteDTO> findOne(Long id) {
        log.debug("Request to get Participante : {}", id);
        return participanteRepository.findById(id).map(participanteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Participante : {}", id);
        participanteRepository.deleteById(id);
    }
}
