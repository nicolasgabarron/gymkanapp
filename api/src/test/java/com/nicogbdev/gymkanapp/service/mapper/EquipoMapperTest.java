package com.nicogbdev.gymkanapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipoMapperTest {

    private EquipoMapper equipoMapper;

    @BeforeEach
    public void setUp() {
        equipoMapper = new EquipoMapperImpl();
    }
}
