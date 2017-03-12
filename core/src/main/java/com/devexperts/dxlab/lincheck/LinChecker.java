package com.devexperts.dxlab.lincheck;

/*
 * #%L
 * core
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

import com.devexperts.dxlab.lincheck.report.Reporter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;

/**
 * TODO avoid executions without write operations
 */
public class LinChecker {
    private static final int MAX_WAIT = 1000;

    // TODO a lot of structure changes: why? Let's remove such changes.
    // TODO They can introduce problems with merging and history.

    private final Random random = new Random(0);
    private final Object testInstance;
    private final List<CTestConfiguration> testConfigurations;
    private final CTestStructure testStructure;
    private Reporter reporter;

    private LinChecker(Object testInstance) {
        this.testInstance = testInstance;
        Class<?> testClass = testInstance.getClass();
        this.testConfigurations = CTestConfiguration.getFromTestClass(testClass);
        this.testStructure = CTestStructure.getFromTestClass(testClass);
    }

    public static void check(Object testInstance) throws AssertionError{
        new LinChecker(testInstance).check();
    }

    /**
     * @throws AssertionError if atomicity violation is detected
     */
    private void check() throws AssertionError {
        reporter = new Reporter(testInstance.getClass().getSimpleName(), "RandomInvocation");
        testConfigurations.forEach((testConfiguration) -> {
            try {
                reporter.setConfiguratuon(testConfiguration);
                checkImpl(testConfiguration);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private List<Actor> generateActorsForThread(CTestConfiguration.TestThreadConfiguration threadCfg) {
        int actorsInThread = threadCfg.minActors + random.nextInt(threadCfg.maxActors - threadCfg.minActors + 1);
        return random.ints(actorsInThread, 0, testStructure.getActorGenerators().size()) // random indexes
            .mapToObj(i -> testStructure.getActorGenerators().get(i)) // random actor generators
            .map(ActorGenerator::generate) // generate actors
            .collect(Collectors.toList()); // return as list
    }

    private List<List<Actor>> generateActors(CTestConfiguration testConfiguration) {
        return testConfiguration.getThreadConfigurations().stream()
            .map(this::generateActorsForThread)
            .collect(Collectors.toList());
    }

    private List<List<Actor>> generateAllLinearizableExecutions(List<List<Actor>> actorsPerThread) {
        List<List<Actor>> executions = new ArrayList<>();
        generateLinearizableExecutions0(executions, actorsPerThread, new ArrayList<>(), new int[actorsPerThread.size()],
            actorsPerThread.stream().mapToInt(List::size).sum());
        return executions;
    }

    private void generateLinearizableExecutions0(List<List<Actor>> executions, List<List<Actor>> actorsPerThread,
        ArrayList<Actor> currentExecution, int[] indexes, int length)
    {
        if (currentExecution.size() == length) {
            executions.add((List<Actor>) currentExecution.clone());
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            List<Actor> actors = actorsPerThread.get(i);
            if (indexes[i] == actors.size())
                continue;
            currentExecution.add(actors.get(indexes[i]));
            indexes[i]++;
            generateLinearizableExecutions0(executions, actorsPerThread, currentExecution, indexes, length);
            indexes[i]--;
            currentExecution.remove(currentExecution.size() - 1);
        }
    }

    private Set<List<List<Result>>> generateLinearizeResults(List<List<Actor>> actorsPerThread){
        return generateAllLinearizableExecutions(actorsPerThread).stream()
                .map(linEx -> {
                    List<Result> results = executeActors(linEx);
                    Map<Actor, Result> resultMap = new IdentityHashMap<>();
                    for (int i = 0; i < linEx.size(); i++) {
                        resultMap.put(linEx.get(i), results.get(i));
                    }
                    return actorsPerThread.stream()
                            .map(actors -> actors.stream()
                                    .map(resultMap::get)
                                    .collect(Collectors.toList())
                            ).collect(Collectors.toList());
                })
                .collect(Collectors.toSet());
    }

    private void checkImpl(CTestConfiguration testCfg) throws InterruptedException {
        // Fixed thread pool executor to run TestThreadExecution
        ExecutorService pool = Executors.newFixedThreadPool(testCfg.getThreads());
        try {
            System.out.println("Number iterations: " + testCfg.getIterations());
            System.out.println("Number invocations per iteration: " + testCfg.getInvocationsPerIteration() + "\n");
            // Reusable phaser
            final Phaser phaser = new Phaser(testCfg.getThreads());
            // Run iterations
            reporter.setCurrentTime();
            for (int iteration = 1; iteration <= testCfg.getIterations(); iteration++) {
                List<List<Actor>> actorsPerThread = generateActors(testCfg);
                System.out.println("for iteration №" + iteration + " generated algorithm:");
                actorsPerThread.forEach(System.out::println);

                // Create TestThreadExecution's
                List<TestThreadExecution> testThreadExecutions = actorsPerThread.stream()
                    .map(actors -> TestThreadExecutionGenerator.create(testInstance, phaser, actors, false))
                    .collect(Collectors.toList());
                // Generate all possible results
                Set<List<List<Result>>> possibleResultsSet = generateLinearizeResults(actorsPerThread);
                System.out.println("Linearizable results:");
                possibleResultsSet.forEach(possibleResults -> {
                    possibleResults.forEach(System.out::println);
                    System.out.println();
                });
                // Run invocations
                for (int invocation = 1; invocation <= testCfg.getInvocationsPerIteration(); invocation++) {
                    // Reset the state of test
                    invokeReset();
                    // Specify waits
                    int maxWait = (int) ((float) invocation * MAX_WAIT / testCfg.getInvocationsPerIteration()) + 1;
                    setWaits(actorsPerThread, testThreadExecutions, maxWait);
                    // Run multithreaded test and get operation results for each thread
                    List<List<Result>> results = pool.invokeAll(testThreadExecutions).stream() // get futures
                        .map(f -> {
                            try {
                                return Arrays.asList(f.get()); // wait and get results
                            } catch (InterruptedException | ExecutionException e) {
                                throw new IllegalStateException(e);
                            }
                        })
                        .collect(Collectors.toList()); // and store results as list
                    // Check correctness& Throw an AssertionError if current execution
                    // is not linearizable and log invalid execution
                    if (!possibleResultsSet.contains(results)) {
                        System.out.println("Iteration №" + iteration +" completed with number invocations = " +
                                testCfg.getInvocationsPerIteration());
                        StringBuilder result = new StringBuilder();
                        results.forEach(res -> result.append(res.toString()));
                        System.out.println("For invocation" + testCfg.getInvocationsPerIteration() + "result was " + result);
                        reporter.addFailedResult(iteration, invocation);
                        throw new AssertionError("Not linearizable execution detected, see log for details");
                    }
                }
                reporter.addCompletedResult(iteration, testCfg.getInvocationsPerIteration());
            }
        } finally {
            reporter.close();
            pool.shutdown();
        }
    }

    private Phaser SINGLE_THREAD_PHASER = new Phaser(1);

    private void setWaits(List<List<Actor>> actorsPerThread, List<TestThreadExecution> testThreadExecutions, int maxWait){
        for (int i = 0; i < testThreadExecutions.size(); i++) {
            TestThreadExecution ex = testThreadExecutions.get(i);
            ex.waits = random.ints(actorsPerThread.get(i).size() - 1, 0, maxWait).toArray();
        }
    }

    private List<Result> executeActors(List<Actor> actors) {
        invokeReset();
        return Arrays.asList(TestThreadExecutionGenerator.create(testInstance, SINGLE_THREAD_PHASER, actors, false).call());
    }

    private void invokeReset() {
        try {
            testStructure.getResetMethod().invoke(testInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to call method annotated with @Reset", e);
        }
    }
}