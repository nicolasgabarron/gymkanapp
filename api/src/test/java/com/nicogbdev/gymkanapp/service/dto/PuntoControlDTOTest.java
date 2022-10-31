package com.nicogbdev.gymkanapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nicogbdev.gymkanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PuntoControlDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PuntoControlDTO.class);
        PuntoControlDTO puntoControlDTO1 = new PuntoControlDTO();
        puntoControlDTO1.setId(1L);
        PuntoControlDTO puntoControlDTO2 = new PuntoControlDTO();
        assertThat(puntoControlDTO1).isNotEqualTo(puntoControlDTO2);
        puntoControlDTO2.setId(puntoControlDTO1.getId());
        assertThat(puntoControlDTO1).isEqualTo(puntoControlDTO2);
        puntoControlDTO2.setId(2L);
        assertThat(puntoControlDTO1).isNotEqualTo(puntoControlDTO2);
        puntoControlDTO1.setId(null);
        assertThat(puntoControlDTO1).isNotEqualTo(puntoControlDTO2);
    }
}
