package com.henheang.oop.abstraction;

// Interface = a CONTRACT of ability.
// Any class that says "implements Flyable" MUST provide a fly() method.
// Interfaces have no fields and no constructors — only method signatures.
public interface Flyable {
    void fly(); // no body — just a promise that the method will exist
}
