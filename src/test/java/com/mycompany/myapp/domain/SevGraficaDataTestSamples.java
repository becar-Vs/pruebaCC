package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SevGraficaDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SevGraficaData getSevGraficaDataSample1() {
        return new SevGraficaData().id(1L).idRow(1);
    }

    public static SevGraficaData getSevGraficaDataSample2() {
        return new SevGraficaData().id(2L).idRow(2);
    }

    public static SevGraficaData getSevGraficaDataRandomSampleGenerator() {
        return new SevGraficaData().id(longCount.incrementAndGet()).idRow(intCount.incrementAndGet());
    }
}
