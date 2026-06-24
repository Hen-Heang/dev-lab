package com.henheang.oop.encapsulation;

// Scanner is a built-in Java class that lets us READ input from the user (keyboard).
// We must import it at the top before we can use it.
// "java.util.Scanner" = the full location of Scanner inside Java's standard library
import java.util.Scanner;

/**
 * BankAccountDemo is the "runner" class — it shows how to USE BankAccount.
 * <p>
 * Think of it like this:
 *   - BankAccount.java = the blueprint of a bank account (rules + data)
 *   - BankAccountDemo.java = a person actually USING that bank account
 * <p>
 * This class only has a main() method, which is the entry point Java runs first.
 */
public class BankAccountDemo {

    // ==========================================================================
    // main() METHOD
    //
    // This is the starting point of the program.
    // When you run the program, Java looks for:
    //   public static void main(String[] args)
    // and begins execution here, line by line, top to bottom.
    //
    // "public"          = accessible from anywhere
    // "static"          = belongs to the class, not an object (no "new" needed)
    // "void"            = returns nothing
    // "String[] args"   = command-line arguments (we don't use them here)
    // ==========================================================================

    public static void main(String[] args) {

        // ----------------------------------------------------------------------
        // STEP 1: Create a Scanner to read user input from the keyboard
        //
        // "System.in"              = represents the keyboard (standard input)
        // "new Scanner(System.in)" = creates a Scanner that listens to keyboard
        // "scanner"                = variable name we use to call Scanner methods
        // ----------------------------------------------------------------------
        Scanner scanner = new Scanner(System.in);

        // ----------------------------------------------------------------------
        // STEP 2: Ask the user to type the owner's name
        //
        // System.out.print()  = prints text WITHOUT moving to a new line,
        //                        so the user types on the same line
        // scanner.nextLine()  = waits for the user to press Enter,
        //                        then returns everything they typed as a String
        // ----------------------------------------------------------------------
        System.out.print("Enter owner name       : ");
        String owner = scanner.nextLine(); // e.g. user types: Hen Heang

        // ----------------------------------------------------------------------
        // STEP 3: Ask the user to type the account number
        //
        // Same pattern — nextLine() reads a full line of text input
        // ----------------------------------------------------------------------
        System.out.print("Enter account number   : ");
        String accountNumber = scanner.nextLine(); // e.g. user types: ACC-123456789

        // ----------------------------------------------------------------------
        // STEP 4: Ask the user to type the initial balance
        //
        // scanner.nextDouble() = reads a decimal number (double) from the keyboard
        //                        e.g. user types: 1000.0
        //
        // We use nextDouble() instead of nextLine() because balance is a number,
        // not text — Java needs to store it as a double, not a String.
        // ----------------------------------------------------------------------
        System.out.print("Enter initial balance  : ");
        double initialBalance = scanner.nextDouble(); // e.g. user types: 1000.0

        // ----------------------------------------------------------------------
        // STEP 5: Create a new BankAccount object using the values the user typed
        //
        // Now instead of hardcoded values like "Hen Heang", we pass in whatever
        // the user entered at runtime — this is called "dynamic input".
        //
        // "new BankAccount(...)" calls the constructor in BankAccount.java.
        // "account" is a REFERENCE VARIABLE — it points to the object in memory.
        // ----------------------------------------------------------------------
        BankAccount account = new BankAccount(owner, accountNumber, initialBalance);

        // Print the account info
        // (shows custom text if BankAccount has a toString() method, otherwise prints memory address)
        System.out.println("My Bank Account Info : " + account);

        // ----------------------------------------------------------------------
        // STEP 6: Deposit $500
        //
        // Calls the deposit() method in BankAccount with amount = 500.0
        // Inside deposit():
        //   - Checks if 500 > 0 → YES, valid
        //   - balance = initialBalance + 500
        //   - Prints: "Deposited: $ 500.0. New balance: $ ..."
        // ----------------------------------------------------------------------
        account.deposit(500.0);

        // ----------------------------------------------------------------------
        // STEP 7: Withdraw $200
        //
        // Calls withdraw() with amount = 200.0
        // Inside withdraw():
        //   - Checks if 200 > 0 → YES
        //   - Checks if 200 > balance → depends on current balance
        //   - If enough: balance = balance - 200
        // ----------------------------------------------------------------------
        account.withdraw(200.0);

        // ----------------------------------------------------------------------
        // STEP 8: Try to withdraw $1500 (maybe more than the balance)
        //
        // Inside withdraw():
        //   - If 1500 > current balance → prints "Insufficient funds"
        //   - balance stays unchanged
        // ----------------------------------------------------------------------
        account.withdraw(1500.0);

        // ----------------------------------------------------------------------
        // STEP 9: Try to deposit a negative amount (-$100)
        //
        // Inside deposit():
        //   - Checks if -100 <= 0 → YES (invalid!)
        //   - Prints: "Deposit amount must be positive."
        //   - balance stays unchanged
        // ----------------------------------------------------------------------
        account.deposit(-100.0);

        // ----------------------------------------------------------------------
        // STEP 10: Print the account info again after all operations
        // ----------------------------------------------------------------------
        System.out.println("After transactions : " + account);

        // ----------------------------------------------------------------------
        // STEP 11: Print just the final balance using the getter method
        //
        // account.getBalance() = calls the public getter in BankAccount.java
        //                         which returns the private "balance" field safely
        // We CANNOT do account.balance directly — that is a compiler error!
        // ----------------------------------------------------------------------
        System.out.println("Final Balance      : " + account.getBalance());

        // ----------------------------------------------------------------------
        // STEP 12: Close the scanner when we are done
        //
        // Good practice: always close the Scanner after use to free up resources.
        // Like closing a file after reading it.
        // ----------------------------------------------------------------------
        scanner.close();
    }
}