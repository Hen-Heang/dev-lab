package com.henheang.oop.interfaces;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * INTERFACES — Functional interfaces (the basis of lambdas).
 *
 * A FUNCTIONAL INTERFACE has exactly ONE abstract method (a "SAM" — Single
 * Abstract Method). That single method is what a lambda or method reference
 * implements. @FunctionalInterface makes the compiler enforce the "exactly one"
 * rule.
 *
 * The JDK ships ready-made ones in java.util.function:
 *   Predicate<T>      T -> boolean   (test)
 *   Function<T,R>     T -> R         (transform)
 *   Supplier<T>       () -> T        (produce)
 *   Consumer<T>       T -> void      (consume)
 *   BiFunction<T,U,R> (T,U) -> R
 */
public class FunctionalInterfaceDemo {

    @FunctionalInterface
    interface Calculator {
        int apply(int a, int b);          // the ONE abstract method
        // adding a second abstract method here would be a COMPILE ERROR
    }

    static int doubleIt(int x) { return x * 2; }   // for a method reference

    public static void main(String[] args) {
        System.out.println("== your own functional interface ==");
        Calculator add = (a, b) -> a + b;          // lambda implements apply()
        Calculator mul = (a, b) -> a * b;
        System.out.println("  add(3,4) = " + add.apply(3, 4));
        System.out.println("  mul(3,4) = " + mul.apply(3, 4));

        System.out.println("\n== built-in functional interfaces ==");
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Function<String, Integer> length = String::length;        // method reference
        Supplier<String> greeting = () -> "hello";
        Consumer<String> printer = s -> System.out.println("  consumed: " + s);
        BiFunction<Integer, Integer, Integer> power = (b, e) -> (int) Math.pow(b, e);

        System.out.println("  isEven(4)      = " + isEven.test(4));
        System.out.println("  length(\"java\") = " + length.apply("java"));
        System.out.println("  supplier       = " + greeting.get());
        printer.accept("data");
        System.out.println("  power(2,10)    = " + power.apply(2, 10));

        System.out.println("\n== method reference vs lambda (same thing) ==");
        Function<Integer, Integer> a1 = x -> doubleIt(x);   // lambda
        Function<Integer, Integer> a2 = FunctionalInterfaceDemo::doubleIt; // method ref
        System.out.println("  " + a1.apply(5) + " == " + a2.apply(5));

        System.out.println("\n== compose functions ==");
        Function<Integer, Integer> plus1 = x -> x + 1;
        Function<Integer, Integer> times3 = x -> x * 3;
        System.out.println("  andThen (plus1 then times3) of 4 = " + plus1.andThen(times3).apply(4)); // (4+1)*3=15
        System.out.println("  compose (times3 before plus1) of 4 = " + plus1.compose(times3).apply(4)); // (4*3)+1=13

        /*
         * 🔧 PRACTICE IDEAS
         *  - Write a @FunctionalInterface StringTransformer and pass lambdas to it.
         *  - Chain Predicate with .and(), .or(), .negate().
         */
    }
}
