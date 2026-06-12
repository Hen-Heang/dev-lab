package com.henheang.applying_project.studentmanagementsystem;

import java.util.ArrayList;
import java.util.List;
/**
 * Student is a MODEL class — it only holds data and does nothing else.
 * <p>
 * [Spring] In Spring Boot, this class becomes an @Entity (JPA model).
 *          Spring will map each field to a database column automatically.
 * <p>
 * OOP concept: Encapsulation — all fields are private.
 *              You can only read/change them through getter/setter methods.
 */

public class Student {

    // 'final' means this field can never change after the object is created.
    // A student's ID is fixed — the same idea as a primary key in a database.
    // [Spring] In Spring + JPA: @Id @GeneratedValue


    private final String id;
        private String name;
        private int age;
        private String email;
        private double gpa;
        private final List<String> courses;
    // A student can enroll in many courses — this is a one-to-many relationship.
    // 'final' here means the List object itself never changes, but its contents can.
    // [Spring] In Spring + JPA: @ElementCollection or @OneToMany


    /**
     * Constructor — called when we create a new Student object.
     * It sets the initial values for all fields.
     * <p>
     * [Spring] Spring can auto-inject dependencies via constructor injection.
     *          This is the same idea: you pass what you need when creating the object.
     */

        public Student(String id, String name, int age, String email, double gpa) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.email = email;
            this.gpa = gpa;
            this.courses = new ArrayList<>();
        }
    // Getters let other classes READ a field without breaking encapsulation.
    // [Spring] Lombok @Getter generates all of these automatically.
        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getEmail() { return email; }
        public double getGpa() { return gpa; }
        public List<String> getCourses() { return courses; }

        public void setName(String name) { this.name = name; }
        public void setAge(int age) { this.age = age; }
        public void setEmail(String email) { this.email = email; }
        public void setGpa(double gpa) { this.gpa = gpa; }
    /**
     * Add a course only if the student is not already enrolled.
     * This is business logic — validating before changing data.
     * [Spring] In a Service layer, this kind of rule lives in a @Service class.
     */

        public void addCourse(String course) {
            if (!courses.contains(course)) {
                courses.add(course);
            }
        }

        public void removeCourse(String course) {
            courses.remove(course);
        }

        public String toCSV() {
            return String.join(",", id, name, String.valueOf(age), email,
                    String.valueOf(gpa), String.join(";", courses));
        }
    /**
     * Reads one CSV line from the file and rebuilds a Student object from it.
     * This is called deserialization — turning saved text back into an object.
     * <p>
     * [Spring] Jackson does this automatically when your API receives JSON data.
     *          @RequestBody Student — Spring converts the JSON to a Student object.
     */

        public static Student fromCSV(String csvLine) {
            String[] parts = csvLine.split(",");
            if (parts.length >= 5) {
                Student student = new Student(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        parts[3],
                        Double.parseDouble(parts[4]));
                if (parts.length > 5 && !parts[5].isEmpty()) {
                    String[] courses = parts[5].split(";");
                    for (String course : courses) {
                        student.addCourse(course);
                    }
                }
                return student;
            }
            return null;
        }
    /**
     * Converts this Student object into a CSV text line for file storage.
     * Format: id,name,age,email,gpa,course1;course2
     * <p>
     * [Spring] In Spring Boot, the Jackson library does this automatically.
     *          It converts your object to JSON instead of CSV.
     *          Example output: {"id":"S001","name":"Hen Heang","gpa":3.9}
     */

        @Override
        public String toString() {
            return String.format(
                    "| %-6s | %-15s | %-3d | %-25s | %-4.2f | %s |",
                    id, name, age, email, gpa, courses.isEmpty() ? "-" : String.join(";", courses)
            );
        }
    }

