package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SevGraficaDataTestSamples.*;
import static com.mycompany.myapp.domain.SevGraficaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SevGraficaDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SevGraficaData.class);
        SevGraficaData sevGraficaData1 = getSevGraficaDataSample1();
        SevGraficaData sevGraficaData2 = new SevGraficaData();
        assertThat(sevGraficaData1).isNotEqualTo(sevGraficaData2);

        sevGraficaData2.setId(sevGraficaData1.getId());
        assertThat(sevGraficaData1).isEqualTo(sevGraficaData2);

        sevGraficaData2 = getSevGraficaDataSample2();
        assertThat(sevGraficaData1).isNotEqualTo(sevGraficaData2);
    }

    @Test
    void graficaTest() {
        SevGraficaData sevGraficaData = getSevGraficaDataRandomSampleGenerator();
        SevGrafica sevGraficaBack = getSevGraficaRandomSampleGenerator();

        sevGraficaData.setGrafica(sevGraficaBack);
        assertThat(sevGraficaData.getGrafica()).isEqualTo(sevGraficaBack);

        sevGraficaData.grafica(null);
        assertThat(sevGraficaData.getGrafica()).isNull();
    }
}
