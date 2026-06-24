package com.henheang.oop.interfaces;

/**
 * INTERFACES — Marker interfaces (an empty interface used as a "tag").
 *
 * A MARKER interface has NO methods. Its mere presence labels a type so code can
 * check `instanceof` and change behaviour. The JDK uses this idea for
 * java.io.Serializable and Cloneable.
 *
 * Modern Java often prefers ANNOTATIONS for tagging (see saasolv/ex07 @Token),
 * but marker interfaces still matter because they're TYPE-checked at compile time
 * and work with instanceof.
 */
public class MarkerInterfaceDemo {

    // marker: "instances of this type may be auditable" — no methods
    interface Auditable { }

    static class Order implements Auditable {
        final String id;
        Order(String id) { this.id = id; }
    }

    static class TempCalc {            // intentionally NOT Auditable
        int result = 42;
    }

    /** Behaviour gated by the marker. */
    static void audit(Object obj) {
        if (obj instanceof Auditable) {
            System.out.println("  AUDIT: recording " + obj.getClass().getSimpleName());
        } else {
            System.out.println("  skip : " + obj.getClass().getSimpleName() + " is not Auditable");
        }
    }

    public static void main(String[] args) {
        audit(new Order("ORD-1"));   // tagged -> audited
        audit(new TempCalc());       // not tagged -> skipped

        System.out.println("\nJDK examples of marker interfaces: Serializable, Cloneable, RandomAccess.");
        System.out.println("Lesson: an empty interface is a compile-time TYPE tag you check via instanceof.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Make Order implement java.io.Serializable and serialize it.
         *  - Re-implement the tag as an annotation (@Auditable) and read it by
         *    reflection — compare the two approaches (type vs metadata).
         */
    }
}
