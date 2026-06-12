package com.henheang.oop.abstraction;

// Duck IS-A Vehicle (extends), AND it CAN fly (Flyable), AND it CAN swim (Swimmable).
// Java allows only ONE 'extends' but MULTIPLE 'implements' — this is how Java
// achieves multiple inheritance of behavior.
public class Duck extends Vehicle implements Flyable, Swimmable {

    public Duck(String brand, int speed) {
        super(brand, speed);
    }

    // Overrides the empty fuelType() from Vehicle — ducks use water!
    @Override
    public void fuelType(){
        System.out.println(getBrand() + " duck uses water as fuel.");
    }

    // Fulfills the Flyable contract — MUST exist because Duck implements Flyable.
    @Override
    public void fly() {
        System.out.println(getBrand() + " duck is flying at speed " + getSpeed() + " km/h.");
    }

    // Fulfills the Swimmable contract — MUST exist because Duck implements Swimmable.
    @Override
    public void swim() {
        System.out.println(getBrand() + " duck is swimming at speed " + getSpeed() + " km/h.");
    }
}
