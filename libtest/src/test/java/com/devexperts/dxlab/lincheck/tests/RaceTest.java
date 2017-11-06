package com.devexperts.dxlab.lincheck.tests;

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
import com.devexperts.dxlab.lincheck.libtest.Race;
import org.junit.Test;

/**
 * Created by alexander on 09.02.17.
 */
@CTest(iterations = 30, actorsPerThread = {"1:2", "1:2"})
public class RaceTest {

    private Race race;

    @Reset
    public void reload(){
        race = new Race();
    }

    @Operation
    public int dec(){
        return race.dec();
    }

    @Operation
    public int inc(){
        return race.add2();
    }

    @Test(timeout = 1000000)
    public void test() {
        LinChecker.check(this);
    }
}