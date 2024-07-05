package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SevGraficaTestSamples.*;
import static com.mycompany.myapp.domain.SevProcesoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SevGraficaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SevGrafica.class);
        SevGrafica sevGrafica1 = getSevGraficaSample1();
        SevGrafica sevGrafica2 = new SevGrafica();
        assertThat(sevGrafica1).isNotEqualTo(sevGrafica2);

        sevGrafica2.setId(sevGrafica1.getId());
        assertThat(sevGrafica1).isEqualTo(sevGrafica2);

        sevGrafica2 = getSevGraficaSample2();
        assertThat(sevGrafica1).isNotEqualTo(sevGrafica2);
    }

    @Test
    void procesoTest() {
        SevGrafica sevGrafica = getSevGraficaRandomSampleGenerator();
        SevProceso sevProcesoBack = getSevProcesoRandomSampleGenerator();

        sevGrafica.setProceso(sevProcesoBack);
        assertThat(sevGrafica.getProceso()).isEqualTo(sevProcesoBack);

        sevGrafica.proceso(null);
        assertThat(sevGrafica.getProceso()).isNull();
    }
}
