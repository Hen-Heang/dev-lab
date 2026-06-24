package com.henheang.concurrency.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * EXECUTORSERVICE — don't create threads by hand; submit TASKS to a POOL.
 *
 * Creating a `new Thread()` per task is wasteful and unbounded. A thread pool
 * reuses a fixed set of threads and queues extra work — the standard approach.
 *
 *   Runnable  -> returns nothing            -> submit() gives Future<?>
 *   Callable  -> returns a value / throws   -> submit() gives Future<T>
 *   future.get() blocks until the result is ready.
 */
public class ExecutorServiceDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // pool of 3 reusable threads
        ExecutorService pool = Executors.newFixedThreadPool(3);

        try {
            System.out.println("== submit a Runnable (fire-and-forget) ==");
            pool.submit(() ->
                    System.out.println("  ran on " + Thread.currentThread().getName()));

            System.out.println("\n== submit a Callable, get its result ==");
            Future<Integer> future = pool.submit(() -> {
                Thread.sleep(100);          // simulate work
                return 6 * 7;
            });
            System.out.println("  doing other work while it computes...");
            System.out.println("  result = " + future.get());  // blocks until ready

            System.out.println("\n== invokeAll: run many Callables, collect all results ==");
            List<Callable<Integer>> tasks = new ArrayList<>();
            for (int n = 1; n <= 5; n++) {
                final int v = n;
                tasks.add(() -> {
                    Thread.sleep(50);
                    return v * v;            // square
                });
            }
            List<Future<Integer>> results = pool.invokeAll(tasks); // waits for all
            for (Future<Integer> f : results) {
                System.out.print("  " + f.get());
            }
            System.out.println("  (squares of 1..5)");

        } finally {
            // ALWAYS shut the pool down or the JVM won't exit (non-daemon threads)
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
            System.out.println("\npool shut down cleanly.");
        }

        /*
         * 🔧 PRACTICE IDEAS
         *  - Swap newFixedThreadPool(3) for newVirtualThreadPerTaskExecutor()
         *    (Java 21) and submit 10,000 tasks — virtual threads handle it easily.
         *  - Submit a Callable that throws; observe the exception surface at future.get().
         */
    }
}
