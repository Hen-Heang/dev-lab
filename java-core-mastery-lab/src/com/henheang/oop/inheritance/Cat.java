package com.henheang.oop.inheritance;

/**
 * Cat is a CHILD class, just like Dog.
 * <p>
 * "extends Animal" means:
 *   - Cat INHERITS everything from Animal (name, age, eat(), sleep(), getters, toString)
 *   - Cat also ADDS its own stuff: isIndoor field, meow() method
 *   - Cat also OVERRIDES eat() — but differently from Dog (no super.eat() call)
 * <p>
 * Relationship: Cat IS-AN Animal
 *   Every Cat object automatically has: name, age, eat(), sleep(), getName(), getAge()
 *   Plus Cat's own:                     isIndoor, meow(), isIndoor()
 */
public class Cat extends Animal {
    //              ↑
    // "extends Animal" = Cat inherits from Animal (Cat is the child, Animal is the parent)

    // ==========================================================================
    // FIELD — Cat's own extra data (Animal and Dog don't have this)
    //
    // "boolean" = a value that is either true or false (nothing else)
    // "isIndoor" = true means the cat lives indoors, false means it goes outside
    // ==========================================================================

    private final boolean isIndoor; // true = indoor cat, false = outdoor cat

    // ==========================================================================
    // CONSTRUCTOR
    //
    // Same pattern as Dog:
    //   "super(name, age)" → pass name and age UP to Animal's constructor
    //   "this.isIndoor"    → Cat handles its own field
    // ==========================================================================

    public Cat(String name, int age, boolean isIndoor) {
        super(name, age);       // call Animal's constructor to set name and age
        this.isIndoor = isIndoor; // then set Cat's own field
    }

    // ==========================================================================
    // meow() — Cat's OWN method (Animal and Dog do not have this)
    //
    // Only cats can meow. This is unique behavior that belongs only to Cat.
    // ==========================================================================

    public void meow() {
        System.out.println(getName() + " is meowing.");
        // getName() is inherited from Animal
    }

    // ==========================================================================
    // eat() — OVERRIDING the parent's eat() method
    //
    // "@Override" = replacing Animal's eat() with a Cat-specific version.
    //
    // Notice: Cat does NOT call super.eat() here (unlike Dog).
    //   - Dog: called super.eat() first, then added its own line
    //   - Cat: completely REPLACES eat() with its own version only
    //
    // This shows two ways to override:
    //   1. super.eat() + extra code  (extend the parent behavior)
    //   2. no super.eat()            (completely replace the parent behavior)
    // ==========================================================================

    @Override
    public void eat() {
        // Animal's eat() is completely ignored here — Cat has its own version
        System.out.println(getName() + " is eating cat food.");
    }

    // ==========================================================================
    // isIndoor() — Getter for the isIndoor field
    //
    // For boolean fields, the getter is named "isXxx()" by convention (not "getXxx()").
    // e.g. isIndoor() instead of getIsIndoor()
    // Returns true if the cat is an indoor cat, false otherwise.
    // ==========================================================================

    public boolean isIndoor() {
        return isIndoor;
    }

    // Note: Cat does NOT override toString(), so when you do System.out.println(cat),
    // Java uses Animal's toString() → prints: Animal [name=Whiskers, age=2]
}