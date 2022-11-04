package com.nicogbdev.gymkanapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasoControlMapperTest {

    private PasoControlMapper pasoControlMapper;

    @BeforeEach
    public void setUp() {
        pasoControlMapper = new PasoControlMapperImpl();
    }
}
