package com.devexperts.dxlab.lincheck.tests.juc.blocking_queue;

import com.devexperts.dxlab.lincheck.CheckerAnnotatedASM;
import com.devexperts.dxlab.lincheck.annotations.ActorAnn;
import com.devexperts.dxlab.lincheck.annotations.CTest;
import com.devexperts.dxlab.lincheck.annotations.Immutable;
import com.devexperts.dxlab.lincheck.annotations.Reload;
import com.devexperts.dxlab.lincheck.util.Result;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.assertTrue;


@CTest(iter = 200, actorsPerThread = {"1:3", "1:3"})
@CTest(iter = 200, actorsPerThread = {"1:3", "1:3", "1:3"})
public class BlockingQueueTest2 {
    public BlockingQueue<Integer> q;

    @Reload
    public void reload() {
        q = new LinkedBlockingDeque<>();
    }

    @ActorAnn(args = {"1:10"})
    public void add(Result res, Object[] args) throws Exception {
        Integer value = (Integer) args[0];

        res.setValue(q.add(value));
    }

    @Immutable
    @ActorAnn(args = {})
    public void element(Result res, Object[] args)  throws Exception  {
        res.setValue(q.element());
    }

    @ActorAnn(args = {})
    public void remove(Result res, Object[] args) throws Exception {
        res.setValue(q.remove());
    }

    @ActorAnn(args = {})
    public void poll(Result res, Object[] args) throws Exception {
        res.setValue(q.poll());
    }


    @Test
    public void test() throws Exception {
        assertTrue(CheckerAnnotatedASM.check(new BlockingQueueTest2()));
    }
}

