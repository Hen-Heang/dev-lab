package com.henheang.dsa.linkedlist;

/**
 * Drives the hand-built {@link SinglyLinkedList}.
 */
public class LinkedListDemo {

    public static void main(String[] args) {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();

        list.addLast("B");
        list.addLast("C");
        list.addFirst("A");          // O(1) front insert
        System.out.println("built    : " + list + "  (size " + list.size() + ")");

        list.addLast("D");
        System.out.println("addLast D: " + list);

        list.remove("C");
        System.out.println("remove C : " + list);

        list.reverse();
        System.out.println("reversed : " + list);

        System.out.println("remove X : " + list.remove("X") + "  (not present)");

        /*
         * 🔧 PRACTICE IDEAS
         *  - Add get(int index) and contains(T value).
         *  - Find the MIDDLE node in one pass (slow/fast pointer technique).
         *  - Detect a cycle (Floyd's tortoise & hare).
         */
    }
}
