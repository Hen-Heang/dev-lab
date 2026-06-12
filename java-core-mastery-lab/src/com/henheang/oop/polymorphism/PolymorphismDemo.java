package com.henheang.oop.polymorphism;

/**
 * PolymorphismDemo shows polymorphism in action.
 *
 * The KEY moment of polymorphism is here:
 *   Shape[] shapes = { new Circle(...), new Rectangle(...), new Circle(...) };
 *
 * All three objects are stored in a Shape array (type = Shape).
 * But each object KNOWS what it really is (Circle or Rectangle).
 * When we call shape.area() or shape.perimeter(), Java automatically
 * calls the RIGHT version for each object — Circle's or Rectangle's.
 *
 * We write the loop ONCE, but it handles ALL shape types correctly.
 * This is the power of polymorphism.
 */
public class PolymorphismDemo {

    public static void main(String[] args) {

        // ----------------------------------------------------------------------
        // STEP 1: Create an array of Shape objects
        //
        // "Shape[]" = an array where each slot holds a Shape (or any child of Shape)
        //
        // Even though we store them as "Shape", each object remembers its real type:
        //   shapes[0] = Circle   (radius=5,  color=Red)
        //   shapes[1] = Rectangle (width=4, height=6, color=Blue)
        //   shapes[2] = Circle   (radius=3,  color=Green)
        //
        // This is called "upcasting" — storing a child object in a parent-type variable.
        // It's allowed because: Circle IS-A Shape, Rectangle IS-A Shape.
        // ----------------------------------------------------------------------
        Shape[] shapes = {
                new Circle("Red", 5),         // Circle stored as Shape
                new Rectangle("Blue", 4, 6),  // Rectangle stored as Shape
                new Circle("Green", 3)         // Circle stored as Shape
        };

        // ----------------------------------------------------------------------
        // STEP 2: Loop through all shapes and print info
        //
        // "for (Shape shape : shapes)" = enhanced for-loop
        //   Each iteration, "shape" points to one object in the array.
        //   First: shape = Circle(Red, 5)
        //   Second: shape = Rectangle(Blue, 4, 6)
        //   Third:  shape = Circle(Green, 3)
        //
        // POLYMORPHISM IN ACTION:
        //   shape.perimeter() → Java checks: "what is the REAL type of shape?"
        //     - If it's a Circle    → runs Circle's perimeter()    → 2 × π × r
        //     - If it's a Rectangle → runs Rectangle's perimeter() → 2 × (w + h)
        //
        //   shape.toString() (called by println) → same thing:
        //     - Circle    → "Circle[radius=5.0, area=78.54]"
        //     - Rectangle → "Rectangle [width=4.0, height=6.0, color=Blue]"
        //
        // We write this loop ONCE — it works for ALL shape types automatically.
        // If you add a Triangle class later, this loop handles it with zero changes.
        // ----------------------------------------------------------------------
        for (Shape shape : shapes) {

            // String.format("%.2f", shape.perimeter()) = format perimeter to 2 decimal places
            // shape + " | ..." → calls shape.toString() automatically
            System.out.println(shape + " | Perimeter : " + String.format("%.2f", shape.perimeter()));
        }

        // ----------------------------------------------------------------------
        // Expected output:
        //   Circle[radius=5.0, area=78.54]    | Perimeter : 31.42
        //   Rectangle [width=4.0, height=6.0, color=Blue] | Perimeter : 20.00
        //   Circle[radius=3.0, area=28.27]    | Perimeter : 18.85
        // ----------------------------------------------------------------------
    }
}