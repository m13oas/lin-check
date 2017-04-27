package com.devexperts.dxlab.lincheck.tests.counter;

/*
 * #%L
 * libtest
 * %%
 * Copyright (C) 2015 - 2017 Devexperts, LLC
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.devexperts.dxlab.lincheck.LinChecker;
import com.devexperts.dxlab.lincheck.annotations.CTest;
import com.devexperts.dxlab.lincheck.annotations.Operation;
import com.devexperts.dxlab.lincheck.annotations.Reset;
import com.devexperts.dxlab.lincheck.libtest.counter.SimpleWrongCounter1;
import org.junit.Test;

/**
 * Created by alexander on 26.02.17.
 */
@CTest(iterations = 50, actorsPerThread = {"2:5", "2:5"})
public class SimpleWrongCounterTest1 {
    private SimpleWrongCounter1 counter;


    @Reset
    public void reload() throws Exception {
        counter = new SimpleWrongCounter1();
    }


    //    @Suspendable
    @Operation
    public Integer incAndGet() {
//        StrategyHolder.getCurrentStrategy().onSharedVariableRead(0);
        return counter.incrementAndGet();
    }

    @Test
    public void test() throws Exception {
        LinChecker.check(this);
    }
}
