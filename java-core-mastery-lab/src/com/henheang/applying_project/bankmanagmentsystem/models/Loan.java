package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;
import com.henheang.applying_project.bankmanagmentsystem.enums.LoanStatus;
import com.henheang.applying_project.bankmanagmentsystem.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;


        private final String loanId;
        private final String accountNumber;
        private final double principalAmount;
        private final double interestRate;
        private final int termMonths;
        private double remainingBalance;
        private final double monthlyPayment;
        private final LocalDate issueDate;
        private LocalDate nextPaymentDate;
        private LoanStatus status;
        private final List<Transaction> payments;

        public Loan(String accountNumber, double principalAmount, double interestRate, int termMonths) {
            this.loanId = "LOAN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.accountNumber = accountNumber;
            this.principalAmount = principalAmount;
            this.interestRate = interestRate;
            this.termMonths = termMonths;
            this.remainingBalance = principalAmount;
            this.monthlyPayment = calculateMonthlyPayment();
            this.issueDate = LocalDate.now();
            this.nextPaymentDate = issueDate.plusMonths(1);
            this.status = LoanStatus.PENDING;
            this.payments = new ArrayList<>();
        }

        private double calculateMonthlyPayment() {
            double monthlyRate = interestRate / 100.0 / 12.0;
            return (principalAmount * monthlyRate * Math.pow(1 + monthlyRate, termMonths)) /
                    (Math.pow(1 + monthlyRate, termMonths) - 1);
        }

        public void approve() { this.status = LoanStatus.APPROVED; }
        public void activate() { this.status = LoanStatus.ACTIVE; }

        public boolean makePayment(double amount) {
            if (amount >= monthlyPayment && remainingBalance > 0) {
                double interestPayment = remainingBalance * (interestRate / 100.0 / 12.0);
                double principalPayment = amount - interestPayment;
                remainingBalance = Math.max(0, remainingBalance - principalPayment);

               Transaction payment = new Transaction(payments.size() + 1, accountNumber,
                        TransactionType.LOAN_PAYMENT, amount, Currency.USD,
                        "Loan Payment - Principal: " + String.format("%.2f", principalPayment) +
                                ", Interest: " + String.format("%.2f", interestPayment), remainingBalance, loanId);
                payments.add(payment);

                nextPaymentDate = nextPaymentDate.plusMonths(1);

                if (remainingBalance == 0) {
                    status = LoanStatus.PAID_OFF;
                }
                return true;
            }
            return false;
        }

        // Getters
        public String getLoanId() { return loanId; }
        public String getAccountNumber() { return accountNumber; }
        public double getPrincipalAmount() { return principalAmount; }
        public double getInterestRate() { return interestRate; }
        public int getTermMonths() { return termMonths; }
        public double getRemainingBalance() { return remainingBalance; }
        public double getMonthlyPayment() { return monthlyPayment; }
        public LocalDate getIssueDate() { return issueDate; }
        public LocalDate getNextPaymentDate() { return nextPaymentDate; }
        public LoanStatus getStatus() { return status; }
        public List<Transaction> getPayments() { return new ArrayList<>(payments); }

        @Override
        public String toString() {
            return String.format("Loan[%s] Principal: %.2f | Remaining: %.2f | Payment: %.2f | Status: %s | Next Due: %s",
                    loanId, principalAmount, remainingBalance, monthlyPayment, status,
                    nextPaymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

}
