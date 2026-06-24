package com.henheang.oop.encapsulation;

import java.util.HashSet;
import java.util.Set;

public class EqualHashCodeDemo {

    public static void main(String[] args) {
//        Adding two BankAccount objects with the same name and address.
        BankAccount a = new BankAccount("Heang", "Acc-001", 100);
        BankAccount b = new BankAccount("Heang", "Acc-001", 100);

//  Applying HashSet to avoid duplicate entries.
      Set<BankAccount> accounts = new HashSet<>();
      accounts.add(a);
      accounts.add(b);

//
        System.out.println("Accounts: " + a.equals(b));
        System.out.println("Accounts Size: " + accounts.size());

    }
}
