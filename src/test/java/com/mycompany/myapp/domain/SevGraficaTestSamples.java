package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SevGraficaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SevGrafica getSevGraficaSample1() {
        return new SevGrafica().id(1L).idGrafica(1).nombre("nombre1").fkIdResponsable(1);
    }

    public static SevGrafica getSevGraficaSample2() {
        return new SevGrafica().id(2L).idGrafica(2).nombre("nombre2").fkIdResponsable(2);
    }

    public static SevGrafica getSevGraficaRandomSampleGenerator() {
        return new SevGrafica()
            .id(longCount.incrementAndGet())
            .idGrafica(intCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .fkIdResponsable(intCount.incrementAndGet());
    }
}
