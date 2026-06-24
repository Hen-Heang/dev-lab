package com.henheang.exception.custom;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * CUSTOM EXCEPTIONS — a tiny bank showing both kinds:
 *   - AccountNotFoundException  (checked   — caller must handle)
 *   - InsufficientBalanceException (unchecked — business rule violation)
 *
 * Lesson: custom exceptions give errors a NAME and carry DOMAIN DATA, so callers
 * can react precisely instead of parsing strings.
 */
public class CustomExceptionDemo {

    // pretend account store: id -> balance
    private static final Map<String, BigDecimal> ACCOUNTS = new HashMap<>();
    static {
        ACCOUNTS.put("ACC-1", new BigDecimal("100.00"));
    }

    public static void main(String[] args) {
        System.out.println("== checked: must be handled ==");
        try {
            withdraw("ACC-9", new BigDecimal("10")); // unknown account
        } catch (AccountNotFoundException e) {
            System.out.println("handled checked: " + e.getMessage()
                    + " (id=" + e.getAccountId() + ")");
        }

        System.out.println("\n== unchecked: business rule violation ==");
        try {
            withdraw("ACC-1", new BigDecimal("250")); // more than balance
        } catch (AccountNotFoundException e) {
            System.out.println("won't happen here: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("handled unchecked: " + e.getMessage()
                    + " (short by " + e.getShortfall() + ")");
        }

        System.out.println("\n== happy path ==");
        try {
            withdraw("ACC-1", new BigDecimal("40"));
            System.out.println("withdrew 40, balance now " + ACCOUNTS.get("ACC-1"));
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws AccountNotFoundException     (checked) if the account doesn't exist
     * @throws InsufficientBalanceException (unchecked) if funds are too low
     */
    private static void withdraw(String accountId, BigDecimal amount)
            throws AccountNotFoundException {

        BigDecimal balance = ACCOUNTS.get(accountId);
        if (balance == null) {
            throw new AccountNotFoundException(accountId);
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(balance, amount);
        }
        ACCOUNTS.put(accountId, balance.subtract(amount));
    }

    /*
     * 🔧 PRACTICE IDEAS
     *  - Add a deposit() and a TransferException that CHAINS an
     *    InsufficientBalanceException as its cause (ties into ../chaining).
     *  - Give the exceptions an error-code field like saas-olv's CmmBizException.
     */
}
