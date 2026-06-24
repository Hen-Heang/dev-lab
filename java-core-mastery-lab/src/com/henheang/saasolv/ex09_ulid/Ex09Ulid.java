package com.henheang.saasolv.ex09_ulid;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * EXERCISE 09 — ULID generator (sortable, unique IDs).
 * Concept source: saas-olv IdGenService.generateUlid().
 *
 * Proves the two properties that make ULIDs useful:
 *   (A) time-ordered  — sorting the strings = sorting by creation time
 *   (B) unique        — even thousands made in the same millisecond
 */
public class Ex09Ulid {

    public static void main(String[] args) {
        UlidGenerator gen = new UlidGenerator();

        // --- 1. what one looks like, and decode its timestamp back ---
        String id = gen.next();
        long ms = UlidGenerator.decodeTimestamp(id);
        String when = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(ms));
        System.out.println("ulid       = " + id);
        System.out.println("  └ ts part = " + id.substring(0, 10) + "  -> " + ms + "  (" + when + ")");
        System.out.println("  └ random  = " + id.substring(10));

        // --- 2. generate many FAST (same millisecond) -> still increasing + unique ---
        int n = 10_000;
        List<String> ids = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ids.add(gen.next());
        }

        boolean monotonic = true;
        for (int i = 1; i < ids.size(); i++) {
            if (ids.get(i).compareTo(ids.get(i - 1)) <= 0) { // plain String compare!
                monotonic = false;
                break;
            }
        }
        Set<String> unique = new HashSet<>(ids);

        System.out.println("\ngenerated " + n + " ULIDs in a tight loop:");
        System.out.println("  strictly increasing (monotonic)? " + monotonic);
        System.out.println("  all unique?                       " + (unique.size() == n));

        // --- 3. shuffle, then sort as strings -> creation order is restored ---
        List<String> sample = new ArrayList<>(ids.subList(0, 5));
        List<String> shuffled = new ArrayList<>(sample);
        Collections.shuffle(shuffled);
        List<String> resorted = new ArrayList<>(shuffled);
        Collections.sort(resorted); // lexicographic == chronological for ULIDs

        System.out.println("\nshuffled then string-sorted == original creation order? "
                + resorted.equals(sample));

        /*
         * 🔧 PRACTICE IDEAS
         *  - Run gen.next() from multiple threads and confirm no duplicates
         *    (the ReentrantLock is what makes this safe — try removing it).
         *  - Compare with java.util.UUID.randomUUID(): sort 5 UUIDs and 5 ULIDs,
         *    see how UUIDs sort into random order while ULIDs stay time-ordered.
         *  - Add a full decode() that also returns the 80-bit random part as hex.
         */
    }
}
