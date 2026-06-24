package com.henheang.dsa.linkedlist;

/**
 * A hand-built SINGLY LINKED LIST (so you understand what ArrayList/LinkedList
 * do under the hood). Each node holds a value + a reference to the next node.
 *
 *   head -> [A|next] -> [B|next] -> [C|null]
 *
 * Trade-off vs an array:
 *   + O(1) insert/remove at the front (no shifting)
 *   - O(n) random access (must walk from head; no index jump)
 *
 * @param <T> element type
 */
public class SinglyLinkedList<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T value) { this.value = value; }
    }

    private Node<T> head;
    private int size;

    /** O(n): walk to the end and append. */
    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
        } else {
            Node<T> cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = node;
        }
        size++;
    }

    /** O(1): insert at the front. */
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        size++;
    }

    /** O(n): remove the first node matching value. Returns true if removed. */
    public boolean remove(T value) {
        if (head == null) return false;
        if (equalsValue(head.value, value)) {     // removing the head
            head = head.next;
            size--;
            return true;
        }
        Node<T> cur = head;
        while (cur.next != null) {
            if (equalsValue(cur.next.value, value)) {
                cur.next = cur.next.next;          // skip over the node
                size--;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    /** O(n): reverse the list by flipping each node's next pointer. */
    public void reverse() {
        Node<T> prev = null;
        Node<T> cur = head;
        while (cur != null) {
            Node<T> nextTmp = cur.next; // remember
            cur.next = prev;            // flip
            prev = cur;                 // advance
            cur = nextTmp;
        }
        head = prev;
    }

    public int size() { return size; }

    private boolean equalsValue(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(" -> ");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }
}
