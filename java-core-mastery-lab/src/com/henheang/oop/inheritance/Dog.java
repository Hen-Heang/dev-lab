package com.henheang.oop.inheritance;

/**
 * Dog is a CHILD class (also called "subclass" or "derived class").
 * <p>
 * "extends Animal" means:
 *   - Dog INHERITS everything from Animal (name, age, eat(), sleep(), getters, toString)
 *   - Dog also ADDS its own stuff: breed field, bark() method
 *   - Dog also OVERRIDES eat() to give it dog-specific behavior
 * <p>
 * Relationship: Dog IS-AN Animal
 *   Every Dog object automatically has: name, age, eat(), sleep(), getName(), getAge()
 *   Plus Dog's own:                     breed, bark(), getBreed()
 */
public class Dog extends Animal {
    //              ↑
    // "extends Animal" = Dog inherits from Animal (Dog is the child, Animal is the parent)

    // ==========================================================================
    // FIELD — Dog's own extra data (Animal doesn't have this)
    //
    // Dog adds "breed" on top of what it inherited (name, age) from Animal.
    // "private final" = only Dog's methods can use it, and it never changes.
    // ==========================================================================

    private final String breed; // the dog's breed (e.g. "Golden Retriever", "Poodle")

    // ==========================================================================
    // CONSTRUCTOR
    //
    // Dog's constructor takes 3 parameters: name, age, AND breed.
    // - name and age belong to Animal, so Dog passes them UP to Animal's constructor
    // - breed belongs to Dog, so Dog handles it itself
    //
    // "super(name, age)" = calls the PARENT (Animal) constructor.
    //   This MUST be the very first line in the constructor.
    //   It tells Animal: "please set up name and age for me."
    // ==========================================================================

    public Dog(String name, int age, String breed) {
        super(name, age); // call Animal's constructor to set name and age
        this.breed = breed; // then set Dog's own field
    }

    // ==========================================================================
    // bark() — Dog's OWN method (Animal does not have this)
    //
    // Only dogs can bark. This method does not exist in Animal or Cat.
    // getName() is inherited from Animal — Dog uses it to get its own name.
    // ==========================================================================

    public void bark() {
        System.out.println(getName() + " is barking.");
        // getName() → inherited from Animal, returns the dog's name
    }

    // ==========================================================================
    // eat() — OVERRIDING the parent's eat() method
    //
    // "@Override" = we are REPLACING Animal's eat() with a Dog-specific version.
    //
    // Animal's eat() just says: "Buddy is eating."
    // Dog's eat() says that AND adds: "Buddy is eating dog food."
    //
    // "super.eat()" = call Animal's original eat() first (don't throw it away),
    //                 then add Dog's extra behavior on top.
    // ==========================================================================

    @Override
    public void eat() {
        super.eat(); // runs Animal's eat() → prints: "Buddy is eating."
        System.out.println(getName() + " is eating dog food."); // adds dog-specific line
    }

    // Getter for breed (because breed is private, outside code uses this to read it)
    public String getBreed() {
        return breed;
    }

    // ==========================================================================
    // toString() — OVERRIDING Animal's toString()
    //
    // Animal's toString() would print: Animal [name=Buddy, age=3]
    // Dog overrides it to include breed as well.
    //
    // getName() and getAge() are inherited getters from Animal.
    // We use them here because name and age are "private" in Animal —
    // even Dog cannot access them directly.
    // ==========================================================================

    @Override
    public String toString() {
        return "Dog [breed=" + breed + ", name=" + getName() + ", age=" + getAge() + "]";
        // Output example: Dog [breed=Golden Retriever, name=Buddy, age=3]
    }
}