package com.nicogbdev.gymkanapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PuntoControlMapperTest {

    private PuntoControlMapper puntoControlMapper;

    @BeforeEach
    public void setUp() {
        puntoControlMapper = new PuntoControlMapperImpl();
    }
}
