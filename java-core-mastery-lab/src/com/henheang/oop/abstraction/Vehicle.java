package com.henheang.oop.abstraction;

// Base class: holds shared data and behavior for ALL vehicle types.
// Car and Duck both inherit from this — they get brand, speed, move(), and getters for free.
public class Vehicle {

    // 'final' = cannot be changed after the constructor sets it.
    // 'private' = subclasses cannot access directly — they must use getters below.
    private final String brand;
    private final int speed;

    // Constructor: subclasses MUST call super(brand, speed) to fill these fields.
    public Vehicle(String brand, int speed) {
        this.brand = brand;
        this.speed = speed;
    }

    // Empty on purpose — each subclass overrides this with its own fuel logic.
    // This is a soft abstraction: Vehicle says "this method exists" but gives no body.
    public void fuelType() {

    }

    // Shared behavior: every vehicle moves, so this lives in the base class.
    // Neither Car nor Duck needs to rewrite this.
    public void move() {
        System.out.println("The vehicle is moving at " + speed + " km/h.");
    }

    // Getters — needed because brand and speed are private.
    // Subclasses call getBrand() and getSpeed() to read these values.
    public String getBrand() {
        return brand;
    }

    public int getSpeed() {
        return speed;
    }
}
