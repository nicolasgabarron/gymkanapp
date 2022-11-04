package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.PasoControl;
import com.nicogbdev.gymkanapp.repository.PasoControlRepository;
import com.nicogbdev.gymkanapp.service.PasoControlService;
import com.nicogbdev.gymkanapp.service.dto.PasoControlDTO;
import com.nicogbdev.gymkanapp.service.mapper.PasoControlMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PasoControl}.
 */
@Service
@Transactional
public class PasoControlServiceImpl implements PasoControlService {

    private final Logger log = LoggerFactory.getLogger(PasoControlServiceImpl.class);

    private final PasoControlRepository pasoControlRepository;

    private final PasoControlMapper pasoControlMapper;

    public PasoControlServiceImpl(PasoControlRepository pasoControlRepository, PasoControlMapper pasoControlMapper) {
        this.pasoControlRepository = pasoControlRepository;
        this.pasoControlMapper = pasoControlMapper;
    }

    @Override
    public PasoControlDTO save(PasoControlDTO pasoControlDTO) {
        log.debug("Request to save PasoControl : {}", pasoControlDTO);
        PasoControl pasoControl = pasoControlMapper.toEntity(pasoControlDTO);
        pasoControl = pasoControlRepository.save(pasoControl);
        return pasoControlMapper.toDto(pasoControl);
    }

    @Override
    public PasoControlDTO update(PasoControlDTO pasoControlDTO) {
        log.debug("Request to update PasoControl : {}", pasoControlDTO);
        PasoControl pasoControl = pasoControlMapper.toEntity(pasoControlDTO);
        pasoControl = pasoControlRepository.save(pasoControl);
        return pasoControlMapper.toDto(pasoControl);
    }

    @Override
    public Optional<PasoControlDTO> partialUpdate(PasoControlDTO pasoControlDTO) {
        log.debug("Request to partially update PasoControl : {}", pasoControlDTO);

        return pasoControlRepository
            .findById(pasoControlDTO.getId())
            .map(existingPasoControl -> {
                pasoControlMapper.partialUpdate(existingPasoControl, pasoControlDTO);

                return existingPasoControl;
            })
            .map(pasoControlRepository::save)
            .map(pasoControlMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PasoControlDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PasoControls");
        return pasoControlRepository.findAll(pageable).map(pasoControlMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PasoControlDTO> findOne(Long id) {
        log.debug("Request to get PasoControl : {}", id);
        return pasoControlRepository.findById(id).map(pasoControlMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PasoControl : {}", id);
        pasoControlRepository.deleteById(id);
    }
}
