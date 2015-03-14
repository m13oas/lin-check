package me.aevd.lintesting;

import me.aevd.lintesting.queue.QueueWithoutAnySync;
import me.aevd.lintesting.util.Caller;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
//        Caller caller = new QueueCaller(AccountsSynchronized.class);

        Caller caller = new QueueCaller(QueueWithoutAnySync.class);
        Tester tester = new Tester(caller);
        tester.test();
    }
}

