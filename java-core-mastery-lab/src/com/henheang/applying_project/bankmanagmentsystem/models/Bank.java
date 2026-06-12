package com.henheang.applying_project.bankmanagmentsystem.models;

import com.henheang.applying_project.bankmanagmentsystem.enums.AccountStatus;
import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;
import com.henheang.applying_project.bankmanagmentsystem.enums.LoanStatus;
import com.henheang.applying_project.bankmanagmentsystem.enums.TransactionType;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.AccountNotFoundException;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.BankingException;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.InvalidTransactionException;
import com.henheang.applying_project.bankmanagmentsystem.utilities.ValidationUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Bank implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // transient = skip saving Logger to file (Logger is not serializable)
    private static final transient Logger LOGGER = Logger.getLogger(Bank.class.getName());

        private final String bankName;

        private final Map<String, Account> accounts;
        private final Map<String, Customer> customers;
        private final Map<String, Loan> loans;
        private final AtomicLong accountCounter;
        private final List<String> fraudulentTransactions;

        public Bank(String bankName) {
            this.bankName = bankName;
            this.accounts = new ConcurrentHashMap<>();
            this.customers = new ConcurrentHashMap<>();
            this.loans = new ConcurrentHashMap<>();
            this.accountCounter = new AtomicLong(10000);
            this.fraudulentTransactions = Collections.synchronizedList(new ArrayList<>());
        }

        public String registerCustomer(String firstName, String lastName, String email, String phone,
                                       String address, LocalDate dateOfBirth, String password) throws BankingException {
            if (!ValidationUtils.isValidName(firstName) || !ValidationUtils.isValidName(lastName)) {
                throw new BankingException("Invalid name format");
            }
            if (!ValidationUtils.isValidEmail(email)) {
                throw new BankingException("Invalid email format");
            }
            if (!ValidationUtils.isValidPhone(phone)) {
                throw new BankingException("Invalid phone format");
            }

            Customer customer = new Customer(firstName, lastName, email, phone, address, dateOfBirth, password);
            customers.put(customer.getCustomerId(), customer);
            LOGGER.info("New customer registered: " + customer.getCustomerId());
            return customer.getCustomerId();
        }

        public String createAccount(String customerId, String accountType, double initialBalance,
                                    Currency currency, String password, String... additionalParams) throws BankingException {
            Customer customer = customers.get(customerId);
            if (customer == null) {
                throw new BankingException("Customer not found");
            }
            if (!customer.verifyPassword(password)) {
                throw new BankingException("Invalid password");
            }

            String accountNumber = generateAccountNumber();
            Account account = switch (accountType.toLowerCase()) {
                case "savings" -> new SavingsAccount(accountNumber, customerId, initialBalance, currency, 3.5);
                case "checking" -> new CheckingAccount(accountNumber, customerId, initialBalance, currency);
                case "business" -> {
                    if (additionalParams.length < 2) {
                        throw new BankingException("Business name and tax ID required for business account");
                    }
                    yield new BusinessAccount(accountNumber, customerId, initialBalance, currency,
                            additionalParams[0], additionalParams[1]);
                }
                default -> throw new BankingException("Invalid account type: " + accountType);
            };

            accounts.put(accountNumber, account);
            LOGGER.info("New account created: " + accountNumber + " for customer: " + customerId);
            return accountNumber;
        }

        public void deposit(String accountNumber, double amount, String description) throws BankingException {
            Account account = getAccount(accountNumber);
            account.deposit(amount, description != null ? description : "Deposit");
        }

        public void withdraw(String accountNumber, double amount, String description) throws BankingException {
            Account account = getAccount(accountNumber);
            account.withdraw(amount, description != null ? description : "Withdrawal");
        }

        public void transfer(String fromAccount, String toAccount, double amount, String description) throws BankingException {
            if (fromAccount.equals(toAccount)) {
                throw new InvalidTransactionException("Cannot transfer to same account");
            }

            Account from = getAccount(fromAccount);
            Account to = getAccount(toAccount);

            // Check for fraud patterns
            if (detectFraud(fromAccount, amount)) {
                fraudulentTransactions.add("Suspicious transfer: " + fromAccount + " -> " + toAccount + " Amount: " + amount);
                throw new InvalidTransactionException("Transaction flagged as potentially fraudulent");
            }

            synchronized (from) {
                synchronized (to) {
                    from.validateWithdrawal(amount);

                    from.balance -= amount;
                    from.addTransaction(TransactionType.TRANSFER_OUT, amount,
                            description != null ? description : "Transfer to " + toAccount, toAccount);

                    to.balance += amount;
                    to.addTransaction(TransactionType.TRANSFER_IN, amount,
                            description != null ? description : "Transfer from " + fromAccount, fromAccount);
                }
            }
            LOGGER.info(String.format("Transfer completed: %s -> %s Amount: %.2f", fromAccount, toAccount, amount));
        }

        private boolean detectFraud(String accountNumber, double amount) {
           Account account = accounts.get(accountNumber);
            if (account == null) return false;

            // Simple fraud detection rules
            return amount > account.getBalance() * 0.8 || // Large percentage of balance
                    amount > account.getDailyTransactionLimit() * 0.9 || // Near daily limit
                    account.getTransactions().stream()
                            .filter(t -> t.getTimestamp().isAfter(LocalDateTime.now().minusHours(1)))
                            .mapToDouble(Transaction::getAmount)
                            .sum() > account.getDailyTransactionLimit() * 0.5; // High hourly activity
        }

        public String applyForLoan(String accountNumber, double amount, double interestRate, int termMonths) throws BankingException {
            Account account = getAccount(accountNumber);
            if (account.getBalance() < amount * 0.1) { // Require 10% of loan amount as minimum balance
                throw new BankingException("Insufficient account balance for loan application");
            }

            Loan loan = new Loan(accountNumber, amount, interestRate, termMonths);
            loans.put(loan.getLoanId(), loan);
            LOGGER.info("Loan application submitted: " + loan.getLoanId());
            return loan.getLoanId();
        }

        public void approveLoan(String loanId) throws BankingException {
            Loan loan = loans.get(loanId);
            if (loan == null) {
                throw new BankingException("Loan not found");
            }
            loan.approve();
            loan.activate();

            // Disburse loan amount to account
            Account account = getAccount(loan.getAccountNumber());
            account.balance += loan.getPrincipalAmount();
            account.addTransaction(TransactionType.LOAN_DISBURSEMENT, loan.getPrincipalAmount(),
                    "Loan Disbursement - " + loanId, loanId);
            LOGGER.info("Loan approved and disbursed: " + loanId);
        }


        public void chargeMaintenanceFees() {
            int processed = 0;
            for (Account account : accounts.values()) {
                if (account.getStatus() == AccountStatus.ACTIVE) {
                    double fee = account.getMaintenanceFee();
                    if (account.getBalance() >= fee) {
                        account.balance -= fee;
                        account.addTransaction(TransactionType.FEE, fee, "Monthly Maintenance Fee", null);
                        processed++;
                    }
                }
            }
            LOGGER.info("Maintenance fees charged to " + processed + " accounts");
        }

        public Account getAccount(String accountNumber) throws AccountNotFoundException {
            Account account = accounts.get(accountNumber);
            if (account == null) {
                throw new AccountNotFoundException(accountNumber);
            }
            return account;
        }

        public Customer getCustomer(String customerId) throws BankingException {
            Customer customer = customers.get(customerId);
            if (customer == null) {
                throw new BankingException("Customer not found: " + customerId);
            }
            return customer;
        }

        public Loan getLoan(String loanId) throws BankingException {
            Loan loan = loans.get(loanId);
            if (loan == null) {
                throw new BankingException("Loan not found: " + loanId);
            }
            return loan;
        }

        public List<Account> getCustomerAccounts(String customerId) {
            return accounts.values().stream()
                    .filter(account -> account.getCustomerId().equals(customerId))
                    .collect(Collectors.toList());
        }

        public List<Loan> getCustomerLoans(String customerId) {
            List<String> customerAccountNumbers = getCustomerAccounts(customerId).stream()
                    .map(Account::getAccountNumber)
                    .toList();

            return loans.values().stream()
                    .filter(loan -> customerAccountNumbers.contains(loan.getAccountNumber()))
                    .collect(Collectors.toList());
        }

        public List<Account> getAllAccounts() { return new ArrayList<>(accounts.values()); }
        public List<Customer> getAllCustomers() { return new ArrayList<>(customers.values()); }
        public List<Loan> getAllLoans() { return new ArrayList<>(loans.values()); }

        private String generateAccountNumber() {
            return "ACCT" + String.format("%08d", accountCounter.getAndIncrement());
        }

        public void freezeAccount(String accountNumber) throws BankingException {
            Account account = getAccount(accountNumber);
            account.freeze();
            LOGGER.info("Account frozen: " + accountNumber);
        }

        public void unfreezeAccount(String accountNumber) throws BankingException {
            Account account = getAccount(accountNumber);
            account.unfreeze();
            LOGGER.info("Account unfrozen: " + accountNumber);
        }

        public void closeAccount(String accountNumber) throws BankingException {
            Account account = getAccount(accountNumber);
            if (account.getBalance() > 0) {
                throw new BankingException("Cannot close account with positive balance");
            }
            account.close();
            LOGGER.info("Account closed: " + accountNumber);
        }

        public String generateAccountStatement(String accountNumber, LocalDate startDate, LocalDate endDate) throws BankingException {
           Account account = getAccount(accountNumber);
            Customer customer = getCustomer(account.getCustomerId());

            StringBuilder statement = new StringBuilder();
            statement.repeat("=", 80).append("\n");
            statement.append("                    ").append(bankName.toUpperCase()).append(" - ACCOUNT STATEMENT").append("\n");
            statement.repeat("=", 80).append("\n");
            statement.append(String.format("Account Number: %s\n", account.getAccountNumber()));
            statement.append(String.format("Account Holder: %s\n", customer.getFullName()));
            statement.append(String.format("Statement Period: %s to %s\n",
                    startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            statement.append(String.format("Current Balance: %.2f %s\n", account.getBalance(), account.getCurrency()));
            statement.repeat("-", 80).append("\n");

            List<Transaction> periodTransactions = account.getTransactionHistory(startDate, endDate);
            if (periodTransactions.isEmpty()) {
                statement.append("No transactions during this period.\n");
            } else {
                statement.append("TRANSACTION HISTORY:\n");
                statement.repeat("-", 80).append("\n");
                for (Transaction tx : periodTransactions) {
                    statement.append(tx.toString()).append("\n");
                }
                statement.repeat("-", 80).append("\n");
                statement.append(String.format("Total Transactions: %d\n", periodTransactions.size()));

                double totalDebits = periodTransactions.stream()
                        .filter(t -> t.getType() == TransactionType.WITHDRAWAL ||
                                t.getType() == TransactionType.TRANSFER_OUT ||
                                t.getType() == TransactionType.FEE)
                        .mapToDouble(Transaction::getAmount)
                        .sum();

                double totalCredits = periodTransactions.stream()
                        .filter(t -> t.getType() == TransactionType.DEPOSIT ||
                                t.getType() == TransactionType.TRANSFER_IN ||
                                t.getType() == TransactionType.INTEREST)
                        .mapToDouble(Transaction::getAmount)
                        .sum();

                statement.append(String.format("Total Credits: %.2f %s\n", totalCredits, account.getCurrency()));
                statement.append(String.format("Total Debits: %.2f %s\n", totalDebits, account.getCurrency()));
            }

            statement.append("=".repeat(80)).append("\n");
            statement.append("Statement generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
            statement.append("=".repeat(80)).append("\n");

            return statement.toString();
        }

        public void displayBankSummary() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                " + bankName.toUpperCase() + " - BANK SUMMARY");
            System.out.println("=".repeat(60));

            long totalCustomers = customers.size();
            long totalAccounts = accounts.size();
            long activeAccounts = accounts.values().stream().filter(a -> a.getStatus() == AccountStatus.ACTIVE).count();

            double totalBalance = accounts.values().stream()
                    .filter(a -> a.getStatus() == AccountStatus.ACTIVE)
                    .mapToDouble(Account::getBalance)
                    .sum();

            long savingsAccounts = accounts.values().stream()
                    .filter(a -> a instanceof SavingsAccount && a.getStatus() == AccountStatus.ACTIVE)
                    .count();

            long checkingAccounts = accounts.values().stream()
                    .filter(a -> a instanceof CheckingAccount && a.getStatus() == AccountStatus.ACTIVE)
                    .count();

            long businessAccounts = accounts.values().stream()
                    .filter(a -> a instanceof BusinessAccount && a.getStatus() == AccountStatus.ACTIVE)
                    .count();

            long totalLoans = loans.size();
            long activeLoans = loans.values().stream().filter(l -> l.getStatus() == LoanStatus.ACTIVE).count();

            double totalLoanAmount = loans.values().stream()
                    .filter(l -> l.getStatus() == LoanStatus.ACTIVE)
                    .mapToDouble(Loan::getRemainingBalance)
                    .sum();

            System.out.printf("Total Customers: %d\n", totalCustomers);
            System.out.printf("Total Accounts: %d (Active: %d)\n", totalAccounts, activeAccounts);
            System.out.printf("Account Types - Savings: %d | Checking: %d | Business: %d\n",
                    savingsAccounts, checkingAccounts, businessAccounts);
            System.out.printf("Total Deposits: $%.2f\n", totalBalance);
            System.out.printf("Total Loans: %d (Active: %d)\n", totalLoans, activeLoans);
            System.out.printf("Outstanding Loan Balance: $%.2f\n", totalLoanAmount);

            if (!fraudulentTransactions.isEmpty()) {
                System.out.printf("Fraud Alerts: %d\n", fraudulentTransactions.size());
            }

            System.out.println("=".repeat(60));
        }

        // Data persistence methods
        public void exportData(String filePath) throws IOException {
            Map<String, Object> bankData = new HashMap<>();
            bankData.put("bankName", bankName);
            bankData.put("customers", customers);
            bankData.put("accounts", accounts);
            bankData.put("loans", loans);
            bankData.put("fraudulentTransactions", fraudulentTransactions);

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath)))) {
                writer.println("# Bank Data Export - " + LocalDateTime.now());
                writer.println("# This is a simplified export format");
                writer.println("BANK_NAME:" + bankName);
                writer.println("EXPORT_DATE:" + LocalDateTime.now());
                writer.println("TOTAL_CUSTOMERS:" + customers.size());
                writer.println("TOTAL_ACCOUNTS:" + accounts.size());
                writer.println("TOTAL_LOANS:" + loans.size());
            }
            LOGGER.info("Bank data exported to: " + filePath);
        }

        public void generateMonthlyInterest() {
            int processed = 0;
            for (Account account : accounts.values()) {
                if (account instanceof SavingsAccount sa && sa.getStatus() == AccountStatus.ACTIVE) {
                    sa.creditMonthlyInterest();
                    processed++;
                }
            }
            LOGGER.info("Monthly interest credited to " + processed + " savings accounts");
        }
    }

