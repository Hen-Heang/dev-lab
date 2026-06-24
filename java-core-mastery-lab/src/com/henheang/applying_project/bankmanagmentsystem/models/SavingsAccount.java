package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;
import com.henheang.applying_project.bankmanagmentsystem.enums.TransactionType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

// extends Account which is already Serializable, but we declare it here for clarity
public class SavingsAccount extends Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

        private static final double MINIMUM_BALANCE = 500.0;
        private static final double DAILY_LIMIT = 5000.0;
        private static final double MONTHLY_LIMIT = 50000.0;
        private final double interestRate;
        private LocalDate lastInterestCredit;

        public SavingsAccount(String accountNumber, String customerId, double initialBalance,
                              Currency currency, double interestRate) {
            super(accountNumber, customerId, initialBalance, currency);
            this.interestRate = interestRate;
            this.lastInterestCredit = LocalDate.now();
        }

        @Override
        protected double getDefaultDailyLimit() { return DAILY_LIMIT; }
        @Override
        protected double getDefaultMonthlyLimit() { return MONTHLY_LIMIT; }
        @Override
        public double getMinimumBalance() { return MINIMUM_BALANCE; }
        @Override
        protected double getMaintenanceFee() { return 5.0; }

        @Override
        public boolean canWithdraw(double amount) {
            return (balance - amount >= MINIMUM_BALANCE);
        }

        public void creditMonthlyInterest() {
            LocalDate today = LocalDate.now();
            if (today.getMonth() != lastInterestCredit.getMonth() ||
                    today.getYear() != lastInterestCredit.getYear()) {

                double interest = balance * (interestRate / 100.0) / 12.0;
                if (interest > 0) {
                    balance += interest;
                    addTransaction(TransactionType.INTEREST, interest,
                            "Monthly Interest @ " + interestRate + "%", null);
                    lastInterestCredit = today;
                }
            }
        }

        public double getInterestRate() { return interestRate; }
    }
