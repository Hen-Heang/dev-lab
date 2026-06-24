package com.henheang.oop.oop_advance;

/**
 * ADVANCED OOP — Abstract class vs. Interface (and interface default/static methods).
 * <p>
 *   ABSTRACT CLASS: shared STATE + some implemented methods + abstract ones.
 *                     A class can extend only ONE. Use for an "is-a" with shared code.
 *   INTERFACE: a CONTRACT. A class can implement MANY. Since Java 8 it can
 *                     have `default` (inherited body) and `static` (utility) methods.
 * <p>
 * Rule of thumb: model a TYPE with an abstract class; model a CAPABILITY with an
 * interface. A class often extends one and implements several.
 */
public class AbstractVsInterfaceDemo {

    // abstract class: holds state + a template method, leaves one step abstract
    abstract static class Report {
        private final String title;
        Report(String title) { this.title = title; }

        // template method: shared skeleton, subclass fills the gap
        final String render() {
            return "== " + title + " ==\n" + body();
        }
        protected abstract String body();   // subclass MUST implement
    }

    // interface: a capability, with a default method and a static helper
    interface Exportable {
        String export();
        default String exportWithBanner() {        // inherited body
            return "--- EXPORT ---\n" + export();
        }
        static String formatName(String name) {     // utility, no instance needed
            return name.trim().toLowerCase();
        }
    }

    // a class extends ONE abstract class and implements MANY interfaces
    static class SalesReport extends Report implements Exportable {
        SalesReport() { super("Sales"); }
        protected String body() { return "total = $1,234"; }
        public String export() { return "sales.csv"; }
    }

    public static void main(String[] args) {
        SalesReport r = new SalesReport();

        System.out.println(r.render());                    // from abstract class
        System.out.println();
        System.out.println(r.exportWithBanner());          // default method
        System.out.println();
        System.out.println("static helper: " + Exportable.formatName("  My File  "));

        System.out.println("\nLesson: abstract class = shared TYPE+state (one);");
        System.out.println("interface = CAPABILITY (many) + default/static methods.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a Printable interface and have SalesReport implement it too.
         *  - Add another Report subclass (InventoryReport) reusing render().
         */
    }
}
