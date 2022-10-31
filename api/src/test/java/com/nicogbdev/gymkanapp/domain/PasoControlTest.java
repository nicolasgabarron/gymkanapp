package com.nicogbdev.gymkanapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nicogbdev.gymkanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasoControlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasoControl.class);
        PasoControl pasoControl1 = new PasoControl();
        pasoControl1.setId(1L);
        PasoControl pasoControl2 = new PasoControl();
        pasoControl2.setId(pasoControl1.getId());
        assertThat(pasoControl1).isEqualTo(pasoControl2);
        pasoControl2.setId(2L);
        assertThat(pasoControl1).isNotEqualTo(pasoControl2);
        pasoControl1.setId(null);
        assertThat(pasoControl1).isNotEqualTo(pasoControl2);
    }
}
