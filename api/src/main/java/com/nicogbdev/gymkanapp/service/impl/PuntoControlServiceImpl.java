package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.PuntoControl;
import com.nicogbdev.gymkanapp.repository.PuntoControlRepository;
import com.nicogbdev.gymkanapp.service.PuntoControlService;
import com.nicogbdev.gymkanapp.service.dto.PuntoControlDTO;
import com.nicogbdev.gymkanapp.service.mapper.PuntoControlMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PuntoControl}.
 */
@Service
@Transactional
public class PuntoControlServiceImpl implements PuntoControlService {

    private final Logger log = LoggerFactory.getLogger(PuntoControlServiceImpl.class);

    private final PuntoControlRepository puntoControlRepository;

    private final PuntoControlMapper puntoControlMapper;

    public PuntoControlServiceImpl(PuntoControlRepository puntoControlRepository, PuntoControlMapper puntoControlMapper) {
        this.puntoControlRepository = puntoControlRepository;
        this.puntoControlMapper = puntoControlMapper;
    }

    @Override
    public PuntoControlDTO save(PuntoControlDTO puntoControlDTO) {
        log.debug("Request to save PuntoControl : {}", puntoControlDTO);
        PuntoControl puntoControl = puntoControlMapper.toEntity(puntoControlDTO);
        puntoControl = puntoControlRepository.save(puntoControl);
        return puntoControlMapper.toDto(puntoControl);
    }

    @Override
    public PuntoControlDTO update(PuntoControlDTO puntoControlDTO) {
        log.debug("Request to update PuntoControl : {}", puntoControlDTO);
        PuntoControl puntoControl = puntoControlMapper.toEntity(puntoControlDTO);
        puntoControl = puntoControlRepository.save(puntoControl);
        return puntoControlMapper.toDto(puntoControl);
    }

    @Override
    public Optional<PuntoControlDTO> partialUpdate(PuntoControlDTO puntoControlDTO) {
        log.debug("Request to partially update PuntoControl : {}", puntoControlDTO);

        return puntoControlRepository
            .findById(puntoControlDTO.getId())
            .map(existingPuntoControl -> {
                puntoControlMapper.partialUpdate(existingPuntoControl, puntoControlDTO);

                return existingPuntoControl;
            })
            .map(puntoControlRepository::save)
            .map(puntoControlMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PuntoControlDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PuntoControls");
        return puntoControlRepository.findAll(pageable).map(puntoControlMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PuntoControlDTO> findOne(Long id) {
        log.debug("Request to get PuntoControl : {}", id);
        return puntoControlRepository.findById(id).map(puntoControlMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PuntoControl : {}", id);
        puntoControlRepository.deleteById(id);
    }
}
