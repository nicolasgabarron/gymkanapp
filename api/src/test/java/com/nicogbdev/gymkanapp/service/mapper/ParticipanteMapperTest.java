package com.nicogbdev.gymkanapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParticipanteMapperTest {

    private ParticipanteMapper participanteMapper;

    @BeforeEach
    public void setUp() {
        participanteMapper = new ParticipanteMapperImpl();
    }
}
