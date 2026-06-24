package com.henheang.concurrency.basics;

/**
 * THREAD BASICS — the two ways to make a thread, start() vs run(), join(), sleep().
 *
 * A thread is an independent path of execution. The JVM can run many at once
 * (on multiple CPU cores), so output order is NOT guaranteed — that's the point.
 */
public class ThreadBasicsDemo {

    // Way 1: extend Thread (simple, but you "use up" your one allowed superclass)
    static class CountingThread extends Thread {
        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println("  [" + getName() + "] count " + i);
            }
        }
    }

    // Way 2: implement Runnable (preferred — separates the task from the thread)
    static class PrintTask implements Runnable {
        private final String label;
        PrintTask(String label) { this.label = label; }
        @Override
        public void run() {
            System.out.println("  [" + Thread.currentThread().getName() + "] task " + label);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main thread = " + Thread.currentThread().getName());

        System.out.println("\n== start() vs run() ==");
        CountingThread t = new CountingThread();
        t.run();   // ❌ runs on the MAIN thread (just a normal method call)
        t = new CountingThread();
        t.start(); // ✅ runs on a NEW thread
        t.join();  // wait for it to finish before continuing

        System.out.println("\n== Runnable (preferred) ==");
        Thread r1 = new Thread(new PrintTask("A"), "worker-1");
        Thread r2 = new Thread(new PrintTask("B"), "worker-2");
        r1.start();
        r2.start();
        r1.join();
        r2.join();

        System.out.println("\n== Runnable as a lambda (modern) ==");
        Thread r3 = new Thread(() ->
                System.out.println("  [" + Thread.currentThread().getName() + "] hello from lambda"),
                "worker-3");
        r3.start();
        r3.join();

        System.out.println("\n== join() makes main WAIT ==");
        Thread slow = new Thread(() -> {
            try {
                Thread.sleep(200);            // simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("  slow task done");
        }, "slow");
        slow.start();
        System.out.println("  main is waiting for slow...");
        slow.join();                          // without this, main could finish first
        System.out.println("  main continues after slow finished");

        System.out.println("\nall done on main thread.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Remove the join() calls and run several times — watch the order change.
         *  - Make slow a daemon thread (slow.setDaemon(true)) and skip join — see it
         *    get killed when main exits.
         */
    }
}
