package com.henheang.concurrency.synchronization;

import java.util.concurrent.atomic.AtomicLong;

/**
 * SYNCHRONIZATION — why shared mutable state breaks under threads, and 3 fixes.
 *
 * `count++` looks atomic but is really READ → ADD → WRITE (3 steps). Two threads
 * can read the same value and both write back the same +1, losing increments.
 *
 * We run 4 threads × 100,000 increments = expected 400,000, then compare:
 *   1. unsafe  (plain long)        -> usually WRONG (less than expected)
 *   2. synchronized                -> correct (mutual exclusion via a lock)
 *   3. AtomicLong                  -> correct (lock-free CAS, fastest here)
 */
public class RaceConditionDemo {

    static final int THREADS = 4;
    static final int PER_THREAD = 100_000;
    static final long EXPECTED = (long) THREADS * PER_THREAD;

    // 1. UNSAFE — no protection
    static class UnsafeCounter {
        long count = 0;
        void increment() { count++; }
    }

    // 2. SYNCHRONIZED — only one thread in the method at a time
    static class SyncCounter {
        long count = 0;
        synchronized void increment() { count++; }
    }

    // 3. ATOMIC — hardware compare-and-swap, no explicit lock
    static class AtomicCounter {
        final AtomicLong count = new AtomicLong();
        void increment() { count.incrementAndGet(); }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("expected total = " + EXPECTED + " (" + THREADS + " x " + PER_THREAD + ")\n");

        UnsafeCounter unsafe = new UnsafeCounter();
        runWorkers(unsafe::increment);
        report("1. unsafe (plain long)   ", unsafe.count);

        SyncCounter sync = new SyncCounter();
        runWorkers(sync::increment);
        report("2. synchronized          ", sync.count);

        AtomicCounter atomic = new AtomicCounter();
        runWorkers(atomic::increment);
        report("3. AtomicLong            ", atomic.count.get());

        System.out.println("\nLesson: shared mutable state needs synchronized OR atomic types.");
    }

    /** Run the given increment action on THREADS threads, PER_THREAD times each. */
    private static void runWorkers(Runnable increment) throws InterruptedException {
        Thread[] workers = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < PER_THREAD; j++) {
                    increment.run();
                }
            });
        }
        for (Thread w : workers) w.start();
        for (Thread w : workers) w.join();   // wait for all before reading the result
    }

    private static void report(String label, long actual) {
        String ok = (actual == EXPECTED) ? "OK  " : "LOST UPDATES";
        System.out.printf("%s -> %,d  [%s]%n", label, actual, ok);
    }

    /*
     * 🔧 PRACTICE IDEAS
     *  - Run a few times: the unsafe number changes each run (a real race).
     *  - Replace synchronized method with a synchronized(this){...} block.
     *  - Try java.util.concurrent.locks.ReentrantLock (see saasolv/ex09 ULID).
     */
}
