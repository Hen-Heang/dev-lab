package com.henheang.dsa.recursion;

import java.util.HashMap;
import java.util.Map;

/**
 * RECURSION — a method that calls itself, shrinking the problem toward a BASE CASE.
 *
 * Every recursion needs:
 *   1. a base case (when to STOP) — or you get StackOverflowError
 *   2. a recursive case that moves TOWARD the base case
 *
 * The fibonacci example also shows MEMOIZATION: caching results turns an
 * exponential O(2^n) algorithm into a linear O(n) one.
 */
public class RecursionDemo {

    static long factorial(int n) {
        if (n <= 1) return 1;            // base case
        return n * factorial(n - 1);     // recursive case
    }

    static int sum(int[] a, int i) {
        if (i == a.length) return 0;     // base case
        return a[i] + sum(a, i + 1);     // add head + sum of the rest
    }

    static String reverse(String s) {
        if (s.length() <= 1) return s;   // base case
        return reverse(s.substring(1)) + s.charAt(0);
    }

    /** NAIVE fibonacci — recomputes the same values, O(2^n). */
    static long fibSlow(int n) {
        if (n < 2) return n;
        return fibSlow(n - 1) + fibSlow(n - 2);
    }

    /** MEMOIZED fibonacci — cache each result, O(n). */
    static long fibFast(int n, Map<Integer, Long> memo) {
        if (n < 2) return n;
        if (memo.containsKey(n)) return memo.get(n);
        long result = fibFast(n - 1, memo) + fibFast(n - 2, memo);
        memo.put(n, result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("factorial(5) = " + factorial(5));      // 120
        System.out.println("sum([1..5])  = " + sum(new int[]{1, 2, 3, 4, 5}, 0)); // 15
        System.out.println("reverse(\"hello\") = " + reverse("hello"));

        System.out.println("\nfibonacci(40):");
        long t1 = System.nanoTime();
        long slow = fibSlow(40);
        long t2 = System.nanoTime();
        long fast = fibFast(40, new HashMap<>());
        long t3 = System.nanoTime();

        System.out.printf("  naive  O(2^n) = %d  in %d ms%n", slow, (t2 - t1) / 1_000_000);
        System.out.printf("  memo   O(n)   = %d  in %d ms%n", fast, (t3 - t2) / 1_000_000);
        System.out.println("  -> same answer, dramatically less work");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Convert factorial to an iterative loop and compare.
         *  - Recursively compute the power a^b.
         *  - Solve "Tower of Hanoi" for 3 disks and print the moves.
         */
    }
}
