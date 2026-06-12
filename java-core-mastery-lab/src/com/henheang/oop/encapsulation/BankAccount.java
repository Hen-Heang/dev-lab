package com.henheang.oop.encapsulation;

/**
 * BankAccount class demonstrates ENCAPSULATION in OOP.
 * <p>
 * What is Encapsulation?
 *   Encapsulation = wrapping data (fields) + behavior (methods) together in one class,
 *   and HIDING the data so outside code cannot touch it directly.
 * <p>
 * Real-world analogy — ATM:
 *   - You cannot open the ATM and take money directly
 *   - You MUST use the buttons (methods): deposit, withdraw
 *   - The ATM checks rules before doing anything (e.g. enough balance?)
 * <p>
 * In Java, we achieve encapsulation by:
 *   1. Making fields "private"  → nobody outside can read/write them directly
 *   2. Providing "public" methods → controlled ways to interact with the data
 */
public class BankAccount {

    // ==========================================================================
    // FIELDS (also called "instance variables" or "attributes")
    //
    // A field is a variable that belongs to an OBJECT (not a method).
    // Every BankAccount object has its OWN copy of these 3 variables.
    //
    // "private" keyword means:
    //   - Only code INSIDE this class (BankAccount) can use these variables
    //   - Code in OTHER classes (like BankAccountDemo) CANNOT do:
    //       account.balance = 9999;   <-- COMPILE ERROR, not allowed!
    //   - This is the whole point of encapsulation — protect the data!
    //
    // "final" keyword means:
    //   - The value is set ONCE and can NEVER be changed after that
    //   - owner and accountNumber use "final" because they never change
    //   - balance does NOT use "final" because it changes every deposit/withdraw
    // ==========================================================================

    private final String owner;         // Who owns this account (e.g. "Hen Heang") — never changes
    private double balance;             // Current money in the account — changes often
    private final String accountNumber; // Unique account ID (e.g. "ACC-123456789") — never changes

    // ==========================================================================
    // CONSTRUCTOR
    //
    // A constructor is a SPECIAL method with the same name as the class.
    // It runs AUTOMATICALLY when you create a new object with "new".
    //
    // Example:
    //   BankAccount account = new BankAccount("Hen Heang", "ACC-123456789", 1000.0);
    //                                          ↑ owner      ↑ accountNumber   ↑ initialBalance
    //
    // Java sees "new BankAccount(...)" and immediately calls this constructor.
    // The constructor's job is to SET UP the object's initial state.
    //
    // Parameters:
    //   - String owner          → the person's name, passed in from outside
    //   - String accountNumber  → the account ID, passed in from outside
    //   - double initialBalance → the starting balance, passed in from outside
    // ==========================================================================

    public BankAccount(String owner, String accountNumber, double initialBalance) {

        // "this.owner" = the FIELD declared above (private final String owner)
        // "owner"      = the PARAMETER given to this constructor
        // We copy the parameter value INTO the field so the object remembers it.
        this.owner = owner;

        // Same pattern: copy accountNumber parameter into the accountNumber field
        this.accountNumber = accountNumber;

        // Store the initial balance passed in.
        // Note: ideally we should validate this too (e.g. reject negative values),
        // but for simplicity we store it directly here.
        this.balance = initialBalance;
    }

    // ==========================================================================
    // SETTER METHOD — setBalance()
    //
    // A "setter" is a method that lets you CHANGE a private field's value.
    // Instead of allowing direct access (account.balance = -500), we force
    // all changes to go through this method, which can enforce rules.
    //
    // WARNING: This method has a bug — it sets the balance BEFORE checking
    // if it's negative. The check should come FIRST. But this is the current code.
    //
    // "public" = any class can call this method
    // "void"   = this method does not return any value
    // ==========================================================================

    public void setBalance(double balance) {
        // BUG: balance is set here first, then checked below
        // If balance is -100, it gets stored before the exception is thrown
        this.balance = balance;

        // Validate AFTER setting — this is the wrong order, but it's what's written
        if (balance < 0) {
            // IllegalArgumentException is a built-in Java error for bad input values
            // "throw" means: stop everything and report this error to the caller
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    // ==========================================================================
    // deposit() METHOD — Add money to the account
    //
    // "public"  = any class can call this method
    // "void"    = returns nothing
    // "double amount" = how much money to add (e.g. 500.0)
    //
    // This method has TWO responsibilities:
    //   1. VALIDATE the input (reject zero or negative amounts)
    //   2. UPDATE the balance if the input is valid
    // ==========================================================================

    public void deposit(double amount) {

        // Rule: you cannot deposit 0 or a negative number
        // "<= 0" means: less than zero OR equal to zero
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return; // "return" here exits the method immediately — nothing more happens
        }

        // If we reach here, amount is valid (greater than 0)
        // "+=" means: balance = balance + amount
        balance += amount;

        // Print a confirmation message showing what was deposited and the new balance
        System.out.println("Deposited: $ " + amount + ". New balance: $ " + balance);
    }

    // ==========================================================================
    // withdraw() METHOD — Remove money from the account
    //
    // Similar to deposit(), but in reverse — it SUBTRACTS from the balance.
    // It has an extra rule: you cannot withdraw more than you have.
    // ==========================================================================

    public void withdraw(double amount) {

        // Rule 1: withdrawal amount must be positive
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return; // exit the method immediately
        }

        // Rule 2: you cannot withdraw more than the current balance
        // If amount > balance, you don't have enough money
        if (amount > balance) {
            System.out.println("Insufficient funds. Current balance: $ " + balance);
            return; // exit the method immediately, balance is NOT changed
        }

        // If we reach here, both rules passed — safe to subtract
        // "-=" means: balance = balance - amount
        balance -= amount;

        // Print confirmation
        System.out.println("Withdrew: $ " + amount + ". New balance: $ " + balance);
    }

    // ==========================================================================
    // GETTER METHODS
    //
    // Because fields are "private", outside code cannot read them directly.
    // Getters are "public" methods that return the field value safely.
    //
    // Example from outside this class:
    //   account.owner        <-- NOT ALLOWED (private)
    //   account.getOwner()   <-- ALLOWED (public getter)
    //
    // "String" before the method name = the return type (what the method gives back)
    // "return owner" = send the value of the owner field back to whoever called this
    // ==========================================================================

    // Returns the owner's name
    public String getOwner() {
        return owner; // gives back the value of the private "owner" field
    }

    // Returns the current balance
    public double getBalance() {
        return balance; // gives back the value of the private "balance" field
    }

    // Returns the account number
    public String getAccountNumber() {
        return accountNumber; // gives back the value of the private "accountNumber" field
    }

}