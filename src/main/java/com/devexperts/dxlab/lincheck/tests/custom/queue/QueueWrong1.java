package com.devexperts.dxlab.lincheck.tests.custom.queue;

import java.util.Arrays;

public class QueueWrong1 implements Queue {
    private int indGet;
    private int indPut;
    private int countElements;

    private int[] items;

    private int inc(int i) {
        return (++i == items.length ? 0 : i);
    }

    public QueueWrong1(int capacity) {
        items = new int[capacity];

        indPut = 0;
        indGet = 0;
        countElements = 0;

    }

    @Override
    public void put(int x) throws QueueFullException {
        if (countElements == items.length) {
            throw new QueueFullException();
        }
        items[indPut] = x;
        indPut = inc(indPut);
        countElements++;
    }

    @Override
    public int get() throws QueueEmptyException {
        if (countElements == 0) {
            throw new QueueEmptyException();
        }
        int ret = items[indGet];
        indGet = inc(indGet);
        countElements--;
        return ret;
    }

    @Override
    public String toString() {
        return "QueueWithoutAnySync{" +
                "indGet=" + indGet +
                ", indPut=" + indPut +
                ", countElements=" + countElements +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}