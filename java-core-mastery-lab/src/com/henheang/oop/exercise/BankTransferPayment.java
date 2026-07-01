package com.henheang.oop.exercise;

// BankTransferPayment can also be refunded — implements Refundable
public class BankTransferPayment extends Payment implements Refundable {

    private final String bankAccountNumber;

    // TODO 1: Constructor — call supper, assign bankAccountNumber
    public BankTransferPayment(double amount, String description, String bankAccountNumber) {
        super(amount, description);
        this.bankAccountNumber = bankAccountNumber;
        // TODO 1 here
    }

    // TODO 2: Return "BANK TRANSFER"
    @Override
    public String getPaymentType() {
        return null; // TODO 2
    }

    // TODO 3: Print payment line
    //   Expected: Transferred $500.00 to account BK-9981
    @Override
    protected void execute() {
        // TODO 3
    }

    // TODO 4: Bank transfer has no fee — return 0.0
    @Override
    public double getFee() {
        return 0; // TODO 4
    }

    // TODO 5: Print refund line
    //   Expected: [BANK TRANSFER] Refunded $500.00 to account BK-9981
    @Override
    public void refund(double amount) {
        // TODO 5
    }
}
