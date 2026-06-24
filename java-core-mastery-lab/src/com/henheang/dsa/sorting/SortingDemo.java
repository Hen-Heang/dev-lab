package com.henheang.dsa.sorting;

import java.util.Arrays;

/**
 * SORTING — a simple O(n^2) sort vs an efficient O(n log n) sort.
 *
 *   bubble sort : O(n^2)     — easy to understand, too slow for big n
 *   merge sort  : O(n log n) — divide the array in half, sort each, merge
 *
 * Seeing both side by side is the clearest way to feel why O(n log n) wins.
 */
public class SortingDemo {

    /** Repeatedly swap adjacent out-of-order pairs; biggest "bubbles" to the end. */
    static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j]; a[j] = a[j + 1]; a[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;            // already sorted -> stop early
        }
    }

    /** Divide and conquer: split, sort halves, merge them back sorted. */
    static void mergeSort(int[] a) {
        if (a.length < 2) return;
        int mid = a.length / 2;
        int[] left  = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);

        mergeSort(left);
        mergeSort(right);
        merge(a, left, right);
    }

    private static void merge(int[] a, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            a[k++] = (left[i] <= right[j]) ? left[i++] : right[j++];
        }
        while (i < left.length)  a[k++] = left[i++];   // leftovers
        while (j < right.length) a[k++] = right[j++];
    }

    public static void main(String[] args) {
        int[] data = {9, 3, 7, 1, 8, 2, 5, 4, 6, 0};
        System.out.println("unsorted   : " + Arrays.toString(data));

        int[] b = data.clone();
        bubbleSort(b);
        System.out.println("bubble sort: " + Arrays.toString(b));

        int[] m = data.clone();
        mergeSort(m);
        System.out.println("merge sort : " + Arrays.toString(m));

        // for real code, just use the JDK's highly-optimized sort:
        int[] jdk = data.clone();
        Arrays.sort(jdk);
        System.out.println("Arrays.sort: " + Arrays.toString(jdk) + "  (use this in practice)");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add a swap/comparison counter to each sort and compare for n=1000.
         *  - Implement quicksort and selection sort.
         *  - Sort an array of objects with Comparator (ties into collections).
         */
    }
}
