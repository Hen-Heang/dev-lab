package com.henheang.applying_project.bankmanagmentsystem.enums;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER_IN("Transfer In"),
    TRANSFER_OUT("Transfer Out"),
    INTEREST("Interest Credit"),
    FEE("Fee Charge"),
    LOAN_DISBURSEMENT("Loan Disbursement"),
    LOAN_PAYMENT("Loan Payment"),
    REVERSAL("Transaction Reversal");

    private final String displayName;
    TransactionType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
