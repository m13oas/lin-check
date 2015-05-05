package com.devexperts.dxlab.lincheck.tests.custom.counter;

public class CounterSynchronized implements Counter {
    private int c;

    public CounterSynchronized() {
        c = 0;
    }

    @Override
    public synchronized int incrementAndGet() {
        c++;
        return c;
    }
}