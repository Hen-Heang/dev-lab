package com.henheang.dsa.search;

import java.util.Arrays;

/**
 * SEARCHING — linear vs binary.
 *
 *   linear search : O(n)     — works on ANY array, checks each element
 *   binary search : O(log n) — requires a SORTED array, halves the range each step
 *
 * Binary search is the canonical example of "halving" = logarithmic time.
 */
public class SearchDemo {

    static int linearSearch(int[] a, int target) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == target) return i;
        }
        return -1;
    }

    /** Iterative binary search. Array MUST be sorted. */
    static int binarySearch(int[] a, int target) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;   // avoids overflow vs (lo+hi)/2
            if (a[mid] == target) return mid;
            if (a[mid] < target) lo = mid + 1;  // discard left half
            else                 hi = mid - 1;  // discard right half
        }
        return -1;
    }

    /** Recursive binary search — same idea expressed with recursion. */
    static int binarySearchRecursive(int[] a, int target, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = lo + (hi - lo) / 2;
        if (a[mid] == target) return mid;
        return (a[mid] < target)
                ? binarySearchRecursive(a, target, mid + 1, hi)
                : binarySearchRecursive(a, target, lo, mid - 1);
    }

    public static void main(String[] args) {
        int[] a = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91}; // sorted
        System.out.println("array: " + Arrays.toString(a));

        for (int target : new int[]{23, 2, 91, 100}) {
            System.out.printf("%nfind %d:%n", target);
            System.out.println("  linear            -> index " + linearSearch(a, target));
            System.out.println("  binary (iter)     -> index " + binarySearch(a, target));
            System.out.println("  binary (recursive)-> index "
                    + binarySearchRecursive(a, target, 0, a.length - 1));
        }

        /*
         * 🔧 PRACTICE IDEAS
         *  - Count how many steps binary search takes for n=1,000,000 (~20!).
         *  - Find the FIRST index of a target that appears multiple times.
         *  - Compare your result with java.util.Arrays.binarySearch(a, target).
         */
    }
}
