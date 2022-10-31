package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.Participante;
import com.nicogbdev.gymkanapp.repository.ParticipanteRepository;
import com.nicogbdev.gymkanapp.service.ParticipanteService;
import com.nicogbdev.gymkanapp.service.dto.ParticipanteDTO;
import com.nicogbdev.gymkanapp.service.mapper.ParticipanteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final ParticipanteMapper participanteMapper;

    public ParticipanteServiceImpl(ParticipanteRepository participanteRepository, ParticipanteMapper participanteMapper) {
        this.participanteRepository = participanteRepository;
        this.participanteMapper = participanteMapper;
    }

    @Override
    public ParticipanteDTO save(ParticipanteDTO participanteDTO) {
        log.debug("Request to save Participante : {}", participanteDTO);
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
