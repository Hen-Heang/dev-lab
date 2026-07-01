package com.henheang.oop.exercise;

// Abstract class — provided, do NOT change this file
public abstract class Payment {

    private final double amount;
    private final String description;

    public Payment(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    // --- Concrete method: shared logic for ALL payment types ---
    // Calls execute() which each child implements differently
    public final void processPayment() {
        System.out.printf("[%s] ", getPaymentType());
        execute();
    }

    // --- Abstract methods: each child MUST implement these ---

    // What type of payment is this? e.g. "CREDIT CARD", "BANK TRANSFER"
    public abstract String getPaymentType();

    // The actual payment action — different for each type
    protected abstract void execute();

    // Any extra fee on top of amount (0.0 means no fee)
    public abstract double getFee();

    // --- Getters ---
    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
