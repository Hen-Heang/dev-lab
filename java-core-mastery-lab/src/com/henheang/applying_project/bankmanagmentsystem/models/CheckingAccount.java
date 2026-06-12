package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;

import java.io.Serializable;

public class CheckingAccount extends Account implements Serializable {

    private static final long serialVersionUID = 1L;

        private static final double MINIMUM_BALANCE = 100.0;
        private static final double DAILY_LIMIT = 10000.0;
        private static final double MONTHLY_LIMIT = 100000.0;
        private static final double OVERDRAFT_LIMIT = 1000.0;
        private int checksIssued;

        public CheckingAccount(String accountNumber, String customerId, double initialBalance, Currency currency) {
            super(accountNumber, customerId, initialBalance, currency);
            this.checksIssued = 0;
        }

        @Override
        protected double getDefaultDailyLimit() { return DAILY_LIMIT; }
        @Override
        protected double getDefaultMonthlyLimit() { return MONTHLY_LIMIT; }
        @Override
        public double getMinimumBalance() { return MINIMUM_BALANCE; }
        @Override
        protected double getMaintenanceFee() { return 10.0; }

        @Override
        public boolean canWithdraw(double amount) {
            double postBalance = balance - amount;
            return postBalance >= -OVERDRAFT_LIMIT;
        }

        public void issueCheck() { checksIssued++; }
        public int getChecksIssued() { return checksIssued; }
    }


