package com.henheang.oop.exercise;

import java.util.ArrayList;
import java.util.List;

// Do NOT change this file — run it and match the expected output in TASK.md
public class ExerciseMain {

    public static void main(String[] args) {

        // --- Create payment objects ---
        CreditCardPayment card = new CreditCardPayment(150.00, "Online purchase", "4321");
        BankTransferPayment transfer = new BankTransferPayment(500.00, "Rent payment", "BK-9981");
        CashPayment cash = new CashPayment(75.00, "Market purchase", "Dara");

        // --- Polymorphism: all stored as Payment ---
        List<Payment> payments = new ArrayList<>();
        payments.add(card);
        payments.add(transfer);
        payments.add(cash);  // CashPayment is a Payment

        // --- Only refundable ones ---
        List<Refundable> refundable = new ArrayList<>();
        refundable.add(card);      // CreditCardPayment implements Refundable
        refundable.add(transfer);  // BankTransferPayment implements Refundable
        // cash is NOT added — CashPayment does not implement Refundable

        PaymentProcessor processor = new PaymentProcessor();

        System.out.println("===== PROCESSING PAYMENTS =====");
        processor.processBatch(payments);

        System.out.println("\n===== REFUNDING =====");
        processor.refundAll(refundable);

        System.out.println("\n===== SUMMARY =====");
        System.out.println("Total payments: " + payments.size());
        System.out.println("Total refundable: " + refundable.size());
    }
}
