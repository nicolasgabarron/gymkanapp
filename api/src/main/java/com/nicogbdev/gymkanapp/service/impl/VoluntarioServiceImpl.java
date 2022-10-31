package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.Voluntario;
import com.nicogbdev.gymkanapp.repository.VoluntarioRepository;
import com.nicogbdev.gymkanapp.service.VoluntarioService;
import com.nicogbdev.gymkanapp.service.dto.VoluntarioDTO;
import com.nicogbdev.gymkanapp.service.mapper.VoluntarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final VoluntarioMapper voluntarioMapper;

    public VoluntarioServiceImpl(VoluntarioRepository voluntarioRepository, VoluntarioMapper voluntarioMapper) {
        this.voluntarioRepository = voluntarioRepository;
        this.voluntarioMapper = voluntarioMapper;
    }

    @Override
    public VoluntarioDTO save(VoluntarioDTO voluntarioDTO) {
        log.debug("Request to save Voluntario : {}", voluntarioDTO);
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
