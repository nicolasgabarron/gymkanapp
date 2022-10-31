package com.nicogbdev.gymkanapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nicogbdev.gymkanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PuntoControlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PuntoControl.class);
        PuntoControl puntoControl1 = new PuntoControl();
        puntoControl1.setId(1L);
        PuntoControl puntoControl2 = new PuntoControl();
        puntoControl2.setId(puntoControl1.getId());
        assertThat(puntoControl1).isEqualTo(puntoControl2);
        puntoControl2.setId(2L);
        assertThat(puntoControl1).isNotEqualTo(puntoControl2);
        puntoControl1.setId(null);
        assertThat(puntoControl1).isNotEqualTo(puntoControl2);
    }
}
