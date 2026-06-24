package com.henheang.collection;

import java.util.ArrayList;
import java.util.HashSet;

public class RemoveDuplicates {

	static ArrayList<String> removeDuplicates(ArrayList<String> list) {

		// Store unique items in result.
        return RemoveDuplicatePerformance.removeDuplicates(list);
	}

	public static void main(String[] args) {

		ArrayList<String> list = new ArrayList<>();
		list.add("dog");
		list.add("cat");
		list.add("dog");
		list.add("dog");
		list.add("cat");
		list.add("bird");

		// Remove duplicates from ArrayList of Strings.
		ArrayList<String> unique = removeDuplicates(list);
		for (String element : unique) {
			System.out.println(element);
		}
	}
}
