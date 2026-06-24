package com.henheang.dsa.stackqueue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * STACK vs QUEUE — two fundamental orderings.
 *
 *   STACK = LIFO (Last In, First Out)  — like a stack of plates. push/pop.
 *   QUEUE = FIFO (First In, First Out) — like a line at a shop. offer/poll.
 *
 * Prefer ArrayDeque for both (faster than the legacy Stack class and than
 * LinkedList for queues). A classic stack use case — balanced brackets — is included.
 */
public class StackQueueDemo {

    public static void main(String[] args) {
        System.out.println("== STACK (LIFO) with ArrayDeque ==");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("first");
        stack.push("second");
        stack.push("third");
        System.out.println("  peek: " + stack.peek());     // third
        while (!stack.isEmpty()) {
            System.out.println("  pop:  " + stack.pop());   // third, second, first
        }

        System.out.println("\n== QUEUE (FIFO) with ArrayDeque ==");
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("first");
        queue.offer("second");
        queue.offer("third");
        System.out.println("  peek: " + queue.peek());      // first
        while (!queue.isEmpty()) {
            System.out.println("  poll: " + queue.poll());   // first, second, third
        }

        System.out.println("\n== STACK use case: balanced brackets ==");
        for (String s : new String[]{"(a[b]{c})", "([)]", "(((", "{[()]}"}) {
            System.out.printf("  %-10s -> %s%n", s, isBalanced(s) ? "balanced" : "NOT balanced");
        }

        /*
         * 🔧 PRACTICE IDEAS
         *  - Implement your own Stack<T> backed by SinglyLinkedList (push/pop O(1)).
         *  - Use a queue for BFS over a small graph/grid.
         *  - Evaluate a postfix (RPN) expression with a stack.
         */
    }

    /** Classic stack problem: are all (), [], {} correctly nested/closed? */
    static boolean isBalanced(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '(' -> stack.push(')');
                case '[' -> stack.push(']');
                case '{' -> stack.push('}');
                case ')', ']', '}' -> {
                    if (stack.isEmpty() || stack.pop() != c) return false;
                }
                default -> { /* ignore other chars */ }
            }
        }
        return stack.isEmpty();
    }
}
