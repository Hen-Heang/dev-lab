package com.henheang.applying_project.bankmanagmentsystem;

import com.henheang.applying_project.bankmanagmentsystem.enums.AccountStatus;
import com.henheang.applying_project.bankmanagmentsystem.enums.LoanStatus;
import com.henheang.applying_project.bankmanagmentsystem.enums.Currency;
import com.henheang.applying_project.bankmanagmentsystem.exceptions.BankingException;
import com.henheang.applying_project.bankmanagmentsystem.models.*;
import com.henheang.applying_project.bankmanagmentsystem.utilities.ValidationUtils;
import com.henheang.applying_project.bankmanagmentsystem.utilities.ConsoleColor;
import com.henheang.applying_project.bankmanagmentsystem.utilities.DataPersistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Enhanced Java Community Bank Management System
 * Professional Version with Advanced Features
 * <p>
 * Features:
 * - Enhanced security with password hashing
 * - Data persistence with JSON export/import
 * - Advanced validation and error handling
 * - Comprehensive logging
 * - Account statements and reports
 * - Business accounts with special features
 * - Loan management system
 * - Transaction limits and fraud detection
 * - Multi-currency support
 * - Account freezing/unfreezing
 * - Automated maintenance tasks
 */
public class BankManagementSystem {

    public static final Logger LOGGER = Logger.getLogger(BankManagementSystem.class.getName());

    // ========= ENHANCED USER INTERFACE =========
    private final Bank bank;
    private final Scanner scanner;
    private String currentCustomerId;
    boolean isRunning = true;

    public BankManagementSystem() {
        this.scanner = new Scanner(System.in);
        this.currentCustomerId = null;

        // Try to load previously saved data from bank_data.dat
        // If file doesn't exist (first run), load() returns null → create fresh Bank
        Bank loaded = DataPersistence.load();
        this.bank = (loaded != null) ? loaded : new Bank("Enhanced Java Community Bank");
    }

    public void start() {
        // cyan color for the welcome banner header
        System.out.println(ConsoleColor.cyan("=".repeat(60)));
        System.out.println(ConsoleColor.cyan("     Welcome to Enhanced Java Community Bank"));
        System.out.println(ConsoleColor.cyan("         Professional Banking System v2.0"));
        System.out.println(ConsoleColor.cyan("=".repeat(60)));

//        Set choice for Processing
        while (isRunning) {
            try {
                if (currentCustomerId == null) {
                    displayGuestMenu();
                    int choice = getIntInput("Enter your choice: ");
                    handleGuestChoice(choice);
                } else {
                    displayMainMenu();
                    int choice = getIntInput("Enter your choice: ");
                    handleMainChoice(choice);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                LOGGER.severe("System error: " + e.getMessage());
            }
        }
    }

    private void displayGuestMenu() {
        // cyan for the menu borders, bold for the title
        System.out.println(ConsoleColor.cyan("\n╔" + "═".repeat(38) + "╗"));
        System.out.println(ConsoleColor.cyan("║") + ConsoleColor.bold("              GUEST MENU               ") + ConsoleColor.cyan("║"));
        System.out.println(ConsoleColor.cyan("╠" + "═".repeat(38) + "╣"));
        System.out.println(ConsoleColor.cyan("║") + "  1. Register New Customer             " + ConsoleColor.cyan("║"));
        System.out.println(ConsoleColor.cyan("║") + "  2. Customer Login                    " + ConsoleColor.cyan("║"));
        System.out.println(ConsoleColor.cyan("║") + "  3. Admin Functions                   " + ConsoleColor.cyan("║"));
        System.out.println(ConsoleColor.cyan("║") + "  4. Exit                              " + ConsoleColor.cyan("║"));
        System.out.println(ConsoleColor.cyan("╚" + "═".repeat(38) + "╝"));
    }

    private void displayMainMenu() {
        try {
            Customer customer = bank.getCustomer(currentCustomerId);
            // show customer name in bold green inside the cyan border
            System.out.println(ConsoleColor.cyan("\n╔" + "═".repeat(48) + "╗"));
            System.out.println(ConsoleColor.cyan("║") + "  Hello, " + ConsoleColor.bold(ConsoleColor.green(customer.getFullName())) + ConsoleColor.cyan(""));
            System.out.println(ConsoleColor.cyan("╠" + "═".repeat(48) + "╣"));
            System.out.println(ConsoleColor.cyan("║") + "   1.  Create New Account                       " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   2.  View My Accounts                         " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   3.  Deposit Money                            " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   4.  Withdraw Money                           " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   5.  Transfer Money                           " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   6.  View Account Statement                   " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   7.  Apply for Loan                           " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   8.  View My Loans                            " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   9.  Update Profile                           " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   10. Change Password                          " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("║") + "   11. Logout                                   " + ConsoleColor.cyan("║"));
            System.out.println(ConsoleColor.cyan("╚" + "═".repeat(48) + "╝"));
        } catch (BankingException e) {
            System.out.println(ConsoleColor.red("Error loading customer data: " + e.getMessage()));
            currentCustomerId = null;
        }
    }

    private void handleGuestChoice(int choice) {
        switch (choice) {
            case 1 -> registerCustomer();
            case 2 -> customerLogin();
            case 3 -> adminFunctions();
            case 4 -> {
                DataPersistence.save(bank); // save before exit
                System.out.println(ConsoleColor.green("Thank you for using our banking system!"));
                isRunning = false;
                System.exit(0);
            }

            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void handleMainChoice(int choice) {
        switch (choice) {
            case 1 -> createAccount();
            case 2 -> viewMyAccounts();
            case 3 -> depositMoney();
            case 4 -> withdrawMoney();
            case 5 -> transferMoney();
            case 6 -> viewAccountStatement();
            case 7 -> applyForLoan();
            case 8 -> viewMyLoans();
            case 9 -> updateProfile();
            case 10 -> changePassword();
            case 11 -> {
                currentCustomerId = null;
                DataPersistence.save(bank); // save on logout
                System.out.println(ConsoleColor.green("Logged out successfully."));
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    private void registerCustomer() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("       CUSTOMER REGISTRATION");
        System.out.println("-".repeat(40));

        try {
            String firstName = getStringInput("First Name: ");
            String lastName = getStringInput("Last Name: ");
            String email = getStringInput("Email: ");
            String phone = getStringInput("Phone: ");
            String address = getStringInput("Address: ");

            System.out.print("Date of Birth (YYYY-MM-DD): ");
            LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine().trim());

            String password = getStringInput("Password: ");
            String confirmPassword = getStringInput("Confirm Password: ");

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match!");
                return;
            }

            String customerId = bank.registerCustomer(firstName, lastName, email, phone, address, dateOfBirth, password);
            // green = success message
            System.out.println(ConsoleColor.green("Registration successful!"));
            System.out.println("Your Customer ID: " + ConsoleColor.bold(customerId));
            System.out.println(ConsoleColor.yellow("Please remember this ID for future logins."));
            // save after registration so the new customer persists
            DataPersistence.save(bank);

        } catch (Exception e) {
            // red = error message
            System.out.println(ConsoleColor.red("Registration failed: " + e.getMessage()));
        }
    }

    private void customerLogin() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("         CUSTOMER LOGIN");
        System.out.println("-".repeat(40));

        try {
            String customerId = getStringInput("Customer ID: ");
            String password = getStringInput("Password: ");

            Customer customer = bank.getCustomer(customerId);
            if (customer.verifyPassword(password)) {
                currentCustomerId = customerId;
                // show mini dashboard right after login
                printDashboard(customerId);
            } else {
                System.out.println(ConsoleColor.red("Invalid password!"));
            }
        } catch (BankingException e) {
            System.out.println(ConsoleColor.red("Login failed: " + e.getMessage()));
        }
    }

    private void createAccount() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        CREATE NEW ACCOUNT");
        System.out.println("-".repeat(40));

        try {
            System.out.println("Account Types:");
            System.out.println("1. Savings Account");
            System.out.println("2. Checking Account");
            System.out.println("3. Business Account");

            int typeChoice = getIntInput("Choose account type (1-3): ");
            String accountType = switch (typeChoice) {
                case 1 -> "savings";
                case 2 -> "checking";
                case 3 -> "business";
                default -> throw new BankingException("Invalid account type");
            };

            System.out.println("Currencies: 1=USD, 2=EUR, 3=GBP, 4=JPY, 5=CAD");
            int currencyChoice = getIntInput("Choose currency (1-5): ");
            Currency currency = Currency.values()[currencyChoice - 1];

            double initialBalance = getDoubleInput("Initial deposit amount: ");
            String password = getStringInput("Your password for verification: ");

            String accountNumber;
            if (accountType.equals("business")) {
                String businessName = getStringInput("Business Name: ");
                String taxId = getStringInput("Tax ID: ");
                accountNumber = bank.createAccount(currentCustomerId, accountType, initialBalance, currency, password, businessName, taxId);
            } else {
                accountNumber = bank.createAccount(currentCustomerId, accountType, initialBalance, currency, password);
            }

            System.out.println(ConsoleColor.green("Account created successfully!"));
            System.out.printf("Account Number: %s | Type: %s | Initial Balance: %.2f %s%n",
                    accountNumber, accountType.toUpperCase(), initialBalance, currency);
            // save after new account is created
            DataPersistence.save(bank);

        } catch (Exception e) {
            System.err.println("Account creation failed: " + e.getMessage());
        }
    }

    private void viewMyAccounts() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("                 MY ACCOUNTS");
        System.out.println("-".repeat(60));

        List<Account> accounts = bank.getCustomerAccounts(currentCustomerId);
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts.");
        } else {
            for (Account account : accounts) {
                System.out.println(account);
                if (account instanceof SavingsAccount sa) {
                    System.out.printf("  Interest Rate: %.2f%% | Min Balance: %.2f%n",
                            sa.getInterestRate(), sa.getMinimumBalance());
                } else if (account instanceof BusinessAccount ba) {
                    System.out.printf("  Business: %s | Tax ID: %s%n",
                            ba.getBusinessName(), ba.getTaxId());
                }
                System.out.println();
            }
        }
    }

    private void depositMoney() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("         DEPOSIT MONEY");
        System.out.println("-".repeat(40));

        try {
            String accountNumber = selectMyAccount();
            if (accountNumber == null) return;

            double amount = getDoubleInput("Amount to deposit: ");
            String description = getStringInput("Description (optional): ");

            bank.deposit(accountNumber, amount, description.isEmpty() ? null : description);
            Account account = bank.getAccount(accountNumber);
            // show receipt box after successful deposit
            printReceipt("Deposit", amount, accountNumber, account.getBalance(), account.getCurrency());
            // save after deposit
            DataPersistence.save(bank);

        } catch (Exception e) {
            System.out.println(ConsoleColor.red("Deposit failed: " + e.getMessage()));
        }
    }

    private void withdrawMoney() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        WITHDRAW MONEY");
        System.out.println("-".repeat(40));

        try {
            String accountNumber = selectMyAccount();
            if (accountNumber == null) return;

            Account account = bank.getAccount(accountNumber);
            System.out.printf("Current balance: %.2f %s%n", account.getBalance(), account.getCurrency());
            System.out.printf("Daily limit: %.2f | Available: %.2f%n",
                    account.getDailyTransactionLimit(),
                    account.getDailyTransactionLimit() - account.getMonthlyTransactionTotal());

            double amount = getDoubleInput("Amount to withdraw: ");
            String description = getStringInput("Description (optional): ");

            bank.withdraw(accountNumber, amount, description.isEmpty() ? null : description);
            // show receipt box after successful withdrawal
            printReceipt("Withdrawal", amount, accountNumber, account.getBalance(), account.getCurrency());
            // save after withdrawal
            DataPersistence.save(bank);

        } catch (Exception e) {
            System.out.println(ConsoleColor.red("Withdrawal failed: " + e.getMessage()));
        }
    }

    private void transferMoney() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        TRANSFER MONEY");
        System.out.println("-".repeat(40));

        try {
            String fromAccount = selectMyAccount();
            if (fromAccount == null) return;

            String toAccount = getStringInput("Recipient account number: ");
            double amount = getDoubleInput("Amount to transfer: ");
            String description = getStringInput("Description (optional): ");

            bank.transfer(fromAccount, toAccount, amount, description.isEmpty() ? null : description);
            Account account = bank.getAccount(fromAccount);
            // show receipt box after successful transfer
            printReceipt("Transfer to " + toAccount, amount, fromAccount, account.getBalance(), account.getCurrency());
            // save after transfer
            DataPersistence.save(bank);

        } catch (Exception e) {
            System.out.println(ConsoleColor.red("Transfer failed: " + e.getMessage()));
        }
    }

    private void viewAccountStatement() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("       ACCOUNT STATEMENT");
        System.out.println("-".repeat(40));

        try {
            String accountNumber = selectMyAccount();
            if (accountNumber == null) return;

            System.out.print("Start date (YYYY-MM-DD) or press Enter for last 30 days: ");
            String startDateStr = scanner.nextLine().trim();
            LocalDate startDate = startDateStr.isEmpty() ?
                    LocalDate.now().minusDays(30) : LocalDate.parse(startDateStr);

            System.out.print("End date (YYYY-MM-DD) or press Enter for today: ");
            String endDateStr = scanner.nextLine().trim();
            LocalDate endDate = endDateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(endDateStr);

            String statement = bank.generateAccountStatement(accountNumber, startDate, endDate);
            System.out.println(statement);

            System.out.print("Save statement to file? (y/n): ");
            if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                String filename = "statement_" + accountNumber + "_" + LocalDate.now() + ".txt";
                Files.write(Paths.get(filename), statement.getBytes());
                System.out.println("Statement saved to: " + filename);
            }

        } catch (Exception e) {
            System.err.println("Error generating statement: " + e.getMessage());
        }
    }

    private void applyForLoan() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        LOAN APPLICATION");
        System.out.println("-".repeat(40));

        try {
            String accountNumber = selectMyAccount();
            if (accountNumber == null) return;

            double amount = getDoubleInput("Loan amount: ");
            System.out.println("Loan terms: 12, 24, 36, 48, 60 months");
            int termMonths = getIntInput("Term in months: ");

            double interestRate = switch (termMonths) {
                case 12 -> 5.5;
                case 24 -> 6.0;
                case 36 -> 6.5;
                case 48 -> 7.0;
                case 60 -> 7.5;
                default -> 8.0;
            };

            String loanId = bank.applyForLoan(accountNumber, amount, interestRate, termMonths);
            System.out.printf("Loan application submitted successfully!%n");
            System.out.printf("Loan ID: %s%n", loanId);
            System.out.printf("Amount: %.2f | Rate: %.1f%% | Term: %d months%n",
                    amount, interestRate, termMonths);
            System.out.println("Your application is under review.");

        } catch (Exception e) {
            System.err.println("Loan application failed: " + e.getMessage());
        }
    }

    private void viewMyLoans() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("                  MY LOANS");
        System.out.println("-".repeat(60));

        List<Loan> loans = bank.getCustomerLoans(currentCustomerId);
        if (loans.isEmpty()) {
            System.out.println("You have no loans.");
        } else {
            for (Loan loan : loans) {
                System.out.println(loan);
                System.out.printf("  Payments made: %d | Next payment: %.2f%n",
                        loan.getPayments().size(), loan.getMonthlyPayment());
                System.out.println();
            }
        }
    }

    private void updateProfile() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        UPDATE PROFILE");
        System.out.println("-".repeat(40));

        try {
            Customer customer = bank.getCustomer(currentCustomerId);
            System.out.println("Current Profile:");
            System.out.println(customer);

            System.out.println("\nLeave blank to keep current value:");
            String email = getStringInput("New Email: ");
            String phone = getStringInput("New Phone: ");
            String address = getStringInput("New Address: ");

            if (!email.isEmpty() && ValidationUtils.isValidEmail(email)) {
                customer.setEmail(email);
            }
            if (!phone.isEmpty() && ValidationUtils.isValidPhone(phone)) {
                customer.setPhone(phone);
            }
            if (!address.isEmpty()) {
                customer.setAddress(address);
            }

            System.out.println("Profile updated successfully!");

        } catch (Exception e) {
            System.err.println("Profile update failed: " + e.getMessage());
        }
    }

    private void changePassword() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        CHANGE PASSWORD");
        System.out.println("-".repeat(40));

        try {
            Customer customer = bank.getCustomer(currentCustomerId);
            String currentPassword = getStringInput("Current Password: ");

            if (!customer.verifyPassword(currentPassword)) {
                System.out.println("Invalid current password!");
                return;
            }

            String newPassword = getStringInput("New Password: ");
            String confirmPassword = getStringInput("Confirm New Password: ");

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match!");
                return;
            }

            customer.updatePassword(newPassword);
            System.out.println("Password changed successfully!");

        } catch (Exception e) {
            System.err.println("Password change failed: " + e.getMessage());
        }
    }

    private void adminFunctions() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("       ADMIN FUNCTIONS");
        System.out.println("-".repeat(40));
        System.out.println("1. View Bank Summary");
        System.out.println("2. Generate Monthly Interest");
        System.out.println("3. Charge Maintenance Fees");
        System.out.println("4. View All Customers");
        System.out.println("5. Approve Loans");
        System.out.println("6. Freeze/Unfreeze Account");
        System.out.println("7. Export Bank Data");
        System.out.println("8. Back to Main Menu");

        int choice = getIntInput("Choose option: ");
        switch (choice) {
            case 1 -> bank.displayBankSummary();
            case 2 -> {
                bank.generateMonthlyInterest();
                System.out.println("Monthly interest processed.");
            }
            case 3 -> {
                bank.chargeMaintenanceFees();
                System.out.println("Maintenance fees charged.");
            }
            case 4 -> viewAllCustomers();
            case 5 -> approveLoans();
            case 6 -> freezeUnfreezeAccount();
            case 7 -> exportBankData();
            case 8 -> { /* Return to main menu */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    private void viewAllCustomers() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("                ALL CUSTOMERS");
        System.out.println("-".repeat(60));

        List<Customer> customers = bank.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
                List<Account> accounts = bank.getCustomerAccounts(customer.getCustomerId());
                System.out.printf("  Accounts: %d | Total Balance: %.2f%n",
                        accounts.size(),
                        accounts.stream().mapToDouble(Account::getBalance).sum());
                System.out.println();
            }
        }
    }

    private void approveLoans() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("        APPROVE LOANS");
        System.out.println("-".repeat(40));

        List<Loan> pendingLoans = bank.getAllLoans().stream()
                .filter(loan -> loan.getStatus() == LoanStatus.PENDING)
                .toList();

        if (pendingLoans.isEmpty()) {
            System.out.println("No pending loans.");
            return;
        }

        for (Loan loan : pendingLoans) {
            System.out.println(loan);
            System.out.print("Approve this loan? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.startsWith("y")) {
                try {
                    bank.approveLoan(loan.getLoanId());
                    System.out.println("Loan approved and disbursed!");
                } catch (BankingException e) {
                    System.err.println("Error approving loan: " + e.getMessage());
                }
            }
        }
    }

    private void freezeUnfreezeAccount() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("     FREEZE/UNFREEZE ACCOUNT");
        System.out.println("-".repeat(40));

        try {
            String accountNumber = getStringInput("Account number: ");
            Account account = bank.getAccount(accountNumber);

            System.out.println("Current status: " + account.getStatus());
            System.out.println("1. Freeze Account");
            System.out.println("2. Unfreeze Account");

            int choice = getIntInput("Choose action: ");
            switch (choice) {
                case 1 -> {
                    bank.freezeAccount(accountNumber);
                    System.out.println("Account frozen successfully.");
                }
                case 2 -> {
                    bank.unfreezeAccount(accountNumber);
                    System.out.println("Account unfrozen successfully.");
                }
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void exportBankData() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("       EXPORT BANK DATA");
        System.out.println("-".repeat(40));

        try {
            String filename = "bank_data_export_" + LocalDate.now() + ".txt";
            bank.exportData(filename);
            System.out.println("Bank data exported successfully to: " + filename);
        } catch (IOException e) {
            System.err.println("Export failed: " + e.getMessage());
        }
    }

    private String selectMyAccount() {
        List<Account> accounts = bank.getCustomerAccounts(currentCustomerId);
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts. Please create an account first.");
            return null;
        }

        if (accounts.size() == 1) {
            return accounts.getFirst().getAccountNumber();
        }

        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.printf("%d. %s - Balance: %.2f %s (%s)%n",
                    i + 1, account.getAccountNumber(), account.getBalance(),
                    account.getCurrency(), account.getClass().getSimpleName());
        }

        int choice = getIntInput("Choose account (1-" + accounts.size() + "): ");
        if (choice >= 1 && choice <= accounts.size()) {
            return accounts.get(choice - 1).getAccountNumber();
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    // ========= UI HELPERS =========

    // Prints a mini dashboard showing all accounts right after login
    private void printDashboard(String customerId) {
        try {
            Customer customer = bank.getCustomer(customerId);
            List<Account> accounts = bank.getCustomerAccounts(customerId);
            String line = "─".repeat(50);

            System.out.println("\n" + ConsoleColor.cyan(line));
            System.out.println("  Welcome back, " + ConsoleColor.bold(ConsoleColor.green(customer.getFullName())));
            System.out.println(ConsoleColor.cyan(line));

            if (accounts.isEmpty()) {
                // yellow = informational hint, not an error
                System.out.println(ConsoleColor.yellow("  No accounts yet. Create one from the menu."));
            } else {
                for (Account acc : accounts) {
                    // getSimpleName() returns "SavingsAccount" → remove "Account" → "Savings"
                    String type = acc.getClass().getSimpleName().replace("Account", "");

                    // %-10s = left-align in 10 chars, %, .2f = number with comma separators
                    System.out.printf("  %-10s  %-16s  " + ConsoleColor.yellow("$%,.2f %s") + "%n",
                            type, acc.getAccountNumber(), acc.getBalance(), acc.getCurrency());
                }
            }
            System.out.println(ConsoleColor.cyan(line) + "\n");

        } catch (BankingException e) {
            System.out.println(ConsoleColor.red("Could not load dashboard: " + e.getMessage()));
        }
    }

    // Prints a receipt box after every deposit, withdrawal, or transfer
    private void printReceipt(String type, double amount, String accountNumber, double newBalance, Currency currency) {
        // format the current time as readable string
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String line = "─".repeat(36);

        System.out.println(ConsoleColor.cyan("\n┌" + line + "┐"));
        System.out.println(ConsoleColor.cyan("│") + ConsoleColor.bold("         TRANSACTION RECEIPT          ") + ConsoleColor.cyan("│"));
        System.out.println(ConsoleColor.cyan("├" + line + "┤"));

        // %-10s = left-align label in 10 chars, %-23s = value in 23 chars
        System.out.printf(ConsoleColor.cyan("│") + " %-10s : %-23s" + ConsoleColor.cyan("│%n"), "Type", type);
        System.out.printf(ConsoleColor.cyan("│") + " %-10s : %-23s" + ConsoleColor.cyan("│%n"), "Amount", String.format("$%,.2f %s", amount, currency));
        System.out.printf(ConsoleColor.cyan("│") + " %-10s : %-23s" + ConsoleColor.cyan("│%n"), "Account", accountNumber);

        // green for the new balance to highlight it
        System.out.printf(ConsoleColor.cyan("│") + " %-10s : " + ConsoleColor.green("%-23s") + ConsoleColor.cyan("│%n"), "Balance", String.format("$%,.2f %s", newBalance, currency));
        System.out.printf(ConsoleColor.cyan("│") + " %-10s : %-23s" + ConsoleColor.cyan("│%n"), "Time", time);

        System.out.println(ConsoleColor.cyan("└" + line + "┘\n"));
    }

    // ========= INPUT HELPERS =========
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Please enter a value.");
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Please enter a value.");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    // ========= UTILITY METHODS =========
    public void runDailyMaintenance() {
        System.out.println("Running daily maintenance tasks...");

        // Credit monthly interest for saving accounts
        bank.generateMonthlyInterest();

        // Charge maintenance fees
        bank.chargeMaintenanceFees();

        // Reset daily transaction limits (in a real system, this would be scheduled)
        System.out.println("Daily maintenance completed.");
        LOGGER.info("Daily maintenance tasks completed");
    }

    public void generateSystemReport() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                        COMPREHENSIVE SYSTEM REPORT");
        System.out.println("=".repeat(80));

        bank.displayBankSummary();

        // Transaction volume analysis
        List<Account> allAccounts = bank.getAllAccounts();
        int totalTransactions = allAccounts.stream()
                .mapToInt(account -> account.getTransactions().size())
                .sum();

        double avgTransactionsPerAccount = allAccounts.isEmpty() ? 0 :
                (double) totalTransactions / allAccounts.size();

        System.out.println("\nTRANSACTION ANALYSIS:");
        System.out.println("-".repeat(40));
        System.out.printf("Total Transactions: %d%n", totalTransactions);
        System.out.printf("Average Transactions per Account: %.2f%n", avgTransactionsPerAccount);

        // Account status distribution
        Map<AccountStatus, Long> statusCount = allAccounts.stream()
                .collect(Collectors.groupingBy(Account::getStatus, Collectors.counting()));

        System.out.println("\nACCOUNT STATUS DISTRIBUTION:");
        System.out.println("-".repeat(40));
        statusCount.forEach((status, count) ->
                System.out.printf("%s: %d accounts%n", status, count));

        // Currency distribution
        Map<Currency, Long> currencyCount = allAccounts.stream()
                .collect(Collectors.groupingBy(Account::getCurrency, Collectors.counting()));

        System.out.println("\nCURRENCY DISTRIBUTION:");
        System.out.println("-".repeat(40));
        currencyCount.forEach((currency, count) ->
                System.out.printf("%s: %d accounts%n", currency, count));

        // Loan analysis
        List<Loan> allLoans = bank.getAllLoans();
        if (!allLoans.isEmpty()) {
            Map<LoanStatus, Long> loanStatusCount = allLoans.stream()
                    .collect(Collectors.groupingBy(Loan::getStatus, Collectors.counting()));

            System.out.println("\nLOAN STATUS DISTRIBUTION:");
            System.out.println("-".repeat(40));
            loanStatusCount.forEach((status, count) ->
                    System.out.printf("%s: %d loans%n", status, count));

            double avgLoanAmount = allLoans.stream()
                    .mapToDouble(Loan::getPrincipalAmount)
                    .average()
                    .orElse(0.0);

            System.out.printf("Average Loan Amount: $%.2f%n", avgLoanAmount);
        }

        System.out.println("=".repeat(80));
        System.out.println("Report generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(80));
    }

    // ========= DEMO DATA METHODS =========
    public void loadDemoData() {
        System.out.println("Loading demo data...");

        try {
            // Create demo customers
            String customer1 = bank.registerCustomer("John", "Doe", "john.doe@email.com",
                    "+1234567890", "123 Main St, City, State", LocalDate.of(1985, 5, 15), "password123");

            String customer2 = bank.registerCustomer("Jane", "Smith", "jane.smith@email.com",
                    "+1987654321", "456 Oak Ave, City, State", LocalDate.of(1990, 8, 22), "password456");

            String customer3 = bank.registerCustomer("Bob", "Johnson", "bob.johnson@email.com",
                    "+1122334455", "789 Pine St, City, State", LocalDate.of(1978, 12, 3), "password789");

            // Create demo accounts
            String account1 = bank.createAccount(customer1, "savings", 5000.0, Currency.USD, "password123");
            String account2 = bank.createAccount(customer1, "checking", 2500.0, Currency.USD, "password123");
            String account3 = bank.createAccount(customer2, "savings", 7500.0, Currency.EUR, "password456");
            String account4 = bank.createAccount(customer2, "business", 15000.0, Currency.USD, "password456",
                    "Smith Consulting LLC", "TAX123456789");
            String account5 = bank.createAccount(customer3, "checking", 3200.0, Currency.CAD, "password789");

            // Create some transactions
            bank.deposit(account1, 1000.0, "Payroll deposit");
            bank.withdraw(account2, 500.0, "ATM withdrawal");
            bank.transfer(account1, account3, 300.0, "International transfer");
            bank.deposit(account4, 5000.0, "Client payment");
            bank.withdraw(account5, 200.0, "Online purchase");

            // Apply for demo loans
            String loan1 = bank.applyForLoan(account1, 10000.0, 6.5, 36);
            String loan2 = bank.applyForLoan(account4, 25000.0, 7.0, 48);

            // Approve loans
            bank.approveLoan(loan1);
            bank.approveLoan(loan2);

            // Credit interest
            bank.generateMonthlyInterest();

            System.out.println("Demo data loaded successfully!");
            System.out.println("\nDemo Login Credentials:");
            System.out.println("Customer 1: " + customer1 + " / password123");
            System.out.println("Customer 2: " + customer2 + " / password456");
            System.out.println("Customer 3: " + customer3 + " / password789");

        } catch (Exception e) {
            System.err.println("Error loading demo data: " + e.getMessage());
        }
    }

    // ========= STRESS TEST METHODS =========
    public void runStressTest() {
        System.out.println("Running system stress test...");
        long startTime = System.currentTimeMillis();

        try {
            // Create multiple customers and accounts
            List<String> customerIds = new ArrayList<>();
            List<String> accountNumbers = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                String customerId = bank.registerCustomer("User" + i, "Test" + i,
                        "user" + i + "@test.com", "+123456789" + i,
                        "Address " + i, LocalDate.of(1980 + (i % 40), (i % 12) + 1, (i % 28) + 1),
                        "password" + i);
                customerIds.add(customerId);

                String accountNumber = bank.createAccount(customerId, "savings", 1000.0 + (i * 100),
                        Currency.USD, "password" + i);
                accountNumbers.add(accountNumber);
            }

            // Perform random transactions
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                String accountNumber = accountNumbers.get(random.nextInt(accountNumbers.size()));
                double amount = 10.0 + (random.nextDouble() * 500.0);

                try {
                    if (random.nextBoolean()) {
                        bank.deposit(accountNumber, amount, "Stress test deposit");
                    } else {
                        bank.withdraw(accountNumber, amount, "Stress test withdrawal");
                    }
                } catch (Exception e) {
                    // Expected - some withdrawals may fail due to insufficient funds
                }
            }

            // Perform transfers
            for (int i = 0; i < 200; i++) {
                String fromAccount = accountNumbers.get(random.nextInt(accountNumbers.size()));
                String toAccount = accountNumbers.get(random.nextInt(accountNumbers.size()));
                double amount = 10.0 + (random.nextDouble() * 200.0);

                try {
                    bank.transfer(fromAccount, toAccount, amount, "Stress test transfer");
                } catch (Exception e) {
                    // Expected - some transfers may fail
                }
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("Stress test completed!");
            System.out.printf("Duration: %d ms%n", duration);
            System.out.printf("Created: %d customers, %d accounts%n", customerIds.size(), accountNumbers.size());
            System.out.println("Performed 1000 deposits/withdrawals and 200 transfers");

            // Clean up test data
            System.out.print("Clean up test data? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                // In a real system, you would implement cleanup methods
                System.out.println("Test data cleanup would be performed here.");
            }

        } catch (Exception e) {
            System.err.println("Stress test failed: " + e.getMessage());
        }
    }

    // ========= MAIN METHOD =========
    public static void main(String[] args) {
        BankManagementSystem system = new BankManagementSystem();

        // Check for command line arguments
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "--demo" -> {
                    system.loadDemoData();
                    system.start();
                }
                case "--stress" -> system.runStressTest();
                case "--report" -> {
                    system.loadDemoData();
                    system.generateSystemReport();
                }
                case "--maintenance" -> system.runDailyMaintenance();
                case "--help" -> {
                    System.out.println("Enhanced Bank Management System v2.0");
                    System.out.println("Usage: java EnhancedBankManagementSystem [option]");
                    System.out.println("Options:");
                    System.out.println("  --demo        Load demo data and start interactive mode");
                    System.out.println("  --stress      Run stress test");
                    System.out.println("  --report      Generate comprehensive system report");
                    System.out.println("  --maintenance Run daily maintenance tasks");
                    System.out.println("  --help        Show this help message");
                    System.out.println("  (no option)   Start in normal interactive mode");
                }
                default -> {
                    System.out.println("Unknown option: " + args[0]);
                    System.out.println("Use --help for available options");
                }
            }
        } else {
            // Normal startup
            system.start();
        }
    }
}