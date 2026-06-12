package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.AccountStatus;
import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;
import com.henheang.applying_project.bankmanagmentsystem.enums.TransactionType;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.BankingException;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.InsufficientFundsException;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.InvalidTransactionException;
import com.henheang.applying_project.bankmanagmentsystem.utilities.ValidationUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // transient = do NOT save this field to file
    // Logger is not serializable, so we mark it transient and recreate it when loaded
    private static final transient Logger LOGGER = Logger.getLogger(Account.class.getName());
    protected final String accountNumber;
    protected final String customerId;
    protected double balance;
    protected final Currency currency;
    protected final LocalDateTime createdDate;
    protected final List<Transaction> transactions;
    protected AccountStatus status;
    protected double dailyTransactionLimit;
    protected double monthlyTransactionLimit;
    protected double currentMonthlyTransactions;
    protected LocalDate lastTransactionDate;
    private final AtomicLong transactionCounter;

    public Account(String accountNumber, String customerId, double initialBalance, Currency currency) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.currency = currency;
        this.createdDate = LocalDateTime.now();
        this.status = AccountStatus.ACTIVE;
        this.transactions = Collections.synchronizedList(new ArrayList<>());
        this.dailyTransactionLimit = getDefaultDailyLimit();
        this.monthlyTransactionLimit = getDefaultMonthlyLimit();
        this.currentMonthlyTransactions = 0;
        this.lastTransactionDate = LocalDate.now();
        this.transactionCounter = new AtomicLong(1);

        if (initialBalance > 0) {
            addTransaction(TransactionType.DEPOSIT, initialBalance, "Initial Deposit", null);
        }
    }

    protected double getDefaultDailyLimit() {
        return 0;
    }

    protected double getDefaultMonthlyLimit() {
        return 0;
    }

    protected double getMinimumBalance() {
        return 0;
    }

    protected double getMaintenanceFee() {
        return 0;
    }

    protected boolean canWithdraw(double amount) {
        return false;
    }


    //    Add new transaction to account.
    protected void addTransaction(TransactionType type, double amount, String description,
                                  String relatedAccountNumber) {
        long transactionId = transactionCounter.getAndIncrement();
        Transaction transaction = new Transaction(transactionId, accountNumber, type, amount,
                currency, description, balance, relatedAccountNumber);
        transactions.add(transaction);

        // Update monthly transaction tracking
        LocalDate today = LocalDate.now();
        if (!today.getMonth().equals(lastTransactionDate.getMonth()) ||
                today.getYear() != lastTransactionDate.getYear()) {
            currentMonthlyTransactions = 0;
        }
        currentMonthlyTransactions += amount;
        lastTransactionDate = today;
    }

    public synchronized boolean deposit(double amount, String description) throws BankingException {
        validateTransaction(amount);
        if (status != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Account is not active: " + status);
        }

        balance += amount;
        addTransaction(TransactionType.DEPOSIT, amount, description, null);
        LOGGER.info(String.format("Deposit of %.2f to account %s", amount, accountNumber));
        return true;
    }

    public synchronized void withdraw(double amount, String description) throws BankingException {
        validateTransaction(amount);
        validateWithdrawal(amount);
        balance -= amount;
        addTransaction(TransactionType.WITHDRAWAL, amount, description, null);
        LOGGER.info(String.format("Withdrawal of %.2f from account %s", amount, accountNumber));
    }

    protected void validateTransaction(double amount) throws BankingException {
        if (!ValidationUtils.isValidAmount(amount)) {
            throw new InvalidTransactionException("Invalid transaction amount: " + amount);
        }
        if (status == AccountStatus.FROZEN) {
            throw new InvalidTransactionException("Account is frozen");
        }
        if (status == AccountStatus.CLOSED) {
            throw new InvalidTransactionException("Account is closed");
        }
    }

    protected void validateWithdrawal(double amount) throws BankingException {
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Balance: %.2f, Attempted: %.2f, Min Balance: %.2f",
                            balance, amount, getMinimumBalance()));
        }
        if (amount > dailyTransactionLimit) {
            throw new InvalidTransactionException("Amount exceeds daily transaction limit");
        }
        if (currentMonthlyTransactions + amount > monthlyTransactionLimit) {
            throw new InvalidTransactionException("Amount exceeds monthly transaction limit");
        }
    }

    public void freeze() {
        this.status = AccountStatus.FROZEN;
    }

    public void unfreeze() {
        this.status = AccountStatus.ACTIVE;
    }

    public void close() {
        this.status = AccountStatus.CLOSED;
    }

    public List<Transaction> getTransactionHistory(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> !t.getTimestamp().toLocalDate().isBefore(startDate) &&
                        !t.getTimestamp().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public double getMonthlyTransactionTotal() {
        return currentMonthlyTransactions;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public AccountStatus getStatus() {
        return status;
    }

    public double getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }

    public double getMonthlyTransactionLimit() {
        return monthlyTransactionLimit;
    }

    @Override
    public String toString() {
        return String.format("Account[%s] Balance: %.2f %s | Status: %s | Created: %s | Transactions: %d",
                accountNumber, balance, currency, status,
                createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                transactions.size());
    }
}
