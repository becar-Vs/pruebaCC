package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SevProcesoTestSamples.*;
import static com.mycompany.myapp.domain.SevUnidadNegocioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SevProcesoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SevProceso.class);
        SevProceso sevProceso1 = getSevProcesoSample1();
        SevProceso sevProceso2 = new SevProceso();
        assertThat(sevProceso1).isNotEqualTo(sevProceso2);

        sevProceso2.setId(sevProceso1.getId());
        assertThat(sevProceso1).isEqualTo(sevProceso2);

        sevProceso2 = getSevProcesoSample2();
        assertThat(sevProceso1).isNotEqualTo(sevProceso2);
    }

    @Test
    void unidadNegocioTest() {
        SevProceso sevProceso = getSevProcesoRandomSampleGenerator();
        SevUnidadNegocio sevUnidadNegocioBack = getSevUnidadNegocioRandomSampleGenerator();

        sevProceso.setUnidadNegocio(sevUnidadNegocioBack);
        assertThat(sevProceso.getUnidadNegocio()).isEqualTo(sevUnidadNegocioBack);

        sevProceso.unidadNegocio(null);
        assertThat(sevProceso.getUnidadNegocio()).isNull();
    }
}
