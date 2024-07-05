package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SevUnidadNegocioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SevUnidadNegocio getSevUnidadNegocioSample1() {
        return new SevUnidadNegocio().id(1L).idUnidadNegocio(1).nombre("nombre1").fkIdResponsable(1);
    }

    public static SevUnidadNegocio getSevUnidadNegocioSample2() {
        return new SevUnidadNegocio().id(2L).idUnidadNegocio(2).nombre("nombre2").fkIdResponsable(2);
    }

    public static SevUnidadNegocio getSevUnidadNegocioRandomSampleGenerator() {
        return new SevUnidadNegocio()
            .id(longCount.incrementAndGet())
            .idUnidadNegocio(intCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .fkIdResponsable(intCount.incrementAndGet());
    }
}
