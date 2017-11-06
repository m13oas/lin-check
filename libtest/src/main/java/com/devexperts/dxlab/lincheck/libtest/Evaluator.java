package com.devexperts.dxlab.lincheck.libtest;

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

/**
 * Created by alexander on 08.02.17.
 */
public class Evaluator {

    private volatile int value;

    public Evaluator(int startvalue){
        value = startvalue;
    }

    public void addThree2Times(){
        value += 3;
        value += 3;
    }

    public void multThree(){
        value *= 3;
    }

    public int getValue(){
        return value;
    }

}