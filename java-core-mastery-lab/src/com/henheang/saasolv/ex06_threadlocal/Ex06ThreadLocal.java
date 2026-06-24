package com.henheang.saasolv.ex06_threadlocal;

/**
 * EXERCISE 06 — ThreadLocal request-scoped context.
 * Concept source: saas-olv SessionHolder.
 */
public class Ex06ThreadLocal {

    /** Pretend deep service-layer code that needs the current user. */
    static void deepServiceCall(String label) {
        System.out.println(label + " sees userId=" + SessionHolder.getUserId()
                + " ip=" + SessionHolder.getUserIp() + "  (thread="
                + Thread.currentThread().getName() + ")");
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable requestA = () -> {
            SessionHolder.Context ctx = new SessionHolder.Context();
            ctx.setUserId("alice");
            ctx.setUserIp("10.0.0.1");
            SessionHolder.init(ctx);
            try {
                deepServiceCall("RequestA");
            } finally {
                SessionHolder.clear(); // ⚠️ always clear
            }
        };

        Runnable requestB = () -> {
            SessionHolder.Context ctx = new SessionHolder.Context();
            ctx.setUserId("bob");
            ctx.setUserIp("10.0.0.2");
            SessionHolder.init(ctx);
            try {
                deepServiceCall("RequestB");
            } finally {
                SessionHolder.clear();
            }
        };

        // each thread has its OWN copy — alice and bob never mix
        Thread tA = new Thread(requestA, "http-1");
        Thread tB = new Thread(requestB, "http-2");
        tA.start(); tB.start();
        tA.join();  tB.join();

        // main thread never set a context -> null (proves isolation)
        System.out.println("\nmain thread userId = " + SessionHolder.getUserId());

        /*
         * 🔧 PRACTICE IDEAS
         *  - Comment out SessionHolder.clear() and re-run inside a reused thread
         *    (e.g. an ExecutorService with 1 thread) to SEE the data leak.
         *  - Add setAuditInfo(vo) that copies userId/ip onto a VO automatically.
         */
    }
}
