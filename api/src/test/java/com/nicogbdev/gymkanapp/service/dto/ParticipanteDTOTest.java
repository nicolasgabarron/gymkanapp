package com.nicogbdev.gymkanapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nicogbdev.gymkanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParticipanteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipanteDTO.class);
        ParticipanteDTO participanteDTO1 = new ParticipanteDTO();
        participanteDTO1.setId(1L);
        ParticipanteDTO participanteDTO2 = new ParticipanteDTO();
        assertThat(participanteDTO1).isNotEqualTo(participanteDTO2);
        participanteDTO2.setId(participanteDTO1.getId());
        assertThat(participanteDTO1).isEqualTo(participanteDTO2);
        participanteDTO2.setId(2L);
        assertThat(participanteDTO1).isNotEqualTo(participanteDTO2);
        participanteDTO1.setId(null);
        assertThat(participanteDTO1).isNotEqualTo(participanteDTO2);
    }
}
