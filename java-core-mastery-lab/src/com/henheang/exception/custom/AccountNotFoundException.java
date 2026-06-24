package com.henheang.exception.custom;

/**
 * Custom CHECKED exception (extends Exception).
 *
 * Use checked when the caller is expected to anticipate and handle the condition
 * (e.g. "account doesn't exist" — a normal, recoverable situation). The compiler
 * forces callers to catch it or declare `throws`.
 */
public class AccountNotFoundException extends Exception {

    private final String accountId;

    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
