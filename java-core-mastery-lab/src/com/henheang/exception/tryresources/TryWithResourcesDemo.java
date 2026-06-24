package com.henheang.exception.tryresources;

/**
 * TRY-WITH-RESOURCES — auto-close anything that implements AutoCloseable.
 *
 * Key facts shown below:
 *   1. Resources are closed automatically at the end of the try block.
 *   2. Multiple resources close in REVERSE order of opening.
 *   3. Resources are still closed even when the body throws.
 *   4. If the body AND close() both throw, the close() error is "suppressed"
 *      and attached to the primary exception (getSuppressed()).
 */
public class TryWithResourcesDemo {

    public static void main(String[] args) {
        System.out.println("== 1. single resource, happy path ==");
        try (Resource r = new Resource("file.txt")) {
            r.use();
        }

        System.out.println("\n== 2. multiple resources close in REVERSE order ==");
        try (Resource a = new Resource("A");
             Resource b = new Resource("B");
             Resource c = new Resource("C")) {
            a.use(); b.use(); c.use();
        } // closes C, then B, then A

        System.out.println("\n== 3. body throws -> resource STILL closed ==");
        try (Resource r = new Resource("db-conn")) {
            r.use();
            throw new RuntimeException("query blew up");
        } catch (RuntimeException e) {
            System.out.println("  caught: " + e.getMessage() + " (but db-conn was closed above)");
        }

        System.out.println("\n== 4. suppressed exception (body + close both throw) ==");
        try (Resource r = new Resource("flaky", true)) { // close() will fail
            r.use();
            throw new RuntimeException("primary failure in body");
        } catch (RuntimeException e) {
            System.out.println("  primary    : " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  suppressed : " + s.getMessage());
            }
        }

        System.out.println("\ndone.");
    }

    /*
     * 🔧 PRACTICE IDEAS
     *  - Replace Resource with a real java.io.BufferedReader on a small file and
     *    read it inside try-with-resources (no finally, no manual close()).
     *  - Compare against the OLD way: a try/finally that calls close() by hand —
     *    notice how easy it is to forget, and how suppressed exceptions are lost.
     */
}
