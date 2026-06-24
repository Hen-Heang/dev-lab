package com.henheang.oop.oop_advance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ADVANCED OOP — Immutability & defensive copying.
 *
 * An immutable object can never change after construction → it is automatically
 * thread-safe, safe to cache, and safe to share. Recipe:
 *   1. class is final (can't be subclassed to add mutability)
 *   2. all fields private final
 *   3. no setters
 *   4. DEFENSIVELY COPY mutable fields IN (constructor) and OUT (getters)
 *
 * Step 4 is the subtle one — without it, callers can mutate your internals
 * through the reference you stored or returned.
 */
public class ImmutableDemo {

    static final class ImmutablePerson {
        private final String name;
        private final List<String> hobbies;   // List is mutable -> must copy

        ImmutablePerson(String name, List<String> hobbies) {
            this.name = name;
            this.hobbies = new ArrayList<>(hobbies);   // copy IN (don't store the caller's list)
        }

        String getName() { return name; }

        List<String> getHobbies() {
            return Collections.unmodifiableList(hobbies); // copy/lock OUT
        }
    }

    public static void main(String[] args) {
        List<String> hobbies = new ArrayList<>(List.of("reading", "chess"));
        ImmutablePerson p = new ImmutablePerson("Kim", hobbies);

        System.out.println("created with : " + p.getHobbies());

        // attack 1: mutate the original list we passed in
        hobbies.add("hacking");
        System.out.println("after caller mutates source list : " + p.getHobbies()
                + "  (unchanged — copied IN)");

        // attack 2: mutate the list we got back from the getter
        try {
            p.getHobbies().add("hacking");
        } catch (UnsupportedOperationException e) {
            System.out.println("getter result is unmodifiable : blocked (copied/locked OUT)");
        }

        System.out.println("\nLesson: copy mutable fields IN and OUT, or your 'immutable'");
        System.out.println("object leaks references that let callers mutate it.");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Convert ImmutablePerson to a Java 'record' and add compact-constructor
         *    defensive copying (ties into a future modernjava package).
         *  - Add a 'withHobby(x)' method that returns a NEW ImmutablePerson.
         */
    }
}
