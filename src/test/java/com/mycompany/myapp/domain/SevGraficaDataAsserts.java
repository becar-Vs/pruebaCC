package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SevGraficaDataAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSevGraficaDataAllPropertiesEquals(SevGraficaData expected, SevGraficaData actual) {
        assertSevGraficaDataAutoGeneratedPropertiesEquals(expected, actual);
        assertSevGraficaDataAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSevGraficaDataAllUpdatablePropertiesEquals(SevGraficaData expected, SevGraficaData actual) {
        assertSevGraficaDataUpdatableFieldsEquals(expected, actual);
        assertSevGraficaDataUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSevGraficaDataAutoGeneratedPropertiesEquals(SevGraficaData expected, SevGraficaData actual) {
        assertThat(expected)
            .as("Verify SevGraficaData auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSevGraficaDataUpdatableFieldsEquals(SevGraficaData expected, SevGraficaData actual) {
        assertThat(expected)
            .as("Verify SevGraficaData relevant properties")
            .satisfies(e -> assertThat(e.getIdRow()).as("check idRow").isEqualTo(actual.getIdRow()))
            .satisfies(e -> assertThat(e.getFechaObjetivo()).as("check fechaObjetivo").isEqualTo(actual.getFechaObjetivo()))
            .satisfies(e -> assertThat(e.getValorObjetivo()).as("check valorObjetivo").isEqualTo(actual.getValorObjetivo()))
            .satisfies(e -> assertThat(e.getValorLogrado()).as("check valorLogrado").isEqualTo(actual.getValorLogrado()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSevGraficaDataUpdatableRelationshipsEquals(SevGraficaData expected, SevGraficaData actual) {
        assertThat(expected)
            .as("Verify SevGraficaData relationships")
            .satisfies(e -> assertThat(e.getGrafica()).as("check grafica").isEqualTo(actual.getGrafica()));
    }
}
