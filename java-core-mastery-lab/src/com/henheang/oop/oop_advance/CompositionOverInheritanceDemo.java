package com.henheang.oop.oop_advance;

/**
 * ADVANCED OOP — Composition over Inheritance.
 *
 * Inheritance = "IS-A" (a Cat IS-A Animal). Composition = "HAS-A" (a Car HAS-A Engine).
 *
 * Prefer composition when you want to REUSE behaviour or SWAP it at runtime.
 * Inheritance locks you to one superclass forever and exposes you to the
 * "fragile base class" problem. Here a Car HAS-A Engine, so we can swap engines
 * without touching the Car class.
 */
public class CompositionOverInheritanceDemo {

    // the swappable behaviour, behind an interface
    interface Engine {
        String start();
    }

    static class PetrolEngine implements Engine {
        public String start() { return "vroom (petrol)"; }
    }

    static class ElectricEngine implements Engine {
        public String start() { return "hum... (electric)"; }
    }

    // Car HAS-A Engine (composition) — not "extends PetrolEngine"
    static class Car {
        private Engine engine;                 // composed part
        private final String model;

        Car(String model, Engine engine) {
            this.model = model;
            this.engine = engine;
        }

        void swapEngine(Engine engine) {       // behaviour change at RUNTIME
            this.engine = engine;
        }

        String drive() {
            return model + ": " + engine.start();
        }
    }

    public static void main(String[] args) {
        Car car = new Car("Model-X", new PetrolEngine());
        System.out.println(car.drive());       // petrol

        car.swapEngine(new ElectricEngine());  // impossible with inheritance!
        System.out.println(car.drive());       // electric

        System.out.println("\nLesson: composition lets you swap/reuse behaviour;");
        System.out.println("inheritance would hard-wire ONE engine into the class hierarchy.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a HybridEngine that itself COMPOSES a Petrol + Electric engine.
         *  - Compose a Logger into the Car instead of extending a LoggingCar.
         */
    }
}
