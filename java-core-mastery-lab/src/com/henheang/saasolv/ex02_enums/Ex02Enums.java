package com.henheang.saasolv.ex02_enums;

/**
 * EXERCISE 02 — Enums with state + behavior + lookup factory.
 * Concept source: saas-olv ApiResponseCode.
 */
public class Ex02Enums {

    public static void main(String[] args) {
        // simulate a code that came back from an external API
        String incomingCode = "F2002";

        StatusCode status = StatusCode.of(incomingCode);
        System.out.println("code    = " + status.getCode());
        System.out.println("message = " + status.getMessage());
        System.out.println("success?= " + status.isSuccess());

        // unknown code -> safe fallback, never null, never crashes
        StatusCode weird = StatusCode.of("???");
        System.out.println("\nunknown -> " + weird + " (" + weird.getMessage() + ")");

        // iterate all constants (enums know their own members via values())
        System.out.println("\n-- all status codes --");
        for (StatusCode s : StatusCode.values()) {
            System.out.printf("%-14s %-8s %s%n", s.name(), s.getCode(), s.getMessage());
        }

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a category (CLIENT_ERROR / SERVER_ERROR / OK) field and a
         *    helper isServerError().
         *  - Use a Map<String,StatusCode> built in a static block to make of()
         *    O(1) instead of looping (like a real high-traffic lookup).
         */
    }
}
