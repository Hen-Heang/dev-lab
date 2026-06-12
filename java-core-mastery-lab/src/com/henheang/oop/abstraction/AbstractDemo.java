package com.henheang.oop.abstraction;

public class AbstractDemo {
    static void main(String[] args) {

        // --- Polymorphism via a parent class type ---
        // Variable type is Vehicle, but the actual object is Car.
        // Java calls Car's fuelType() at runtime — this is RUNTIME POLYMORPHISM.
        Vehicle vehicle = new Car("Toyota", 120);
        vehicle.move();      // inherited from Vehicle — prints speed
        vehicle.fuelType();  // Car's @Override version — prints gasoline

        System.out.println("-----------------------------");

        // --- Duck used as its own concrete type ---
        // All 4 methods are accessible: move() + fuelType() from Vehicle,
        // fly() from Flyable, swim() from Swimmable.
        Duck duck = new Duck("Marry", 180);
        duck.move();
        duck.fuelType();
        duck.fly();
        duck.swim();

        System.out.println("-----------------------------");

        // --- Interface used as a TYPE ---
        // Variable type is Flyable, but the actual object is Duck.
        // Through this reference you can ONLY call fly() — nothing else is visible.
        // Useful when you only care that something CAN fly, not what it IS.
        Flyable flyable = new Duck("Mallard", 150);
        flyable.fly();

        System.out.println("-----------------------------");

        // --- Same idea with Swimmable ---
        // Through Swimmable reference, only swim() is accessible.
        Swimmable swimmable = new Duck("Tony", 130);
        swimmable.swim();
    }

}
