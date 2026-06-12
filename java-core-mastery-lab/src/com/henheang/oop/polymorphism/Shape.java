package com.henheang.oop.polymorphism;

/**
 * Shape is the PARENT class for all shapes.
 * <p>
 * What is Polymorphism?
 *   "Poly" = many, "morph" = form → MANY FORMS
 *   Polymorphism means: ONE variable (type Shape) can hold MANY different objects
 *   (Circle, Rectangle, etc.), and each object behaves in its OWN way.
 * <p>
 * Real-world analogy:
 *   Imagine a remote control (Shape) that can control a TV, a fan, or an AC.
 *   You press the same button "turn on" (area()), but each device does it differently.
 *   The remote doesn't need to know WHICH device it controls — it just sends the signal.
 * <p>
 * In this project:
 *   Shape defines WHAT all shapes can do: area(), perimeter()
 *   Circle and Rectangle define HOW they do it — each with their own formula
 */
public class Shape {

    // ==========================================================================
    // FIELD
    //
    // "color" is shared by ALL shapes — every shape has a color.
    // Circle and Rectangle inherit this field automatically.
    // ==========================================================================

    private final String color; // the color of this shape (e.g. "Red", "Blue")

    // ==========================================================================
    // CONSTRUCTOR
    //
    // Sets the color when a Shape (or child) object is created.
    // Circle and Rectangle call super(color) to run this constructor.
    // ==========================================================================

    public Shape(String color) {
        this.color = color;
    }

    // ==========================================================================
    // area() — Default implementation returns 0.0
    //
    // Shape itself doesn't know WHICH shape it is, so it can't calculate area.
    // That's why it just returns 0.0 as a placeholder.
    //
    // Circle and Rectangle will OVERRIDE this with their own real formulas.
    //
    // This is the key idea of polymorphism:
    //   Shape says "I have an area()" — but each child decides HOW to calculate it.
    // ==========================================================================

    public double area() {
        return 0.0; // placeholder — child classes will override this
    }

    // ==========================================================================
    // perimeter() — Same idea as area(), returns 0 as placeholder
    //
    // Circle overrides this with: 2 * PI * radius
    // Rectangle overrides this with: 2 * (width + height)
    // ==========================================================================

    public double perimeter() {
        return 0; // placeholder — child classes will override this
    }

    // Getter for color (private field — must use getter to read it)
    public String getColor() {
        return color;
    }

    // ==========================================================================
    // toString() — How this object looks when printed
    //
    // Notice: area() is called INSIDE toString().
    // When a Circle object calls toString() inherited from Shape,
    // Java will call Circle's area() — NOT Shape's area().
    // This is polymorphism in action inside toString() itself!
    // ==========================================================================

    @Override
    public String toString() {
        return "Shape[color=" + color + ", area=" + area() + "]";
        // If a Circle calls this: area() → Circle's area() formula runs
        // If a Rectangle calls this: area() → Rectangle's area() formula runs
    }
}