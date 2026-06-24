package com.henheang.oop.interfaces;

/**
 * INTERFACES — private interface methods (Java 9+).
 *
 * Default methods often share logic. Before Java 9 that shared code had to be a
 * public default (leaking an unwanted method) or be duplicated. Java 9 added
 * PRIVATE interface methods: helpers visible only inside the interface, so
 * defaults can share code without exposing it to implementors.
 */
public class PrivateInterfaceMethodDemo {

    interface Logger {
        // public API: two default methods callers can use
        default void info(String msg)  { log("INFO", msg); }
        default void error(String msg) { log("ERROR", msg); }

        // PRIVATE helper — shared by the defaults, hidden from implementors/callers
        private void log(String level, String msg) {
            System.out.println("  [" + level + "] " + timestampTag() + " " + msg);
        }

        // private STATIC helper — pure utility, no instance needed
        private static String timestampTag() {
            return "(t=0)"; // fixed for a deterministic demo
        }
    }

    // an implementor only needs to "be a Logger" — it inherits info()/error(),
    // and CANNOT see or override log()/timestampTag()
    static class ConsoleService implements Logger { }

    public static void main(String[] args) {
        ConsoleService svc = new ConsoleService();
        svc.info("service started");
        svc.error("disk almost full");

        System.out.println("\nLesson: private interface methods (Java 9+) let default methods");
        System.out.println("share code WITHOUT exposing that helper as part of the contract.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a default warn() that reuses the same private log() helper.
         *  - Try to call svc.log(...) from main — see it won't compile (private).
         */
    }
}
