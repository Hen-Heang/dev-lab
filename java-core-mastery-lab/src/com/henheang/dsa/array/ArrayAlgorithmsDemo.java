package com.henheang.dsa.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ARRAY ALGORITHMS — classical patterns every interview leans on.
 * <p>
 *   - reverse in place      : the TWO-POINTER pattern, O(n) time, O(1) space
 *   - find max              : single pass, O(n)
 *   - two-sum               : HashMap trick, O(n) instead of the O(n^2) brute force
 */
public class ArrayAlgorithmsDemo {

    /** Two-pointer: swap ends moving inward. */
    static void reverseInPlace(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int tmp = a[lo];
            a[lo] = a[hi];
            a[hi] = tmp;
            lo++;
            hi--;
        }
    }

    static int findMax(int[] a) {
        int max = a[0];
        for (int x : a) {
            if (x > max) max = x;
        }
        return max;
    }

    /** Brute force O(n^2): check every pair. */
    static int[] twoSumBrute(int[] a, int target) {
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[i] + a[j] == target) return new int[]{i, j};
        return new int[]{-1, -1};
    }

    /** Smart O(n): remember values seen, look up the needed complement. */
    static int[] twoSumFast(int[] a, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value -> index
        for (int i = 0; i < a.length; i++) {
            int need = target - a[i];
            if (seen.containsKey(need)) {
                return new int[]{seen.get(need), i};
            }
            seen.put(a[i], i);
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] a = {5, 1, 9, 3, 7};
        System.out.println("original : " + Arrays.toString(a));

        int[] copy = a.clone();
        reverseInPlace(copy);
        System.out.println("reversed : " + Arrays.toString(copy));

        System.out.println("max      : " + findMax(a));

        int target = 12;
        System.out.println("\ntwo-sum target " + target + ":");
        System.out.println("  brute O(n^2) -> indices " + Arrays.toString(twoSumBrute(a, target)));
        System.out.println("  fast  O(n)   -> indices " + Arrays.toString(twoSumFast(a, target)));
        System.out.println("  (a[2]=9 + a[3]=3 = 12)");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Two-pointer: check if an array is a palindrome.
         *  - Sliding window: max sum of k consecutive elements.
         *  - Remove duplicates from a SORTED array in place (two-pointer).
         */
    }
}
