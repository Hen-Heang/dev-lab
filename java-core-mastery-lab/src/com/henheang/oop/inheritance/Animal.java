package com.henheang.oop.inheritance;

/**
 * Animal is the PARENT class (also called "base class" or "superclass").
 * <p>
 * What is Inheritance?
 *   Inheritance = a child class RECEIVES all fields and methods from a parent class.
 *   The child gets everything the parent has, and can also ADD its own stuff.
 * <p>
 * Real-world analogy:
 *   A Dog IS-AN Animal. A Cat IS-AN Animal.
 *   All animals eat and sleep → put those in the parent (Animal).
 *   Only dogs bark, only cats meow → put those in the child (Dog, Cat).
 * <p>
 * Why use inheritance?
 *   - Avoid repeating code (DRY = Don't Repeat Yourself)
 *   - "Name" and "age" are written ONCE in Animal, not copied into Dog and Cat
 *   - eat() and sleep() are written ONCE in Animal, not copied into Dog and Cat
 */
public class Animal {

    // ==========================================================================
    // FIELDS
    //
    // These fields belong to Animal, but Dog and Cat will INHERIT them.
    // Dog and Cat don't need to declare "name" or "age" — they get it for free.
    //
    // "private" = only Animal's own methods can access them directly.
    //             Even Dog and Cat CANNOT do: this.name  (they use getName() instead)
    // "final"   = set once in the constructor, never changed after that
    // ==========================================================================

    private final String name; // the animal's name (e.g. "Buddy", "Whiskers")
    private final int age;     // the animal's age in years (e.g. 3, 2)

    // ==========================================================================
    // CONSTRUCTOR
    //
    // When Dog or Cat is created, they must call THIS constructor first
    // to set up the name and age fields that they inherited.
    //
    // They do that using "super(name, age)" — see Dog.java and Cat.java.
    // ==========================================================================

    public Animal(String name, int age) {
        this.name = name; // store the name passed in
        this.age = age;   // store the age passed in
    }

    // ==========================================================================
    // METHODS — shared behavior that ALL animals have
    //
    // Both Dog and Cat inherit these methods automatically.
    // Neither Dog nor Cat needs to write their own eat() or sleep() —
    // unless they want to OVERRIDE (change) the behavior. See Dog.java.
    // ==========================================================================

    // All animals eat — this is a general/default behavior
    public void eat() {
        System.out.println(name + " is eating.");
    }

    // All animals sleep — Dog and Cat inherit this as-is (neither overrides it)
    public void sleep() {
        System.out.println(name + " is sleeping.");
    }

    // ==========================================================================
    // GETTER METHODS
    //
    // Because name and age are "private", Dog and Cat cannot access them directly.
    // They must use these public getters — e.g. getName() instead of this.name
    // ==========================================================================

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // ==========================================================================
    // toString() METHOD
    //
    // toString() is a special method from Java's built-in Object class.
    // Every class in Java automatically inherits Object, so every class has toString().
    //
    // By default, toString() returns something ugly like: Animal@1a2b3c
    //
    // "@Override" means: we are REPLACING the default toString() with our own version.
    // Now when you do: System.out.println(animal), Java calls THIS toString() instead.
    // ==========================================================================

    @Override
    public String toString() {
        return "Animal [name=" + name + ", age=" + age + "]";
        // Output example: Animal [name=Buddy, age=3]
    }
}