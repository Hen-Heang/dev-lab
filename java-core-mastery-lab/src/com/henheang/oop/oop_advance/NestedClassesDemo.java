package com.henheang.oop.oop_advance;

/**
 * ADVANCED OOP — the four kinds of nested classes (+ lambda).
 * <p>
 * STATIC NESTED: no link to an outer instance — just namespacing/helpers.
 * INNER (non-static): tied to an OUTER instance; can read its fields.
 * LOCAL: declared inside a method.
 * ANONYMOUS: a one-off class+instance, usually implementing an interface.
 * LAMBDA: the modern shorthand for an anonymous functional interface.
 */
public class NestedClassesDemo {

    private final String outerField = "outer-state";

    // 1. STATIC NESTED — independent of any NestedClassesDemo instance
    static class StaticNested {
        String describe() {
            return "static nested (no outer instance needed)";
        }
    }

    // 2. INNER (non-static) — needs an enclosing instance, can see outerField
    class Inner {
        String describe() {
            return "inner can read outer -> " + outerField;
        }
    }

    interface Greeter {
        String greet(String name);
    }

    void run() {
        System.out.println("1. " + new StaticNested().describe());
        System.out.println("2. " + this.new Inner().describe());

        // 3. LOCAL class — visible only inside this method
        class Local {
            String describe() {
                return "local class, sees outer -> " + outerField;
            }
        }
        System.out.println("3. " + new Local().describe());

        // 4. ANONYMOUS class — implement Greeter inline, once
        Greeter anon = new Greeter() {
            public String greet(String name) {
                return "anonymous: hello " + name;
            }
        };
        System.out.println("4. " + anon.greet("Kim"));

        // 5. LAMBDA — same thing, far shorter (Greeter is a functional interface)
        Greeter lambda = name -> "lambda: hi " + name;
        System.out.println("5. " + lambda.greet("Lee"));
    }

    public static void main(String[] args) {
        new NestedClassesDemo().run();

        System.out.println("\nLesson: static nested = helper; inner = bound to outer;");
        System.out.println("anonymous/lambda = quick one-off implementations.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Make StaticNested a Builder for the outer class.
         *  - Replace the anonymous Comparator in a sort call with a lambda.
         */
    }
}
