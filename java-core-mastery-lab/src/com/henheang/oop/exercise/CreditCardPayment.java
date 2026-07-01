package com.henheang.oop.exercise;

// CreditCardPayment extends Payment AND implements Refundable
// because credit card payments can be refunded
public class CreditCardPayment extends Payment implements Refundable {

    private final String cardLastFour;

    // TODO 1: Write the constructor
    //   - Call supper(amount, description)
    //   - Assign cardLastFour
    public CreditCardPayment(double amount, String description, String cardLastFour) {
        // TODO 1 here
        super(amount, description);
        this.cardLastFour = cardLastFour;
    }

    // TODO 2: Return "CREDIT CARD"
    @Override
    public String getPaymentType() {

        return "CREDIT CARD";
        // TODO 2
    }

    // TODO 3: Print payment line
    //   Expected: Paid $150.00 via card **** 4321 (fee: $3.00)
    //   Tip: use String.format("%.2f", amount) for 2 decimal places
    @Override
    protected void execute() {
        // TODO 3

    }

    // TODO 4: Fee is 2% of amount
    @Override
    public double getFee() {
        return 0; // TODO 4
    }

    // TODO 5: Print refund line
    //   Expected: [CREDIT CARD] Refunded $150.00 to card **** 4321
    @Override
    public void refund(double amount) {
        // TODO 5

    }
}
