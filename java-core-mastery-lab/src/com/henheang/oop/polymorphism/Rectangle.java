package com.henheang.oop.polymorphism;

/**
 * Rectangle is a CHILD class of Shape.
 *
 * Rectangle extends Shape, meaning:
 *   - It INHERITS: color field, getColor(), and the default area()/perimeter()
 *   - It OVERRIDES: area(), perimeter(), and toString() with Rectangle-specific formulas
 *   - It ADDS: its own fields "width" and "height"
 *
 * Polymorphism role:
 *   When Java sees a Shape variable holding a Rectangle object,
 *   and calls shape.area() — it runs Rectangle's area(), NOT Shape's.
 */
public class Rectangle extends Shape {

    // ==========================================================================
    // FIELDS — Rectangle's own data
    //
    // width and height are unique to Rectangle.
    // Shape and Circle don't have these.
    // Note: these are NOT final — they could be changed later (no final keyword).
    // ==========================================================================

    private double width;  // the width of the rectangle (e.g. 4.0)
    private double height; // the height of the rectangle (e.g. 6.0)

    // ==========================================================================
    // CONSTRUCTOR
    //
    // Takes color (for the parent), width and height (for itself).
    // "super(color)" → passes color UP to Shape's constructor
    // ==========================================================================

    public Rectangle(String color, double width, double height) {
        super(color);       // call Shape's constructor to set color
        this.width = width;   // set Rectangle's own fields
        this.height = height;
    }

    // ==========================================================================
    // area() — OVERRIDING Shape's area()
    //
    // Formula for rectangle area: width × height
    // ==========================================================================

    @Override
    public double area() {
        return width * height;
        // Example: width=4, height=6 → 4 × 6 = 24.0
    }

    // ==========================================================================
    // perimeter() — OVERRIDING Shape's perimeter()
    //
    // Formula for rectangle perimeter: 2 × (width + height)
    // ==========================================================================

    @Override
    public double perimeter() {
        return 2 * (width + height);
        // Example: width=4, height=6 → 2 × (4 + 6) = 20.0
    }

    // ==========================================================================
    // toString() — OVERRIDING Shape's toString()
    //
    // getColor() is inherited from Shape — we use it here because
    // "color" is private in Shape, so Rectangle cannot access it directly.
    // ==========================================================================

    @Override
    public String toString() {
        return "Rectangle [width=" + width + ", height=" + height + ", color=" + getColor() + "]";
        // Output example: Rectangle [width=4.0, height=6.0, color=Blue]
    }
}