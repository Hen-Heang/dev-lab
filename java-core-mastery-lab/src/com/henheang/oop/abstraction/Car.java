package com.henheang.oop.abstraction;

// Car IS-A Vehicle (inheritance via 'extends').
// It inherits: brand, speed, move(), getBrand(), getSpeed() — no need to rewrite them.
public class Car extends Vehicle {

    // Just passes the values up to Vehicle's constructor.
    // 'super(...)' MUST be the first line in any subclass constructor.
    public Car(String brand, int speed) {
        super(brand, speed);
    }

    // @Override = replacing the empty fuelType() from Vehicle with Car-specific behavior.
    // move() is NOT redefined — Car reuses Vehicle's move() as-is.
    @Override
    public void fuelType() {
        System.out.println(getBrand() + " Car runs on gasoline.");
    }
}
