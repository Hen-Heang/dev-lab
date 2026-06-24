package com.henheang.exception.tryresources;

/**
 * A fake closeable resource. Implementing AutoCloseable is what lets a class be
 * used in try-with-resources — close() is called automatically at block exit,
 * even if an exception is thrown.
 */
public class Resource implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    public Resource(String name) {
        this(name, false);
    }

    public Resource(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        System.out.println("  opened  " + name);
    }

    public void use() {
        System.out.println("  using   " + name);
    }

    /** Called automatically by try-with-resources. */
    @Override
    public void close() {
        if (failOnClose) {
            throw new RuntimeException("failed closing " + name);
        }
        System.out.println("  closed  " + name);
    }
}
