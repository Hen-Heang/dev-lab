package com.henheang.dsa.bigo;

/**
 * BIG-O — how the number of steps GROWS as input size n grows.
 * <p>
 * Big-O describes the worst-case growth rate, ignoring constants. It's the single
 * most important idea for choosing/evaluating algorithms. This demo COUNTS the actual
 * steps for several n so you can see the shapes.
 * <p>
 *   O(1) constant — same work regardless of n
 *   O(log n) logarithmic — halve the problem each step (binary search)
 *   O(n)      linear — one pass
 *   O(n log n) linearithmic — good sorts (merge/quick)
 *   O(n^2)    quadratic   — nested loops (avoid for big n)
 */
public class BigODemo {

    static long constant(int[] a)  { return a.length > 0 ? a[0] : 0; }        // O(1): 1 step

    static long linear(int[] a) {                                             // O(n)
        long steps = 0;
        for (int x : a) steps++;
        return steps;
    }

    static long quadratic(int n) {                                            // O(n^2)
        long steps = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                steps++;
        return steps;
    }

    static long logarithmic(int n) {                                          // O(log n)
        long steps = 0;
        while (n > 1) { n /= 2; steps++; }
        return steps;
    }

    public static void main(String[] args) {
        int[] sizes = {8, 16, 32, 64, 128};

        System.out.printf("%-8s %-8s %-10s %-8s %-10s%n",
                "n", "O(1)", "O(log n)", "O(n)", "O(n^2)");
        System.out.println("-".repeat(48));

        for (int n : sizes) {
            int[] arr = new int[n];
            System.out.printf("%-8d %-8d %-10d %-8d %-10d%n",
                    n,
                    1,                       // constant
                    logarithmic(n),
                    linear(arr),
                    quadratic(n));
        }

        System.out.println("\nNotice: when n DOUBLES,");
        System.out.println("  O(1)      stays 1");
        System.out.println("  O(log n)  grows by +1");
        System.out.println("  O(n)      doubles");
        System.out.println("  O(n^2)    quadruples  <- this is why nested loops hurt");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add an O(n log n) column (n * log2(n)) and compare to O(n^2).
         *  - Time each with System.nanoTime() for large n and see theory match reality.
         */
    }
}
