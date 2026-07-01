package com.henheang.oop.exercise;

// CreditCardPayment extends Payment AND implements Refundable
// because credit card payments can be refunded
public class CreditCardPayment extends Payment implements Refundable {

    private final String cardLastFour;

    // TODO 1: Write the constructor
    //   - Call supper(amount, description)
    //   - Assign cardLastFour
    public CreditCardPayment(double amount, String description, String cardLastFour) {
        super(amount, description);  // must be first — calls Payment's constructor
        this.cardLastFour = cardLastFour;
    }

    @Override
    public String getPaymentType() {
        return "CREDIT CARD";
    }

    @Override
    protected void execute() {
        // getAmount() is inherited from Payment — we access parent's field via getter
        System.out.printf("Paid $%.2f via card **** %s (fee: $%.2f)%n", getAmount(), cardLastFour, getFee());
    }

    @Override
    public double getFee() {
        return getAmount() * 0.02;  // 2% fee
    }

    @Override
    public void refund(double amount) {
        System.out.printf("[CREDIT CARD] Refunded $%.2f to card **** %s%n",
                amount, cardLastFour);
    }
}
