package com.henheang.oop.exercise;

// CashPayment does NOT implement Refundable — cash cannot be refunded
public class CashPayment extends Payment {

    private final String cashierName;

    // TODO 1: Constructor — call supper, assign cashierName
    public CashPayment(double amount, String description, String cashierName) {
        super(amount, cashierName);
        this.cashierName = cashierName;
        // TODO 1 here
    }

    // TODO 2: Return "CASH"
    @Override
    public String getPaymentType() {
        return "CASH";
        // TODO 2
    }

    // TODO 3: Print payment line
    //   Expected: $75.00 cash received by cashier: Dara
    @Override
    protected void execute() {

        // TODO 3
    }

    // TODO 4: Cash has no fee
    @Override
    public double getFee() {
        return 0;
        // TODO 4
    }
}
