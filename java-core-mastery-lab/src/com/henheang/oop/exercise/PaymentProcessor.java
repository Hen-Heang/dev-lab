package com.henheang.oop.exercise;

import java.util.List;

public class PaymentProcessor {

    // TODO 1: Loop through all payments and call processPayment() on each
    //   This is POLYMORPHISM — one loop handles CreditCard, BankTransfer, Cash
    public void processBatch(List<Payment> payments) {
        for (Payment payment : payments) {
            payment.processPayment();
        }
        // TODO 1
    }

    // TODO 2: Loop through all refundable payments and call refund()
    //   Notice the parameter type is List<Refundable>, not List<Payment>
    //   CashPayment is NOT in this list because it does not implement Refundable
    public void refundAll(List<Refundable> refundables) {
        for (Refundable refundable : refundables){
            refundable.refund(refundable.getAmount());
        }
        // TODO 2
    }
}
