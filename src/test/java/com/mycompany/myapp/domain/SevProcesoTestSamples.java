package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SevProcesoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SevProceso getSevProcesoSample1() {
        return new SevProceso().id(1L).idProceso(1).nombre("nombre1").fkIdResponsable(1);
    }

    public static SevProceso getSevProcesoSample2() {
        return new SevProceso().id(2L).idProceso(2).nombre("nombre2").fkIdResponsable(2);
    }

    public static SevProceso getSevProcesoRandomSampleGenerator() {
        return new SevProceso()
            .id(longCount.incrementAndGet())
            .idProceso(intCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .fkIdResponsable(intCount.incrementAndGet());
    }
}
