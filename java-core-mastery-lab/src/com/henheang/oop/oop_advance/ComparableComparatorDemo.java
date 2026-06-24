package com.henheang.oop.oop_advance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ADVANCED OOP — Comparable vs Comparator (how objects get ORDERED).
 *
 *   Comparable<T>  : the object's ONE natural ordering — implement compareTo().
 *                    (e.g. Employee's natural order = by age)
 *   Comparator<T>  : ANY number of EXTERNAL orderings — pass to sort().
 *                    (by name, by salary, reversed, multi-key with thenComparing)
 *
 * Use Comparable for the obvious default order; Comparator for everything else.
 */
public class ComparableComparatorDemo {

    static class Employee implements Comparable<Employee> {
        final String name;
        final int age;
        final int salary;

        Employee(String name, int age, int salary) {
            this.name = name; this.age = age; this.salary = salary;
        }

        // natural ordering = by age (ascending)
        @Override
        public int compareTo(Employee other) {
            return Integer.compare(this.age, other.age);
        }

        @Override
        public String toString() {
            return name + "(age " + age + ", $" + salary + ")";
        }
    }

    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>(List.of(
                new Employee("Kim", 35, 5000),
                new Employee("Lee", 28, 6000),
                new Employee("Park", 35, 4000),
                new Employee("Choi", 41, 6000)
        ));

        // 1. Comparable — natural order (by age)
        staff.sort(null);  // null = use compareTo
        System.out.println("by age (natural)    : " + staff);

        // 2. Comparator — by name
        staff.sort(Comparator.comparing(e -> e.name));
        System.out.println("by name             : " + staff);

        // 3. Comparator — by salary DESC
        staff.sort(Comparator.comparingInt((Employee e) -> e.salary).reversed());
        System.out.println("by salary desc      : " + staff);

        // 4. Multi-key — salary desc, then age asc (tie-breaker)
        staff.sort(Comparator.comparingInt((Employee e) -> e.salary).reversed()
                .thenComparingInt(e -> e.age));
        System.out.println("salary desc, age asc: " + staff);

        System.out.println("\nLesson: Comparable = the ONE built-in order;");
        System.out.println("Comparator = compose ANY order (comparing/reversed/thenComparing).");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Sort by name length, then alphabetically as a tie-breaker.
         *  - Use Comparator.nullsFirst(...) on a list containing a null.
         */
    }
}
