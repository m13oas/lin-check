package me.aevd.lintesting.util;

import java.util.Arrays;

public class Actor {
    public int ind;
    public int method;
    public Object[] args;

    public Actor(int method, int ind) {
        this.method = method;
        this.ind = ind;
    }

    public Actor(int ind, int method, Object... args) {
        this.ind = ind;
        this.method = method;
        this.args = args;
    }

    @Override
    public String toString() {
        return "Actor" + ind +
                "{" + method +
                " (" + Arrays.toString(args) +
                ")}";
    }
}
