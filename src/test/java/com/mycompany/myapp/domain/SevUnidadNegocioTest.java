package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SevUnidadNegocioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SevUnidadNegocioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SevUnidadNegocio.class);
        SevUnidadNegocio sevUnidadNegocio1 = getSevUnidadNegocioSample1();
        SevUnidadNegocio sevUnidadNegocio2 = new SevUnidadNegocio();
        assertThat(sevUnidadNegocio1).isNotEqualTo(sevUnidadNegocio2);

        sevUnidadNegocio2.setId(sevUnidadNegocio1.getId());
        assertThat(sevUnidadNegocio1).isEqualTo(sevUnidadNegocio2);

        sevUnidadNegocio2 = getSevUnidadNegocioSample2();
        assertThat(sevUnidadNegocio1).isNotEqualTo(sevUnidadNegocio2);
    }
}
