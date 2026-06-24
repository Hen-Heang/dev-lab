package com.henheang.concurrency.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * COMPLETABLEFUTURE — compose ASYNC tasks without blocking, and run them in parallel.
 *
 * Future.get() blocks. CompletableFuture lets you CHAIN what happens next
 * (thenApply/thenCompose/thenCombine) and run independent calls at the SAME time
 * (allOf), so total time ≈ the slowest call, not the sum.
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("== 1. chain: supplyAsync -> thenApply -> thenApply ==");
        CompletableFuture<String> chain = CompletableFuture
                .supplyAsync(() -> { sleep(100); return 21; })   // produce
                .thenApply(n -> n * 2)                            // transform: 42
                .thenApply(n -> "answer = " + n);                 // transform: String
        System.out.println("  " + chain.get());

        System.out.println("\n== 2. thenCompose: dependent async calls (flatMap) ==");
        CompletableFuture<String> composed =
                fetchUserId("kim").thenCompose(id -> fetchProfile(id));
        System.out.println("  " + composed.get());

        System.out.println("\n== 3. parallel: 3 calls at once (allOf) ==");
        long start = System.currentTimeMillis();
        CompletableFuture<String> profile = supplyAfter(300, "profile");
        CompletableFuture<String> orders  = supplyAfter(300, "orders");
        CompletableFuture<String> notifs  = supplyAfter(300, "notifications");

        CompletableFuture.allOf(profile, orders, notifs).join(); // wait for all
        System.out.println("  got: " + profile.get() + ", " + orders.get() + ", " + notifs.get());
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("  elapsed ≈ " + elapsed + "ms (≈300, NOT 900 — they ran in parallel)");

        System.out.println("\n== 4. error handling: exceptionally ==");
        String safe = CompletableFuture
                .supplyAsync(() -> { throw new RuntimeException("remote down"); })
                .exceptionally(ex -> "fallback (" + ex.getMessage() + ")")
                .thenApply(Object::toString)
                .join();
        System.out.println("  " + safe);

        System.out.println("\ndone.");
    }

    // --- helpers that simulate remote/async work --------------------------
    private static CompletableFuture<String> fetchUserId(String name) {
        return CompletableFuture.supplyAsync(() -> { sleep(100); return name + "-id-7"; });
    }
    private static CompletableFuture<String> fetchProfile(String userId) {
        return CompletableFuture.supplyAsync(() -> { sleep(100); return "profile{" + userId + "}"; });
    }
    private static CompletableFuture<String> supplyAfter(long ms, String value) {
        return CompletableFuture.supplyAsync(() -> { sleep(ms); return value; });
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /*
     * 🔧 PRACTICE IDEAS
     *  - Use thenCombine to merge two independent results into one object.
     *  - Compare elapsed time when you await them one-by-one (.get each) vs allOf.
     *  - This is exactly the saas-olv dashboard pattern: fan out parallel calls.
     */
}
