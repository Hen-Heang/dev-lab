package com.henheang.exception.chaining;

import java.io.IOException;

/**
 * EXCEPTION CHAINING — wrap a low-level cause inside a higher-level exception.
 *
 * Why chain instead of swallow?
 *   - The caller gets a meaningful, layer-appropriate exception
 *     (e.g. "could not load user") ...
 *   - ... while the ORIGINAL cause (e.g. IOException) is preserved for debugging
 *     via getCause() and the "Caused by:" section of the stack trace.
 *
 * Rule of thumb: catch low-level -> throw high-level WITH the cause attached.
 */
public class ExceptionChainingDemo {

    // ----- layered exceptions (low -> high) -------------------------------
    static class DataAccessException extends RuntimeException {
        DataAccessException(String msg, Throwable cause) { super(msg, cause); }
    }
    static class UserServiceException extends RuntimeException {
        UserServiceException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static void main(String[] args) {
        try {
            loadUserProfile(42);
        } catch (UserServiceException e) {
            System.out.println("Top-level message : " + e.getMessage());

            // walk the whole cause chain
            System.out.println("\nFull cause chain:");
            Throwable t = e;
            int depth = 0;
            while (t != null) {
                System.out.println("  ".repeat(depth) + "→ "
                        + t.getClass().getSimpleName() + ": " + t.getMessage());
                t = t.getCause();
                depth++;
            }

            System.out.println("\nFull stack trace (note the 'Caused by:' sections):");
            e.printStackTrace(System.out);
        }
    }

    // service layer: wraps the data-layer exception in a service exception
    private static void loadUserProfile(long userId) {
        try {
            queryUser(userId);
        } catch (DataAccessException e) {
            throw new UserServiceException("Could not load user profile " + userId, e);
        }
    }

    // data layer: wraps the raw IOException in a data-access exception
    private static void queryUser(long userId) {
        try {
            openConnection();
        } catch (IOException e) {
            throw new DataAccessException("DB query failed for user " + userId, e);
        }
    }

    // the original, lowest-level failure
    private static void openConnection() throws IOException {
        throw new IOException("connection refused: db:5432");
    }

    /*
     * 🔧 PRACTICE IDEAS
     *  - Remove the cause (use the (msg) constructor) and notice the "Caused by:"
     *    section disappears — that's lost debugging info.
     *  - Add Throwable.getRootCause-style logic that returns the deepest cause.
     *  - Compare with saasolv/ex03_exceptions (CmmException carries a cause too).
     */
}
