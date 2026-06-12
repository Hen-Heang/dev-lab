package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;
import com.henheang.applying_project.bankmanagmentsystem.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;
    private final String accountNumber;

    public TransactionType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getRelatedAccountNumber() {
        return relatedAccountNumber;
    }

    public boolean isReversed() {
        return isReversed;
    }

    private final TransactionType type;
    private final double amount;
    private final Currency currency;
    private final LocalDateTime timestamp;
    private final String description;
    private final double balanceAfter;
    private final String referenceNumber;
    private final String relatedAccountNumber;
    private boolean isReversed;

    public Transaction(long id, String accountNumber, TransactionType type, double amount,
                       Currency currency, String description, double balanceAfter,
                       String relatedAccountNumber) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.referenceNumber = generateReferenceNumber();
        this.relatedAccountNumber = relatedAccountNumber;
        this.isReversed = false;
    }
//    getter

    @Override
    public String toString() {
        String status = isReversed ? " [REVERSED]" : "";
        return String.format("[%s] %s | %s | %.2f %s | %s | Balance: %.2f%s",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                referenceNumber, type.getDisplayName(), amount, currency,
                description, balanceAfter, status);
    }

    private String generateReferenceNumber() {

        return "";
    }

}

