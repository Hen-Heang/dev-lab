package com.henheang.oop.polymorphism;

/**
 * Circle is a CHILD class of Shape.
 * <p>
 * Circle extends Shape, meaning:
 *   - It INHERITS: color field, getColor(), and the default area()/perimeter()
 *   - It OVERRIDES: area(), perimeter(), and toString() with Circle-specific formulas
 *   - It ADDS: its own field "radius"
 * <p>
 * Polymorphism role:
 *   When Java sees a Shape variable holding a Circle object,
 *   and calls shape.area() — it runs Circle's area(), NOT Shape's.
 *   This is called "runtime polymorphism" or "dynamic dispatch".
 */
public class Circle extends Shape {

    // ==========================================================================
    // FIELD — Circle's own data
    //
    // "radius" is unique to Circle — Shape and Rectangle don't have it.
    // "final" = set once in constructor, never changes.
    // ==========================================================================

    private final double radius; // the radius of this circle (e.g. 5.0)

    // ==========================================================================
    // CONSTRUCTOR
    //
    // Takes color (for the parent) and radius (for itself).
    // "super(color)" → passes color UP to Shape's constructor
    // "this.radius"  → Circle stores its own radius
    // ==========================================================================

    public Circle(String color, double radius) {
        super(color);       // call Shape's constructor to set color
        this.radius = radius; // set Circle's own field
    }

    // ==========================================================================
    // area() — OVERRIDING Shape's area()
    //
    // Formula for circle area: π × r²
    //   Math.PI = 3.14159... (built-in Java constant)
    //   radius * radius = r²
    //
    // "@Override" = replacing Shape's placeholder (0.0) with the real formula
    // ==========================================================================

    @Override
    public double area() {
        return Math.PI * radius * radius;
        // Example: radius=5 → 3.14159 × 5 × 5 = 78.54
    }

    // ==========================================================================
    // perimeter() — OVERRIDING Shape's perimeter()
    //
    // For a circle, perimeter is called "circumference".
    // Formula: 2 × π × r
    // ==========================================================================

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
        // Example: radius=5 → 2 × 3.14159 × 5 = 31.42
    }

    // ==========================================================================
    // toString() — OVERRIDING Shape's toString()
    //
    // String.format("%.2f", area()) = format the area number to 2 decimal places
    //   e.g. 78.539816... becomes "78.54"
    //
    // "%.2f" breakdown:
    //   %  = placeholder for a value
    //   .2 = show 2 digits after the decimal point
    //   f  = the value is a floating-point number (double/float)
    // ==========================================================================

    @Override
    public String toString() {
        return "Circle[radius=" + radius + ", area=" + String.format("%.2f", area()) + "]";
        // Output example: Circle[radius=5.0, area=78.54]
    }
}