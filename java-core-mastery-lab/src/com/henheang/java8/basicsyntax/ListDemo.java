package com.henheang.java8.basicsyntax;

// ArrayList: a resizable array — size grows/shrinks automatically
// List: an interface (a contract); ArrayList is one implementation of it

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListDemo {

    static void main() {

        // --- PART 1: Basic List of department names ---

        // List<String> = a list that only holds String values
        // new ArrayList<>() = creates the actual resizable list object
        List<String> names = new ArrayList<>();

        // .add() appends the value to the end of the list
        names.add("IT");
        names.add("HR");
        names.add("LEGLE");
        names.add("MAKETING");
        names.add("ACCOUNTIN");

        // .forEach(System.out::println) = shorthand to print every element
        // (method reference — same as: names.forEach(n -> System.out.println(n)))
//      names.forEach(System.out::println);

        // .get(index) = returns the element at the given position
        // index starts at 0, so index 1 = "HR"
//      System.out.println(names.get(1));   // Output: HR


        // --- PART 2: Practice more with ArrayList ---

        // Diamond operator <>: Java infers the type String from the left side
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Pineapple");
        fruits.add("Grapes");
        fruits.add("Strawberry");
        fruits.add("Watermelon");
        fruits.add("Peach");
        fruits.add("Pear");

        // .clear() = removes ALL elements from the list (list becomes empty)
//      fruits.clear();

        // forEach + method reference: shortest way to print every element
        // Java internally calls System.out.println(element) for each item
//      fruits.forEach(System.out::println);

        // .set(index, newValue) = replaces the element at the given index, returns OLD value
//      System.out.println("Replace Apple with Cherry " + fruits.set(0, "Cherry"));

        // .remove(index) = removes the element at the given index, returns removed element
//      System.out.println("Remove element at index 2 " + fruits.remove(2));

        // .getLast()  = returns the last element in the list
//      System.out.println("Get Last element: " + fruits.getLast());

        // .getFirst() = returns the first element in the list
//      System.out.println("Get First element: " + fruits.getFirst());

        // .contains("Apple") = returns true if "Apple" exists in the list, false if not
//      System.out.println("Get contain element: " + fruits.contains("Apple"));

        // .isEmpty() = returns true if the list has no elements, false if it has at least one
//      System.out.println("Get List is Empty: " + fruits.isEmpty());


        // --- FOR-EACH LOOP (enhanced for loop) ---
        // Use when you only need the value, not the index
//      for (String fruit: fruits) {
//          System.out.println("element: " + fruit);
//      }

        // --- CLASSIC FOR LOOP ---
        // Use when you need the index (position) of each element
//      for (int i = 0; i < fruits.size(); i++) {
//          System.out.println(i + " : " + fruits.get(i));
//      }


        // ============================================================
        // --- PART 3: STREAM API ---
        // Stream = a pipeline: data flows through steps one by one
        // Structure: list.stream() → intermediate steps → terminal step
        //
        // INTERMEDIATE (can chain many, transforms the stream):
        //   filter(), map(), sorted(), distinct(), limit()
        //
        // TERMINAL (only one, ends the pipeline, produces result):
        //   forEach(), collect(), findFirst(), count(), anyMatch(), allMatch(), noneMatch()
        // ============================================================


        // -------------------------------------------------------
        // 1. filter() — keep only elements where the condition is true
        //    f -> f.startsWith("W") means: for each element f,
        //    keep it only if it starts with "W"
        // -------------------------------------------------------

        System.out.println("--- filter: fruits starting with W ---");
        fruits.stream()
                .filter(f -> f.startsWith("W"))   // only "Watermelon" passes
                .forEach(System.out::println);     // print each surviving element
        // Output: Watermelon

        System.out.println("--- filter: names starting with I ---");
        names.stream()
                .filter(n -> n.startsWith("I"))   // only "IT" passes
                .forEach(System.out::println);
        // Output: IT


        // -------------------------------------------------------
        // 2. filter() + findFirst() + orElse() — find ONE match safely
        //    findFirst() = returns Optional<String> (a box: has value OR empty)
        //    orElse("Not found") = if the box is empty, return this default value
        //    This prevents NullPointerException — safe way to search
        // -------------------------------------------------------
        System.out.println("--- findFirst with orElse ---");
        String found = fruits.stream()
                .filter(f -> f.equals("Mango"))   // look for "Mango"
                .findFirst()                       // get first match as Optional
                .orElse("Not found");              // if empty Optional, use default
        System.out.println("The Result is : " + found);
        // Output: The Result is: Mango

        String match = fruits.stream()
                .filter(m -> m.equals("Pineapple"))
                .findFirst()
                .orElse("Not found");
        System.out.println("The Result is : " + match);
        // Output: The Result is: Pineapple


        // -------------------------------------------------------
        // 3. orElseThrow() — find ONE match or throw an exception
        //    Use in Spring REST APIs when the item MUST exist
        //    If not found → throws RuntimeException immediately
        // -------------------------------------------------------
        System.out.println("--- findFirst with orElseThrow ---");
//      String mustExist = fruits.stream()
//              .filter(f -> f.equals("Durian"))
//              .findFirst()
//              .orElseThrow(() -> new RuntimeException("Fruit not found"));
//      System.out.println(mustExist);
        // Uncomment to see: Exception in thread "main" java.lang.RuntimeException: Fruit not found


        // -------------------------------------------------------
        // 4. filter() by condition — keep elements matching a rule
        //    f.length() > 5 = keep fruits whose name is longer than 5 characters
        // -------------------------------------------------------
        System.out.println("--- filter by length > 5 ---");
        fruits.stream()
                .filter(f -> f.length() > 5)      // Banana(6), Orange(6), Mango(5)=NO ...
                .forEach(System.out::println);
        // Output: Banana, Orange, Pineapple, Grapes, Strawberry, Watermelon


        // -------------------------------------------------------
        // 5. map() — transform each element into something else
        //    Input type and output type can be different
        //    String::toUpperCase = method reference, same as f -> f.toUpperCase()
        // -------------------------------------------------------
        System.out.println("--- map: to uppercase ---");
        fruits.stream()
                .map(String::toUpperCase)          // "Apple" → "APPLE"
                .forEach(System.out::println);
        // Output: APPLE, BANANA, ORANGE, MANGO ...

        System.out.println("--- map: to lowercase ---");
        fruits.stream()
                .map(String::toLowerCase)          // "Apple" → "apple"
                .forEach(System.out::println);
        // Output: apple, banana, orange, mango ...

        System.out.println("--- map: get length of each fruit name ---");
        fruits.stream()
                .map(f -> f + " has " + f.length() + " letters")  // transform to sentence
                .forEach(System.out::println);
        // Output: Apple has 5 letters, Banana has 6 letters ...


        // -------------------------------------------------------
        // 6. sorted() — sort elements in natural order (A → Z)
        //    or with custom comparator (a, b) -> comparison logic
        // -------------------------------------------------------
        System.out.println("--- sorted: A to Z ---");
        fruits.stream()
                .sorted()                          // alphabetical ascending
                .forEach(System.out::println);
        // Output: Apple, Banana, Grapes, Mango, Orange, Peach, Pear, Pineapple, Strawberry, Watermelon

        System.out.println("--- sorted: Z to A (reverse) ---");
        fruits.stream()
                .sorted((a, b) -> b.compareTo(a)) // reverse alphabetical
                .forEach(System.out::println);
        // Output: Watermelon, Strawberry, Pineapple, Pear, Peach, Orange, Mango, Grapes, Banana, Apple


        // -------------------------------------------------------
        // 7. distinct() — remove duplicate elements,
        //    keeps only unique values, removes repeated ones
        // -------------------------------------------------------
        System.out.println("--- distinct: remove duplicates ---");
        List<String> withDupes = new ArrayList<>();
        withDupes.add("Apple");
        withDupes.add("Banana");
        withDupes.add("Apple");    // duplicate
        withDupes.add("Mango");
        withDupes.add("Banana");   // duplicate

        withDupes.stream()
                .distinct()                        // removes second "Apple" and second "Banana"
                .forEach(System.out::println);
        // Output: Apple, Banana, Mango


        // -------------------------------------------------------
        // 8. limit(n) — take only the first N elements, ignore the rest
        // -------------------------------------------------------
        System.out.println("--- limit: first 3 fruits only ---");
        fruits.stream()
                .limit(3)                          // take only index 0,1,2
                .forEach(System.out::println);
        // Output: Apple, Banana, Orange


        // -------------------------------------------------------
        // 9. collect(Collectors.toList()) — save stream result into a new List
        //    forEach = just prints, returns nothing
        //    collect = returns a real List you can store and reuse
        // -------------------------------------------------------
        System.out.println("--- collect: filter then save to new List ---");
        List<String> longFruits = fruits.stream()
                .filter(f -> f.length() > 5)       // keep fruits with name > 5 chars
                .toList();      // save result into a new List

        System.out.println("Long fruits list: " + longFruits);
        // Output: Long fruits list: [Banana, Orange, Pineapple, Grapes, Strawberry, Watermelon]


        // -------------------------------------------------------
        // 10. count() — count how many elements pass the filter
        //     returns a long (not int)
        // -------------------------------------------------------
        System.out.println("--- count: how many fruits start with P ---");
        long totalP = fruits.stream()
                .filter(f -> f.startsWith("P"))    // Pineapple, Peach, Pear
                .count();                           // count surviving elements
        System.out.println("Fruits starting with P: " + totalP);
        // Output: Fruits starting with P: 3


        // -------------------------------------------------------
        // 11. anyMatch() — true if AT LEAST ONE element matches
        //     allMatch() — true if EVERY element matches
        //     noneMatch() — true if NO element matches
        //     All return boolean (true / false)
        // -------------------------------------------------------
        System.out.println("--- anyMatch / allMatch / noneMatch ---");

        // anyMatch: does the list contain "Mango"?
        boolean hasApple = fruits.stream()
                .anyMatch(f -> f.equals("Apple"));
        System.out.println("Has Apple? " + hasApple);
        // Output: Has Apple? true

        // allMatch: are ALL fruit names shorter than 20 characters?
        boolean allShort = fruits.stream()
                .allMatch(f -> f.length() < 20);
        System.out.println("All names < 20 chars? " + allShort);
        // Output: All names < 20 chars? true

        // noneMatch: does the list contain any numbers?
        boolean noNumbers = fruits.stream()
                .noneMatch(f -> f.equals("123"));
        System.out.println("No numbers in list? " + noNumbers);
        // Output: No numbers in the list? true


        // -------------------------------------------------------
        // 12. Chaining multiple operations — the real power of Stream
        //     Each step runs in order, a result of one feeds into the next
        // -------------------------------------------------------
        System.out.println("--- chained: filter + sorted + limit + collect ---");
        List<String> result = fruits.stream()
                .filter(f -> f.length() > 4)       // step 1: keep names longer than 4 chars
                .sorted()                           // step 2: sort A to Z
                .limit(4)                           // step 3: take first 4 only
                .toList();      // step 4: save to new List

        System.out.println("Result: " + result);
        // Output: Result: [Apple, Banana, Grapes, Mango]

    }

}
