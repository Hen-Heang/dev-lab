package com.henheang.oop.exercise;

// Contract: any payment that can be refunded must implement this
public interface Refundable {

    void refund(double amount);

    // default method: child classes get this for free but can override
    default String getRefundPolicy() {
        return "Standard refund policy: refund within 30 days";
    }

    // need this so PaymentProcessor can read the amount from a Refundable reference
    double getAmount();
}
