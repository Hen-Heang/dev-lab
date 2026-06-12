package com.henheang.oop.inheritance;

/**
 * InheritanceDemo is the "runner" class — it shows how Dog and Cat work in practice.
 * <p>
 * This class creates Dog and Cat objects and calls their methods
 * to demonstrate what inheritance, overriding, and super look like when running.
 */
public class InheritanceDemo {

    public static void main(String[] args) {

        // ----------------------------------------------------------------------
        // PART 1: Dog
        //
        // Create a Dog object with name="Buddy", age=3, breed="Golden Retriever"
        //
        // What happens behind the scenes:
        //   1. Java calls Dog's constructor
        //   2. Dog's constructor calls super(name, age) → Animal sets name and age
        //   3. Dog sets its own field: breed = "Golden Retriever"
        //   4. The Dog object is now ready with ALL fields: name, age, breed
        // ----------------------------------------------------------------------
        Dog dog = new Dog("Buddy", 3, "Golden Retriever");

        // dog.eat() → calls Dog's OVERRIDDEN eat() (not Animal's)
        // Dog's eat() first calls super.eat() then prints its own line
        // Output:
        //   "Buddy is eating."           ← from super.eat() (Animal's eat)
        //   "Buddy is eating dog food."  ← from Dog's own eat()
        dog.eat();

        // dog.sleep() → Dog does NOT override sleep(), so it uses Animal's version
        // Output: "Buddy is sleeping."
        dog.sleep();

        // dog.bark() → Dog's own method, Animal and Cat don't have this
        // Output: "Buddy is barking."
        dog.bark();

        // System.out.println(dog) → calls Dog's toString() (Dog overrides it)
        // Output: "Dog [breed=Golden Retriever, name=Buddy, age=3]"
        System.out.println(dog);

        // ----------------------------------------------------------------------
        // PART 2: Cat
        //
        // Create a Cat object with name="Whiskers", age=2, isIndoor=true
        //
        // Same process as Dog:
        //   1. Java calls Cat's constructor
        //   2. Cat's constructor calls super(name, age) → Animal sets name and age
        //   3. Cat sets its own field: isIndoor = true
        // ----------------------------------------------------------------------
        Cat cat = new Cat("Whiskers", 2, true);

        // System.out.println(cat) → Cat does NOT override toString()
        // So Java goes UP to Animal's toString() and uses that instead
        // Output: "Animal [name=Whiskers, age=2]"
        System.out.println(cat);

        // cat.eat() → calls Cat's OVERRIDDEN eat() (completely replaces Animal's)
        // Cat does NOT call super.eat(), so Animal's line is NOT printed
        // Output: "Whiskers is eating cat food."
        cat.eat();

        // cat.sleep() → Cat does NOT override sleep(), so uses Animal's version
        // Output: "Whiskers is sleeping."
        cat.sleep();

        // cat.meow() → Cat's own method, Animal and Dog don't have this
        // Output: "Whiskers is meowing."
        cat.meow();

        // ----------------------------------------------------------------------
        // SUMMARY of what this demo shows:
        //
        // Inherited (from Animal, used as-is):
        //   dog.sleep() → uses Animal's sleep()
        //   cat.sleep() → uses Animal's sleep()
        //
        // Overridden (child replaces or extends parent's version):
        //   dog.eat()   → extends Animal's eat() using super.eat() + extra line
        //   cat.eat()   → completely replaces Animal's eat() (no super.eat())
        //
        // Child-only (parent doesn't have these):
        //   dog.bark()  → only in Dog
        //   cat.meow()  → only in Cat
        // ----------------------------------------------------------------------
    }
}