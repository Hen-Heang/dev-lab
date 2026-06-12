package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;

import java.io.Serial;
import java.io.Serializable;

public class BusinessAccount extends Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

        private static final double MINIMUM_BALANCE = 2500.0;
        private static final double DAILY_LIMIT = 50000.0;
        private static final double MONTHLY_LIMIT = 500000.0;
        private final String businessName;
        private final String taxId;

        public BusinessAccount(String accountNumber, String customerId, double initialBalance,
                               Currency currency, String businessName, String taxId) {
            super(accountNumber, customerId, initialBalance, currency);
            this.businessName = businessName;
            this.taxId = taxId;
        }

        @Override
        protected double getDefaultDailyLimit() { return DAILY_LIMIT; }
        @Override
        protected double getDefaultMonthlyLimit() { return MONTHLY_LIMIT; }
        @Override
        public double getMinimumBalance() { return MINIMUM_BALANCE; }
        @Override
        protected double getMaintenanceFee() { return 25.0; }

        @Override
        public boolean canWithdraw(double amount) {
            return (balance - amount >= MINIMUM_BALANCE);
        }

        public String getBusinessName() { return businessName; }
        public String getTaxId() { return taxId; }

}
