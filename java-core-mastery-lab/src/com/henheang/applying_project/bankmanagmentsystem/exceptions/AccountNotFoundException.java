package com.henheang.applying_project.bankmanagmentsystem.exceptions;

public class AccountNotFoundException extends  BankingException {
    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
