package me.aevd.lintesting.util;

import java.util.Random;

public class ActorGenerator {
    private int methodId;
    private String name;
    private Interval[] rangeArgs;

    public ActorGenerator(int methodId, String name, Interval... rangeArgs) {
        this.methodId = methodId;
        this.name = name;
        this.rangeArgs = rangeArgs;
    }

    public Actor generate(int indActor) {
        Integer[] args = new Integer[rangeArgs.length];
        for (int i = 0; i < rangeArgs.length; i++) {
            args[i] = MyRandom.fromInterval(rangeArgs[i]);
        }

        Actor act = new Actor(indActor, methodId, args);
        act.methodName = name;
        return act;
    }
}
