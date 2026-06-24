package com.henheang.exception.basics;

import java.io.IOException;

/**
 * EXCEPTION BASICS — try / catch / finally, checked vs unchecked, propagation.
 *
 * Run main() to watch each behaviour print in order.
 */
public class ExceptionBasicsDemo {

    public static void main(String[] args) {
        System.out.println("== 1. basic try/catch ==");
        basicTryCatch();

        System.out.println("\n== 2. finally always runs ==");
        System.out.println("returned: " + finallyAlwaysRuns());

        System.out.println("\n== 3. multi-catch ==");
        multiCatch("123");   // ok
        multiCatch("abc");   // NumberFormatException
        multiCatch(null);    // NullPointerException

        System.out.println("\n== 4. checked vs unchecked ==");
        // checked: compiler FORCES us to handle or declare it
        try {
            readConfig();
        } catch (IOException e) {
            System.out.println("caught checked: " + e.getMessage());
        }
        // unchecked: no forced handling — but still crashes if uncaught
        try {
            int x = 10 / 0; // ArithmeticException (unchecked)
        } catch (ArithmeticException e) {
            System.out.println("caught unchecked: " + e.getMessage());
        }

        System.out.println("\n== 5. propagation (bubbles up the call stack) ==");
        try {
            level1();
        } catch (IllegalStateException e) {
            System.out.println("caught at main, thrown deep in level3: " + e.getMessage());
        }

        System.out.println("\ndone — program continued normally.");
    }

    private static void basicTryCatch() {
        int[] arr = {1, 2, 3};
        try {
            System.out.println(arr[5]); // out of bounds
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("oops, bad index: " + e.getMessage());
        }
    }

    /** finally runs even when we return inside try. */
    private static String finallyAlwaysRuns() {
        try {
            return "from try";
        } finally {
            System.out.println("finally ran (e.g. close resources here)");
        }
    }

    /** One catch handling several unrelated types. */
    private static void multiCatch(String input) {
        try {
            int n = Integer.parseInt(input); // may throw NumberFormatException / NPE
            System.out.println("parsed: " + n);
        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("bad input [" + input + "] -> "
                    + e.getClass().getSimpleName());
        }
    }

    /** Checked exception: must be declared with `throws`. */
    private static void readConfig() throws IOException {
        throw new IOException("config.txt not found");
    }

    // propagation chain: level1 -> level2 -> level3 (throws), caught in main
    private static void level1() { level2(); }
    private static void level2() { level3(); }
    private static void level3() {
        throw new IllegalStateException("something broke at the bottom");
    }

    /*
     * 🔧 PRACTICE IDEAS
     *  - Add a finally that closes a (fake) resource and prove it runs on both
     *    success and exception paths.
     *  - Trigger and catch a ClassCastException and a NumberFormatException.
     *  - Print e.printStackTrace() and read the stack frames top-to-bottom.
     */
}
