package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.Authority;
import com.nicogbdev.gymkanapp.domain.User;
import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.repository.AuthorityRepository;
import com.nicogbdev.gymkanapp.repository.UserRepository;
import com.nicogbdev.gymkanapp.repository.VoluntarioRepository;
import com.nicogbdev.gymkanapp.security.AuthoritiesConstants;
import com.nicogbdev.gymkanapp.service.VoluntarioService;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
import com.nicogbdev.gymkanapp.service.mapper.UserMapper;
import com.nicogbdev.gymkanapp.service.mapper.VoluntarioMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Voluntario}.
 */
@Service
@Transactional
public class VoluntarioServiceImpl implements VoluntarioService {

    private final Logger log = LoggerFactory.getLogger(VoluntarioServiceImpl.class);

    private final VoluntarioRepository voluntarioRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final VoluntarioMapper voluntarioMapper;

    private final UserMapper userMapper;

    public VoluntarioServiceImpl(VoluntarioRepository voluntarioRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, VoluntarioMapper voluntarioMapper, UserMapper userMapper) {
        this.voluntarioRepository = voluntarioRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.voluntarioMapper = voluntarioMapper;
        this.userMapper = userMapper;
    }

    @Override
    public VoluntarioDTO save(VoluntarioDTO voluntarioDTO) {
        log.debug("Request to save Voluntario : {}", voluntarioDTO);

        // Authorities del voluntario
        Set<Authority> authoritySet = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authoritySet::add);
        authorityRepository.findById(AuthoritiesConstants.VOLUNTARIO).ifPresent(authoritySet::add);

        // Creación del usuario que utilizará el voluntario
        User voluntarioUser = new User();
        voluntarioUser.setLogin(voluntarioDTO.getDni());
        voluntarioUser.setPassword(passwordEncoder.encode("SoyComoTu_" + voluntarioDTO.getDni()));
        voluntarioUser.setFirstName(voluntarioDTO.getNombre());
        voluntarioUser.setLastName(voluntarioDTO.getApellidos());
        voluntarioUser.setActivated(true);
        voluntarioUser.setLangKey("es");
        voluntarioUser.setAuthorities(authoritySet);

        voluntarioUser = userRepository.save(voluntarioUser);

        // Asigno el ID del usuario creado al Voluntario
        voluntarioDTO.setUsuarioApp(userMapper.userToUserDTO(voluntarioUser));

        Voluntario voluntario = voluntarioMapper.toEntity(voluntarioDTO);
        voluntario = voluntarioRepository.save(voluntario);
        return voluntarioMapper.toDto(voluntario);
    }

    @Override
    public VoluntarioDTO update(VoluntarioDTO voluntarioDTO) {
        log.debug("Request to update Voluntario : {}", voluntarioDTO);
        Voluntario voluntario = voluntarioMapper.toEntity(voluntarioDTO);
        voluntario = voluntarioRepository.save(voluntario);
        return voluntarioMapper.toDto(voluntario);
    }

    @Override
    public Optional<VoluntarioDTO> partialUpdate(VoluntarioDTO voluntarioDTO) {
        log.debug("Request to partially update Voluntario : {}", voluntarioDTO);

        return voluntarioRepository
            .findById(voluntarioDTO.getId())
            .map(existingVoluntario -> {
                voluntarioMapper.partialUpdate(existingVoluntario, voluntarioDTO);

                return existingVoluntario;
            })
            .map(voluntarioRepository::save)
            .map(voluntarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoluntarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Voluntarios");
        return voluntarioRepository.findAll(pageable).map(voluntarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VoluntarioDTO> findOne(Long id) {
        log.debug("Request to get Voluntario : {}", id);
        return voluntarioRepository.findById(id).map(voluntarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Voluntario : {}", id);
        voluntarioRepository.deleteById(id);
    }
}
