package me.aevd.lintesting;

import me.aevd.lintesting.transfer.AccountsSynchronized;
import me.aevd.lintesting.util.Actor;
import me.aevd.lintesting.util.Caller;
import me.aevd.lintesting.util.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AccountsTester {
    Caller caller;
    int COUNT_ITER = 1000;
    int COUNT_THREADS = 3;

    public AccountsTester(Caller caller) {
        this.caller = caller;
    }

    private void genHelper(List<Actor[]> result, Actor[] out, int countUsed, int countActors, Actor[][] actors, int[] inds) {
        if (countActors == countUsed) {
            result.add(out.clone());
        } else {
            for (int i = 0; i < actors.length; i++) {
                if (actors[i].length != inds[i]) {
                    Actor call = actors[i][inds[i]];
                    inds[i]++;
                    out[countUsed] = call;
                    countUsed++;

                    genHelper(result, out, countUsed, countActors, actors, inds);

                    countUsed--;
                    inds[i]--;
                }
            }
        }
    }

    private Actor[][] genPermutations(Actor[][] actors, int countActors) {
        Actor[] out = new Actor[countActors];
        int[] inds = new int[COUNT_THREADS];
        List<Actor[]> perms = new ArrayList<>();
        genHelper(perms, out, 0, countActors, actors, inds);

        return perms.toArray(new Actor[perms.size()][]);
    }

    private Result[][] linearExecution(Actor[][] perms) {
        final Result[][] results = new Result[perms.length][];

        for (int i = 0; i < perms.length; i++) {
            Actor[] localActors = perms[i];
            Result[] localResult = new Result[localActors.length];

            caller.reload();

            for (int j = 0; j < localActors.length; j++) {
                localResult[localActors[j].ind] = caller.call(localActors[j].method, localActors[j].args);
            }

            results[i] = localResult;
        }
        return results;
    }

    public void test() {
        Random random = new Random();

        for (int iter = 0; iter < COUNT_ITER; iter++) {
            Thread[] threads = new Thread[COUNT_THREADS];

            Actor[][] actors = caller.generateActors(COUNT_THREADS);
            int countActors = 0;
            for (Actor[] actor : actors) {
                countActors += actor.length;
            }

            for (int i = 0; i < actors.length; i++) {
                System.out.println(Arrays.asList(actors[i]));
            }
            System.out.println();


            Actor[][] perms = genPermutations(actors, countActors);

            for (int i = 0; i < perms.length; i++) {
                System.out.println(Arrays.asList(perms[i]));
            }
            System.out.println();


            Result[][] lin = linearExecution(perms);
            System.out.println();
            for (int i = 0; i < lin.length; i++) {
                System.out.println(Arrays.asList(lin[i]));
            }
            System.out.println();

            int[] countLin = new int[lin.length];



            boolean errorFound = false;
            for (int threads_num = 0; threads_num < 10000; threads_num++) {
                if (threads_num % 1000 == 0) {
                    System.out.printf("%d : ", threads_num);
                    for (int i = 0; i < countLin.length; i++) {
                        System.out.printf("%d ", countLin[i]);
                    }
                    System.out.println();
                }
                final Result[] results = new Result[countActors];
                caller.reload();
                final CyclicBarrier barrier = new CyclicBarrier(COUNT_THREADS);
                for (int i = 0; i < COUNT_THREADS; i++) {
                    final Actor[] threadActors = actors[i];
                    threads[i] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                barrier.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (BrokenBarrierException e) {
                                e.printStackTrace();
                            }
                            for (Actor actor : threadActors) {
                                results[actor.ind] = caller.call(actor.method, actor.args);
                            }
                        }
                    });
                }

                for (Thread thread : threads) {
                    thread.start();
                }


                for (Thread thread : threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                boolean correct = false;
                for (int i = 0; i < lin.length; i++) {
                    if (Arrays.equals(lin[i], results)) {
                        correct = true;
                        countLin[i]++;
                        break;
                    }
                }
                if (correct) {
//                System.out.println("ok");
                } else {
                    System.out.println("error!");
                    System.out.println(Arrays.asList(results));
                    errorFound = true;
                    break;
                }
//                System.out.println(Arrays.asList(results));
            }
            for (int i = 0; i < countLin.length; i++) {
                System.out.printf("%d ", countLin[i]);
            }
            System.out.println();
            if (errorFound) {
                break;
            }
        }
        System.out.println("finish");
    }
}
