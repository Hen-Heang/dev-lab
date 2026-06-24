package com.henheang.oop.interfaces;

/**
 * INTERFACES — multiple inheritance of TYPE, and the "diamond" default-method clash.
 *
 * A class can implement MANY interfaces (multiple inheritance of type — allowed,
 * unlike classes). But if two interfaces provide the SAME default method, the
 * compiler can't choose for you: the class MUST override it. Inside the override
 * you can call a specific one with  InterfaceName.super.method().
 */
public class DiamondDefaultDemo {

    interface Walker {
        default String move() { return "walking"; }
    }

    interface Swimmer {
        default String move() { return "swimming"; }
    }

    // Amphibian implements both -> inherits TWO move() defaults -> conflict.
    // Resolve it by overriding (compile error if you don't).
    static class Amphibian implements Walker, Swimmer {
        @Override
        public String move() {
            // pick one, combine, or do something new — you decide
            return Walker.super.move() + " + " + Swimmer.super.move();
        }
    }

    // A class can also be REFERENCED as either interface type (polymorphism)
    static void describeAsWalker(Walker w) { System.out.println("  as Walker: " + w.move()); }
    static void describeAsSwimmer(Swimmer s) { System.out.println("  as Swimmer: " + s.move()); }

    public static void main(String[] args) {
        Amphibian frog = new Amphibian();
        System.out.println("resolved move() = " + frog.move());

        // same object, two interface views
        describeAsWalker(frog);
        describeAsSwimmer(frog);

        System.out.println("\nLesson: implement many interfaces freely, but a class MUST");
        System.out.println("override a clashing default; use Iface.super.method() to pick one.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Remove the override and watch the compile error appear.
         *  - Add a third interface Flyer with move(); resolve all three.
         */
    }
}
