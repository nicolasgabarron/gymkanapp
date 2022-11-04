package com.nicogbdev.gymkanapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nicogbdev.gymkanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipoDTO.class);
        EquipoDTO equipoDTO1 = new EquipoDTO();
        equipoDTO1.setId(1L);
        EquipoDTO equipoDTO2 = new EquipoDTO();
        assertThat(equipoDTO1).isNotEqualTo(equipoDTO2);
        equipoDTO2.setId(equipoDTO1.getId());
        assertThat(equipoDTO1).isEqualTo(equipoDTO2);
        equipoDTO2.setId(2L);
        assertThat(equipoDTO1).isNotEqualTo(equipoDTO2);
        equipoDTO1.setId(null);
        assertThat(equipoDTO1).isNotEqualTo(equipoDTO2);
    }
}
