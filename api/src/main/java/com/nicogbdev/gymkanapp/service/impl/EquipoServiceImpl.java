package com.nicogbdev.gymkanapp.service.impl;

import com.nicogbdev.gymkanapp.domain.Equipo;
import com.nicogbdev.gymkanapp.repository.EquipoRepository;
import com.nicogbdev.gymkanapp.service.EquipoService;
import com.nicogbdev.gymkanapp.service.dto.EquipoDTO;
import com.nicogbdev.gymkanapp.service.mapper.EquipoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Equipo}.
 */
@Service
@Transactional
public class EquipoServiceImpl implements EquipoService {

    private final Logger log = LoggerFactory.getLogger(EquipoServiceImpl.class);

    private final EquipoRepository equipoRepository;

    private final EquipoMapper equipoMapper;

    public EquipoServiceImpl(EquipoRepository equipoRepository, EquipoMapper equipoMapper) {
        this.equipoRepository = equipoRepository;
        this.equipoMapper = equipoMapper;
    }

    @Override
    public EquipoDTO save(EquipoDTO equipoDTO) {
        log.debug("Request to save Equipo : {}", equipoDTO);
        Equipo equipo = equipoMapper.toEntity(equipoDTO);
        equipo = equipoRepository.save(equipo);
        return equipoMapper.toDto(equipo);
    }

    @Override
    public EquipoDTO update(EquipoDTO equipoDTO) {
        log.debug("Request to update Equipo : {}", equipoDTO);
        Equipo equipo = equipoMapper.toEntity(equipoDTO);
        equipo = equipoRepository.save(equipo);
        return equipoMapper.toDto(equipo);
    }

    @Override
    public Optional<EquipoDTO> partialUpdate(EquipoDTO equipoDTO) {
        log.debug("Request to partially update Equipo : {}", equipoDTO);

        return equipoRepository
            .findById(equipoDTO.getId())
            .map(existingEquipo -> {
                equipoMapper.partialUpdate(existingEquipo, equipoDTO);

                return existingEquipo;
            })
            .map(equipoRepository::save)
            .map(equipoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EquipoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Equipos");
        return equipoRepository.findAll(pageable).map(equipoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipoDTO> findOne(Long id) {
        log.debug("Request to get Equipo : {}", id);
        return equipoRepository.findById(id).map(equipoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equipo : {}", id);
        equipoRepository.deleteById(id);
    }
}
