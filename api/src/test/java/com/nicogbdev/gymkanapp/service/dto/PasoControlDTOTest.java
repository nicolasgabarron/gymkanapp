package com.nicogbdev.gymkanapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nicogbdev.gymkanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasoControlDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasoControlDTO.class);
        PasoControlDTO pasoControlDTO1 = new PasoControlDTO();
        pasoControlDTO1.setId(1L);
        PasoControlDTO pasoControlDTO2 = new PasoControlDTO();
        assertThat(pasoControlDTO1).isNotEqualTo(pasoControlDTO2);
        pasoControlDTO2.setId(pasoControlDTO1.getId());
        assertThat(pasoControlDTO1).isEqualTo(pasoControlDTO2);
        pasoControlDTO2.setId(2L);
        assertThat(pasoControlDTO1).isNotEqualTo(pasoControlDTO2);
        pasoControlDTO1.setId(null);
        assertThat(pasoControlDTO1).isNotEqualTo(pasoControlDTO2);
    }
}
