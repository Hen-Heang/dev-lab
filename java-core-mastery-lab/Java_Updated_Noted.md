# Java Fundamental Concepts — Complete Guide
> Author: Heang | Goal: Master Java for Spring Boot development
> Every concept: What → Why → When → Real example from this project

---

## Table of Contents

1. [Class](#1-class)
2. [Object](#2-object)
3. [Constructor](#3-constructor)
4. [Parameter & Argument](#4-parameter--argument)
5. [Method](#5-method)
6. [return](#6-return)
7. [void](#7-void)
8. [Access Modifiers](#8-access-modifiers-public-private-protected-default)
9. [static](#9-static)
10. [final](#10-final)
11. [this keyword](#11-this-keyword)
12. [super keyword](#12-super-keyword)
13. [abstract](#13-abstract)
14. [interface](#14-interface)
15. [extends](#15-extends)
16. [implements](#16-implements)
17. [@Override](#17-override)
18. [instanceof](#18-instanceof)
19. [Casting](#19-casting)
20. [Generics](#20-generics)
21. [Collections](#21-collections)
22. [Exception Handling](#22-exception-handling)
23. [Lambda](#23-lambda)
24. [Stream](#24-stream)
25. [Optional](#25-optional)
26. [Annotation](#26-annotation)
27. [Enum](#27-enum)

---

## 1. Class

### What
A class is a **blueprint** — it defines what fields (data) and methods (behavior) an object will have.

### Why
Without a class, you cannot create objects. Everything in Java lives inside a class.

### When
Every time you model a real-world concept: User, Account, Payment, Transaction.

### Real example — `Account.java`
```java
// models/Account.java
public class Account implements Serializable {

    protected final String accountNumber;   // field — data
    protected double balance;               // field — data
    protected AccountStatus status;         // field — data

    public boolean deposit(double amount, String description) throws BankingException {
        // method — behavior
    }
}
```
`Account` is the blueprint. Every `SavingsAccount`, `CheckingAccount`, `BusinessAccount`
is an object created from a child of this blueprint.

---

## 2. Object

### What
An object is a **real instance created from a class** — it lives in heap memory with its own data.

### Why
A class is just a definition. An object is the actual thing you use at runtime.

### When
Any time you write `new ClassName(...)`.

### Real example — `BankManagementSystem.java`
```java
// Creating objects from classes
Customer customer = new Customer("C001", "Heang", "heang@gmail.com", "012345678");
SavingsAccount account = new SavingsAccount("ACC-001", customer.getId(), 1000.0, Currency.USD, 3.5);

// Each object has its own independent data
account.deposit(500.0, "salary");
account.getBalance();   // 1500.0
```

---

## 3. Constructor

### What
A constructor is **code that runs when you create an object** — it sets the starting values.
Same name as the class, no return type.

### Why
Without a constructor, fields are null/0 and the object starts in an invalid state.
A constructor **forces** you to provide required values at creation time.

### When
Every class that needs initial data should have a constructor.

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
public SavingsAccount(String accountNumber, String customerId,
                      double initialBalance, Currency currency, double interestRate) {
    super(accountNumber, customerId, initialBalance, currency); // build parent first
    this.interestRate = interestRate;                           // then child fields
    this.lastInterestCredit = LocalDate.now();
}
```

### Rules
```
1. Same name as class
2. No return type (not even void)
3. super() must be first line in child class
4. If you write any constructor, Java removes the default no-arg constructor
```

---

## 4. Parameter & Argument

### What
- **Parameter** = the variable slot in the method/constructor definition
- **Argument** = the actual value you pass when calling

### Why
Parameters make methods reusable — the same method works with different inputs.

### When
Every method or constructor that needs input data uses parameters.

### Real example — `ValidationUtils.java`
```java
// utilities/ValidationUtils.java
//                     ↓ parameter
public static boolean isValidAmount(double amount) {
    return amount > 0 && amount <= 1_000_000;
}

//                   ↓ argument
isValidAmount(500.0);    // 500.0 fills the "amount" slot
isValidAmount(-10.0);    // -10.0 fills the "amount" slot
```

```
Parameter = the blank slot in the definition: (double amount)
Argument  = what fills the slot at call time:  500.0
```

---

## 5. Method

### What
A method is a **named block of code that does one job** — defined once, called many times.

### Why
Without methods, you repeat the same code everywhere. One bug fix would need changes in many places.

### When
Any reusable logic — calculate, validate, save, find, print.

### Real example — `Account.java`
```java
// models/Account.java
//  access  return   name           parameters
//    ↓       ↓       ↓                 ↓
    public boolean deposit(double amount, String description) throws BankingException {
        validateTransaction(amount);
        balance += amount;
        addTransaction(TransactionType.DEPOSIT, amount, description, null);
        return true;
    }
```

One `deposit()` method used by SavingsAccount, CheckingAccount, BusinessAccount — no duplication.

---

## 6. `return`

### What
`return` **sends a value back to the caller** and stops the method immediately.

### Why
Without `return`, the result of a calculation is thrown away — the caller gets nothing.

### When
Every non-void method must return a value. Also used in void methods to stop early.

### Real example — `ValidationUtils.java`
```java
// utilities/ValidationUtils.java
public static boolean isValidAmount(double amount) {
    return amount > 0 && amount <= 1_000_000;
    //     ↑ sends true or false back to caller
}

// Usage
if (ValidationUtils.isValidAmount(500.0)) {   // receives the returned boolean
    account.deposit(500.0, "salary");
}
```

### Stop method early
```java
// models/Account.java
protected void validateTransaction(double amount) throws BankingException {
    if (status == AccountStatus.FROZEN) {
        throw new InvalidTransactionException("Account is frozen");
        // return happens automatically after throw
    }
    if (status == AccountStatus.CLOSED) {
        throw new InvalidTransactionException("Account is closed");
    }
    // only reaches here if both checks pass
}
```

---

## 7. `void`

### What
`void` means **the method does a job but gives nothing back**.

### Why
Not every method needs to return a value. Some just perform an action — print, save, send.

### When
Use `void` when the caller does not need a result back.

### Real example — `Account.java`
```java
// models/Account.java
public void freeze() {
    this.status = AccountStatus.FROZEN;   // just changes state, no return needed
}

public void close() {
    this.status = AccountStatus.CLOSED;   // just changes state, no return needed
}
```

```
void method   → does action, gives nothing back  → freeze(), close(), print()
return method → calculates and gives back result → getBalance(), isValidAmount()
```

---

## 8. Access Modifiers: `public` `private` `protected` `default`

### What
Access modifiers **control who can see and use a field or method**.

### Why
Without access control, any code anywhere can change your data directly — bugs are impossible to track.
Hiding data and exposing only what is needed is called **encapsulation**.

### When
- Fields → always `private`
- Methods used by everyone → `public`
- Methods only for child classes → `protected`
- Methods only inside same package → default (no keyword)

### Real example — `Account.java`
```java
// models/Account.java
public class Account {

    protected final String accountNumber;   // protected — child classes need this
    protected double balance;               // protected — SavingsAccount uses balance directly
    private final AtomicLong transactionCounter;  // private — internal only

    public double getBalance() { return balance; }           // public — everyone reads balance
    public boolean deposit(...) throws BankingException {...} // public — anyone can deposit

    protected void validateTransaction(double amount) {...}  // protected — child classes call this
    protected void addTransaction(...) {...}                 // protected — child classes call this
}
```

```
Accessible from:          public  protected  default  private
Same class                  ✅       ✅         ✅       ✅
Same package                ✅       ✅         ✅       ❌
Child class (diff package)  ✅       ✅         ❌       ❌
Outside world               ✅       ❌         ❌       ❌
```

---

## 9. `static`

### What
`static` means **belongs to the class itself**, not to any specific object.
All objects share one copy of a static field or method.

### Why
Some things do not need an object — utility methods, constants, shared counters.
Creating an object just to call a utility method is wasteful.

### When
- Utility/helper methods that need no object state
- Constants (`static final`)
- Shared counters or shared config

### Real example — `ValidationUtils.java`
```java
// utilities/ValidationUtils.java
public class ValidationUtils {

    // static final = constant — one copy, never changes
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    // static method — call without creating object
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 1_000_000;
    }
}

// Usage — no new ValidationUtils() needed
ValidationUtils.isValidEmail("heang@gmail.com");
ValidationUtils.isValidAmount(500.0);
```

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
private static final double MINIMUM_BALANCE = 500.0;   // constant — all savings accounts share this
private static final double DAILY_LIMIT     = 5000.0;
private static final double MONTHLY_LIMIT   = 50000.0;
```

---

## 10. `final`

### What
`final` means **cannot be changed after it is set**.

### Why
Prevents accidental changes to values that should never change — account numbers, constants, injected dependencies.

### When
- Fields that must not change after construction
- Constants (`static final`)
- Methods child classes must not override
- Spring Boot: all injected dependencies

### Real example — `Account.java`
```java
// models/Account.java
protected final String accountNumber;   // set once in constructor, never changes
protected final String customerId;      // a customer ID never changes
protected final Currency currency;      // currency never changes
protected final LocalDateTime createdDate;

public Account(String accountNumber, String customerId, ...) {
    this.accountNumber = accountNumber;  // set once here
    // accountNumber = "NEW"; ← ❌ ERROR — final cannot be reassigned
}
```

### Real example — `Payment.java` (your exercise)
```java
// oop/exercise/Payment.java
public final void processPayment() {   // final method — child cannot override this flow
    System.out.printf("[%s] ", getPaymentType());
    execute();
}
```

```
final variable  = cannot reassign
final field     = set in constructor, locked forever
final method    = child cannot override
final class     = nobody can extend
static final    = constant — shared + never changes
```

---

## 11. `this` keyword

### What
`this` refers to **the current object being used right now**.

### Why
When a parameter and a field share the same name, Java cannot tell them apart without `this`.

### When
- In constructor: `this.field = parameter`
- To call another constructor: `this(...)`
- To pass current object to another method

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
public SavingsAccount(String accountNumber, String customerId,
                      double initialBalance, Currency currency, double interestRate) {
    super(accountNumber, customerId, initialBalance, currency);
    this.interestRate = interestRate;        // this.interestRate = field
    //  ↑                  ↑
    // object's field    parameter (same name)
    this.lastInterestCredit = LocalDate.now();
}
```

### Real example — `Computer.java` (Builder pattern)
```java
// design_pattern/builder/Computer.java
public ComputerBuilder setGraphicsCardEnabled(boolean isGraphicsCardEnabled) {
    this.isGraphicsCardEnabled = isGraphicsCardEnabled;
    return this;   // returns the current object — enables method chaining
}
```

---

## 12. `super` keyword

### What
`super` means **access the parent class** — its constructor, fields, or methods.

### Why
Child class must build the parent before building itself.
Also used to call parent method when child overrides it but still needs parent logic.

### When
- `super(...)` — in child constructor, always first line
- `super.method()` — call parent's version of an overridden method
- Custom exceptions passing message up

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
public SavingsAccount(String accountNumber, String customerId,
                      double initialBalance, Currency currency, double interestRate) {
    super(accountNumber, customerId, initialBalance, currency);
    //    ↑ calls Account's constructor first — sets accountNumber, customerId, balance, currency
    this.interestRate = interestRate;   // then SavingsAccount adds its own field
}
```

### Real example — `InsufficientFundsException.java`
```java
// exceptions/InsufficientFundsException.java
public class InsufficientFundsException extends BankingException {
    public InsufficientFundsException(String message) {
        super(message);   // pass message up to BankingException → RuntimeException
    }
}
```

```
this  = look at myself (current object)
super = look at my parent class
```

---

## 13. `abstract`

### What
`abstract` marks a class or method as **incomplete on purpose** — child classes must complete it.
You cannot create an object directly from an abstract class.

### Why
Forces every child class to implement required behavior.
Bug caught at **compile time** — not runtime.

### When
Use `abstract` when:
- Multiple child classes share some code but each does something differently
- You want Java to enforce that every child implements certain methods

### Real example — `Account.java`
```java
// models/Account.java — acts as an abstract base (uses protected methods children override)
protected double getDefaultDailyLimit()   { return 0; }      // children override
protected double getDefaultMonthlyLimit() { return 0; }
protected double getMinimumBalance()      { return 0; }
protected boolean canWithdraw(double amount) { return false; }
```

### Real example — your exercise `Payment.java`
```java
// oop/exercise/Payment.java
public abstract class Payment {

    // concrete — shared by all children, runs the flow
    public final void processPayment() {
        System.out.printf("[%s] ", getPaymentType());
        execute();
    }

    // abstract — each child implements differently
    public abstract String getPaymentType();   // CreditCard → "CREDIT CARD"
    protected abstract void execute();         // CreditCard → prints card info
    public abstract double getFee();           // CreditCard → amount * 0.02
}
```

```
Abstract class =
    concrete methods  → "here is code you get for free"
    abstract methods  → "you MUST write this yourself"

Cannot do: new Payment()          ← abstract, no instances
Can do:    new CreditCardPayment() ← concrete child
```

---

## 14. `interface`

### What
An interface is a **pure contract** — defines what a class must do, but not how.
No fields (only constants), no constructors, no state.

### Why
- One class can implement many interfaces (unlike extends — only one class)
- Enables polymorphism across unrelated classes
- Spring Boot uses interfaces everywhere for flexibility and testability

### When
Use interface when:
- You need a contract/guarantee of behavior
- Multiple unrelated classes share a capability
- You want to swap implementations easily (e.g., mock in tests)

### Real example — your exercise `Refundable.java`
```java
// oop/exercise/Refundable.java
public interface Refundable {
    void refund(double amount);                     // contract — must implement
    double getAmount();                             // contract — must implement

    default String getRefundPolicy() {             // default — free, can override
        return "Standard refund policy: refund within 30 days";
    }
}

// CreditCardPayment CAN be refunded → implements Refundable
public class CreditCardPayment extends Payment implements Refundable { ... }

// CashPayment CANNOT be refunded → does NOT implement Refundable
public class CashPayment extends Payment { ... }
```

### Real example — Spring Boot
```java
// JpaRepository is an interface — Spring generates the implementation
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

```
Abstract class = IS-A + shared code + shared state
Interface      = CAN-DO + pure contract

SavingsAccount extends Account    → "IS-A Account"
CreditCardPayment implements Refundable → "CAN-DO refund"
```

---

## 15. `extends`

### What
`extends` means **this class inherits everything from the parent class** — fields, methods, behavior.

### Why
Avoids duplicated code. Fix a bug in the parent — all children are fixed automatically.

### When
When multiple classes share common fields and behavior, extract them into a parent and `extends` it.

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
public class SavingsAccount extends Account {
    //                               ↑
    //              inherits: accountNumber, balance, currency,
    //                        deposit(), withdraw(), freeze(), close() ...

    private static final double MINIMUM_BALANCE = 500.0;  // adds its own data
    private final double interestRate;

    @Override
    public boolean canWithdraw(double amount) {            // changes parent behavior
        return (balance - amount >= MINIMUM_BALANCE);
    }

    public void creditMonthlyInterest() { ... }            // adds new behavior
}
```

```
Account (parent)
    ├── SavingsAccount  extends Account  → adds interestRate, creditMonthlyInterest()
    ├── CheckingAccount extends Account  → adds overdraftLimit
    └── BusinessAccount extends Account  → adds businessName
```

---

## 16. `implements`

### What
`implements` means **this class signs a contract** — promises to provide all methods defined in the interface.

### Why
A class can implement many interfaces but only extend one class.
`implements` = the class "can do" something.

### When
When a class needs to fulfill a capability contract — Serializable, Refundable, Comparable, UserDetails.

### Real example — `Account.java`
```java
// models/Account.java
public class Account implements Serializable {
    //                         ↑
    //          signs contract to be serializable (saveable to file/network)

    @Serial
    private static final long serialVersionUID = 1L;  // required by Serializable contract
}
```

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
public class SavingsAccount extends Account implements Serializable {
    //  extends one class ↑       implements one interface ↑
    //  can extend + implement at the same time
}
```

---

## 17. `@Override`

### What
`@Override` tells the compiler **"I am intentionally replacing a method from the parent or interface."**

### Why
Without `@Override`, a typo in the method name creates a new method silently — the override never happens.
With `@Override`, the compiler checks — wrong name = compile error immediately.

### When
Every time you implement an abstract method or replace a parent method.

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
@Override
protected double getDefaultDailyLimit() { return DAILY_LIMIT; }   // replaces Account's version

@Override
protected double getDefaultMonthlyLimit() { return MONTHLY_LIMIT; }

@Override
public double getMinimumBalance() { return MINIMUM_BALANCE; }

@Override
public boolean canWithdraw(double amount) {
    return (balance - amount >= MINIMUM_BALANCE);
}
```

Without `@Override`, if you typo `canWithdraw` as `canwithdraw`, Java creates a new method and the withdrawal logic is never enforced — a critical banking bug.

---

## 18. `instanceof`

### What
`instanceof` checks **whether an object is of a specific type** — returns `true` or `false`.

### Why
When you have a parent-type variable, you need to check the real type before downcasting or calling type-specific methods.

### When
Before downcasting. When behavior differs by type. In exception handlers.

### Real example — `Account.java`
```java
// models/Account.java
// Used to check what type of account before applying type-specific logic
if (account instanceof SavingsAccount savingsAccount) {
    savingsAccount.creditMonthlyInterest();   // only savings accounts have this
}
```

### Real example — your exercise
```java
// oop/exercise/ExerciseMain.java
for (Payment payment : payments) {
    if (payment instanceof Refundable r) {      // is this payment refundable?
        r.refund(payment.getAmount());           // only CreditCard and BankTransfer reach here
    }
    // CashPayment skipped — not instanceof Refundable
}
```

---

## 19. Casting

### What
Casting means **telling Java to treat an object as a different type**.

### Why
When stored as a parent type, you lose access to child-specific methods.
Casting gives access back — but must be done safely.

### When
After confirming with `instanceof`, downcast to access child-specific methods.

### Two types

**Upcasting — automatic, always safe**
```java
// Child stored as parent — automatic
Account account = new SavingsAccount("ACC-001", "C001", 1000.0, Currency.USD, 3.5);
//      ↑ parent type        ↑ child object
// SavingsAccount IS-A Account — always safe
```

**Downcasting — manual, check first**
```java
Account account = new SavingsAccount("ACC-001", "C001", 1000.0, Currency.USD, 3.5);

// Old way — check then cast
if (account instanceof SavingsAccount) {
    SavingsAccount savings = (SavingsAccount) account;
    savings.creditMonthlyInterest();
}

// Java 16+ — pattern matching (one line)
if (account instanceof SavingsAccount savings) {
    savings.creditMonthlyInterest();
}
```

---

## 20. Generics

### What
Generics let you **specify the type a class or method works with** — so Java catches type errors at compile time.

### Why
Without generics, collections accept any type — a wrong type causes a runtime crash.
With generics, the wrong type is rejected at compile time — safer and no casting needed.

### When
Collections, repository return types, Optional, ResponseEntity, any reusable container.

### Real example — `Account.java`
```java
// models/Account.java
protected final List<Transaction> transactions;
//                    ↑
//          only Transaction objects allowed — wrong type = compile error

transactions.add(new Transaction(...));   // ✅
transactions.add("hello");               // ❌ compile error
```

### Real example — Spring Boot
```java
// JpaRepository<Entity, ID>
public interface UserRepository extends JpaRepository<User, Long> { }
//                                                     ↑     ↑
//                                                  entity  ID type

// Service method
public Optional<User> findById(Long id) {
    return userRepository.findById(id);   // Optional<User> — not Optional<anything>
}

public ResponseEntity<List<User>> getAll() {
    return ResponseEntity.ok(userRepository.findAll());
}
```

---

## 21. Collections

### What
Collections are **built-in data structures** that store groups of objects.

### Why
Arrays have fixed size and no built-in methods. Collections are resizable and have rich APIs.

### When
- `List` → ordered data, duplicates allowed
- `Set` → unique values only
- `Map` → key → value lookup

### Real example — `Account.java`
```java
// models/Account.java
protected final List<Transaction> transactions;
//              ↑ ordered — transactions in time order, duplicates possible

this.transactions = Collections.synchronizedList(new ArrayList<>());
//                  ↑ thread-safe list — multiple threads can add transactions safely
```

### Real example — `BankManagementSystem.java`
```java
// Multiple accounts stored in a Map — fast lookup by account number
Map<String, Account> accounts = new HashMap<>();
accounts.put("ACC-001", savingsAccount);
accounts.get("ACC-001");          // instant lookup by key
accounts.containsKey("ACC-001"); // check existence
```

### Key methods
| Action | List | Set | Map |
|--------|------|-----|-----|
| Add | `add(value)` | `add(value)` | `put(key, value)` |
| Get | `get(index)` | ❌ no index | `get(key)` |
| Remove | `remove(index)` | `remove(value)` | `remove(key)` |
| Check | `contains(value)` | `contains(value)` | `containsKey(key)` |
| Size | `size()` | `size()` | `size()` |

---

## 22. Exception Handling

### What
Exception handling means **catching errors at runtime and deciding what to do** instead of letting the program crash.

### Why
Real applications must handle failures gracefully — invalid input, missing records, network errors.
A crash in production is unacceptable.

### When
- Any operation that can fail: database, file, parsing, business rule violations
- Custom exceptions for domain errors (InsufficientFunds, AccountNotFound)

### Real example — `Account.java`
```java
// models/Account.java
public synchronized boolean deposit(double amount, String description) throws BankingException {
    try {
        validateTransaction(amount);
        if (status != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Account is not active: " + status);
        }
        balance += amount;
        return true;
    } catch (BankingException e) {
        throw e;   // rethrow — caller handles it
    }
}
```

### Real example — `InsufficientFundsException.java`
```java
// exceptions/InsufficientFundsException.java
public class InsufficientFundsException extends BankingException {
    public InsufficientFundsException(String message) {
        super(message);   // message goes up to RuntimeException
    }
}

// Used in Account.java
throw new InsufficientFundsException(
    String.format("Balance: %.2f, Attempted: %.2f", balance, amount)
);
```

### Spring Boot — global handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handle(InsufficientFundsException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
```

### Exception hierarchy
```
Throwable
    ├── Error              (JVM problems — do not catch)
    └── Exception
          ├── IOException  (checked — must handle)
          └── RuntimeException (unchecked — optional)
                ├── NullPointerException
                ├── ArithmeticException
                └── BankingException       ← your custom base
                      ├── InsufficientFundsException
                      ├── AccountNotFoundException
                      └── InvalidTransactionException
```

---

## 23. Lambda

### What
A lambda is a **short anonymous method** — pass behavior as a value without writing a full class.

### Why
Removes boilerplate. Instead of a full anonymous class just to pass one method, write `(params) -> body`.

### When
Sorting, filtering, forEach, callbacks, Spring Security config, Optional handling.

### Real example — `Account.java`
```java
// models/Account.java — Stream + Lambda to filter transactions by date
public List<Transaction> getTransactionHistory(LocalDate startDate, LocalDate endDate) {
    return transactions.stream()
            .filter(t -> !t.getTimestamp().toLocalDate().isBefore(startDate) &&
                         !t.getTimestamp().toLocalDate().isAfter(endDate))
            //      ↑ lambda — t is each Transaction, returns boolean
            .collect(Collectors.toList());
}
```

### Lambda syntax
```java
() -> System.out.println("Hello")              // no parameter
name -> System.out.println(name)               // one parameter
(a, b) -> a + b                               // multiple parameters
(a, b) -> { int r = a + b; return r; }        // multiple lines
```

---

## 24. Stream

### What
A stream is a **pipeline that processes a collection step by step** — filter, transform, collect.

### Why
Replaces nested loops with a clean, readable pipeline.
Each step has a clear name — easy to understand what the code does.

### When
Any time you process a collection — filter, sort, map, group, sum.

### Real example — `Account.java`
```java
// models/Account.java
public List<Transaction> getTransactionHistory(LocalDate startDate, LocalDate endDate) {
    return transactions.stream()                           // source
            .filter(t -> !t.getTimestamp()                 // intermediate — keep matching
                    .toLocalDate().isBefore(startDate) &&
                    !t.getTimestamp()
                    .toLocalDate().isAfter(endDate))
            .collect(Collectors.toList());                 // terminal — collect to list
}
```

### Common operations
```java
List<Transaction> transactions = account.getTransactions();

// Filter — keep only deposits
List<Transaction> deposits = transactions.stream()
        .filter(t -> t.getType() == TransactionType.DEPOSIT)
        .collect(Collectors.toList());

// Map — get just the amounts
List<Double> amounts = transactions.stream()
        .map(t -> t.getAmount())
        .collect(Collectors.toList());

// Sum all amounts
double total = transactions.stream()
        .mapToDouble(Transaction::getAmount)
        .sum();

// Count
long count = transactions.stream()
        .filter(t -> t.getAmount() > 1000)
        .count();
```

```
Stream pipeline:
list.stream() → filter → map → sorted → collect
   source        step     step    step    result
```

---

## 25. Optional

### What
`Optional` is a **container that either holds a value or is empty** — forces you to handle the missing case.

### Why
`null` is invisible — you forget to check and the program crashes with NullPointerException.
`Optional` makes the missing case visible and forces you to handle it.

### When
Method return type when value might not exist. Repository results. Never as a field or parameter.

### Real example — Spring Boot (Repository)
```java
// Spring JpaRepository returns Optional automatically
Optional<Account> account = accountRepository.findById("ACC-001");

// Handle safely
Account acc = account
        .orElseThrow(() -> new AccountNotFoundException("Account not found: ACC-001"));

// Or with default
Account acc = account.orElse(new DefaultAccount());

// Or only act if present
account.ifPresent(a -> a.creditMonthlyInterest());
```

### Common methods
```java
Optional<Account> opt = accountRepository.findById("ACC-001");

opt.isPresent()                          // true if value exists
opt.isEmpty()                            // true if empty
opt.get()                                // get value — dangerous if empty
opt.orElse(defaultValue)                 // return default if empty
opt.orElseGet(() -> createDefault())     // lazy default
opt.orElseThrow(() -> new Exception())   // throw if empty
opt.ifPresent(a -> doSomething(a))       // run only if present
opt.map(a -> a.getBalance())             // transform if present
opt.filter(a -> a.getBalance() > 0)      // keep only if condition met
```

---

## 26. Annotation

### What
An annotation is **metadata attached to a class, method, or field** — tells Java or a framework what to do with it.

### Why
Without annotations, you would need XML config files to wire everything.
Annotations let Spring Boot discover and configure everything automatically.

### When
Spring beans, endpoints, validation, JPA mapping, security, Lombok.

### Real example — Spring Boot
```java
@RestController                        // this class handles HTTP
@RequestMapping("/api/accounts")
@RequiredArgsConstructor               // Lombok: generate constructor
public class AccountController {

    private final AccountService accountService;   // injected by Spring

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<String> deposit(
            @PathVariable String id,
            @RequestParam double amount) throws BankingException {
        accountService.deposit(id, amount);
        return ResponseEntity.ok("Deposited: " + amount);
    }
}
```

### Common Spring Boot annotations
```
Class level:
@SpringBootApplication    entry point
@RestController           HTTP controller
@Service                  business logic
@Repository               database layer
@Component                general bean
@Configuration            config class
@Entity                   database table

Method level:
@GetMapping               GET endpoint
@PostMapping              POST endpoint
@PutMapping               PUT endpoint
@DeleteMapping            DELETE endpoint
@ExceptionHandler         handle specific exception

Parameter level:
@PathVariable             from URL: /users/{id}
@RequestParam             from query: /users?page=1
@RequestBody              from JSON body
@Valid                    trigger validation

Field level:
@Id                       primary key
@Column                   column mapping
@NotNull / @NotBlank      validation rules
```

---

## 27. Enum

### What
An `enum` is a **fixed set of named constants** — a type with a limited, known set of values.

### Why
Using raw strings or ints for status/type values leads to bugs — typos, invalid values.
Enum restricts the value to only valid options.

### When
Status fields, type fields, categories — anything with a fixed set of options.

### Real example — `AccountStatus.java`
```java
// enums/AccountStatus.java
public enum AccountStatus {
    ACTIVE, FROZEN, SUSPENDED, CLOSED
}

// Used in Account.java
protected AccountStatus status;

public void freeze() {
    this.status = AccountStatus.FROZEN;   // cannot accidentally set status = "FROZE" (typo)
}

// Safe comparison
if (status != AccountStatus.ACTIVE) {
    throw new InvalidTransactionException("Account is not active: " + status);
}
// Cannot do: if (status == "ACTIVE") ← compile error — wrong type
```

### Real example — `TransactionType.java`
```java
// enums/TransactionType.java
public enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER, INTEREST, FEE
}

// Used in Account.java
addTransaction(TransactionType.DEPOSIT, amount, description, null);
addTransaction(TransactionType.INTEREST, interest, "Monthly Interest", null);
```

```
Without enum:
    String status = "ACTVE";   // typo — bug, no compile error

With enum:
    AccountStatus status = AccountStatus.ACTVE;  // ❌ compile error — no such value
    AccountStatus status = AccountStatus.ACTIVE; // ✅
```

---

## Quick Reference — When to use what

| Concept | Use when |
|---------|----------|
| `class` | Modeling any real-world concept |
| `abstract class` | Shared code + shared state + forced child behavior |
| `interface` | Pure contract, multiple implementations |
| `extends` | IS-A relationship, inherit code |
| `implements` | CAN-DO relationship, fulfill contract |
| `static` | Utility methods, constants, no object needed |
| `final` | Values that must never change |
| `private` | Hide internal data (fields) |
| `protected` | Share with child classes only |
| `Optional` | Method might return nothing |
| `List` | Ordered data, duplicates ok |
| `Map` | Key-value lookup |
| `Set` | Unique values only |
| `enum` | Fixed set of valid values |
| `@Override` | Every abstract/interface method implementation |
| `instanceof` | Check type before downcasting |
| `lambda` | Pass behavior as a value |
| `stream` | Process collections in a pipeline |
| `try-catch` | Handle operations that can fail |
| Custom exception | Domain-specific error with meaning |
| `String` | Text data — use methods, never `==` to compare |
| `StringBuilder` | Building strings in a loop — faster than `+` |
| `int/long/double` | Numbers without object overhead |
| `Integer/Double` | When collection or Optional needs an object |
| `array` | Fixed-size, same-type, fast index access |
| `if/else/switch` | Conditional logic |
| `for/while` | Repeat logic |

---

# PRIORITY 1 — Java Foundations

---

## 28. Data Types — Primitives vs Objects

### What
Java has two kinds of data:
- **Primitive** — raw value stored directly on the stack
- **Object (Reference type)** — stored on the heap, variable holds a reference (address)

### Why
Primitives are fast and use less memory. Objects have methods and can be null.
Knowing the difference prevents bugs — especially with `==` comparison and collections.

### When
- Use primitives (`int`, `double`, `boolean`) for simple values — counters, flags, amounts
- Use objects (`Integer`, `String`, `User`) when you need null, generics, or methods

### The 8 primitives

| Type | Size | Example | Default |
|------|------|---------|---------|
| `byte` | 8-bit | `byte b = 100;` | 0 |
| `short` | 16-bit | `short s = 1000;` | 0 |
| `int` | 32-bit | `int age = 25;` | 0 |
| `long` | 64-bit | `long id = 100L;` | 0L |
| `float` | 32-bit | `float f = 3.14f;` | 0.0f |
| `double` | 64-bit | `double price = 99.99;` | 0.0 |
| `char` | 16-bit | `char c = 'A';` | ' ' |
| `boolean` | 1-bit | `boolean active = true;` | false |

### Real example — `Account.java`
```java
// models/Account.java
protected double balance;           // primitive — raw number, fast
protected double dailyTransactionLimit;
protected double monthlyTransactionLimit;
protected double currentMonthlyTransactions;

// models/Customer.java
private final String firstName;     // object — has methods, can be null
private final String lastName;
private final LocalDate dateOfBirth; // object — has methods
```

### Primitive vs Object — key differences
```java
// Primitive — value stored directly
int a = 5;
int b = 5;
a == b;   // ✅ true — comparing values directly

// Object — variable stores a reference (address)
String s1 = new String("hello");
String s2 = new String("hello");
s1 == s2;        // ❌ false — comparing addresses, not content
s1.equals(s2);   // ✅ true  — comparing content
```

---

## 29. Variables — Local, Instance, Static

### What
A variable is a **named storage location** that holds a value.
Three types depending on where it is declared.

### Why
Knowing the type tells you: where it lives in memory, who can see it, and how long it exists.

### When
- **Local** — temporary value inside a method
- **Instance** — data that belongs to each object
- **Static** — data shared by all objects of the class

### All three types
```java
public class Account {

    // INSTANCE variable — each Account object has its own copy
    private double balance;
    private String accountNumber;

    // STATIC variable — one copy shared by ALL Account objects
    private static int totalAccounts = 0;

    public void deposit(double amount) {
        // LOCAL variable — only exists inside this method, gone when method ends
        double newBalance = balance + amount;
        balance = newBalance;
    }
}
```

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java

// static — shared constant, same for all SavingsAccounts
private static final double MINIMUM_BALANCE = 500.0;
private static final double DAILY_LIMIT     = 5000.0;

// instance — each SavingsAccount has its own interestRate
private final double interestRate;
private LocalDate lastInterestCredit;
```

```
Local variable    → method scope → dies when method ends → stack
Instance variable → object scope → dies when object is GC'd → heap
Static variable   → class scope  → lives until program ends → method area
```

---

## 30. Operators

### What
Operators are **symbols that perform operations** on values.

### Why
Every calculation, comparison, and condition in your code uses operators.
The most important one to master: `==` vs `.equals()` — very common bug.

### When
Every method that calculates, compares, or makes a decision.

### Arithmetic operators
```java
int a = 10, b = 3;

a + b;   // 13 — addition
a - b;   // 7  — subtraction
a * b;   // 30 — multiplication
a / b;   // 3  — integer division (decimal cut off)
a % b;   // 1  — remainder (modulo)

double x = 10.0 / 3;   // 3.333... — double division keeps decimal
```

### Comparison operators
```java
a == b;   // false — equal to
a != b;   // true  — not equal to
a > b;    // true  — greater than
a < b;    // false — less than
a >= b;   // true  — greater than or equal
a <= b;   // false — less than or equal
```

### Logical operators
```java
true && false;   // false — AND — both must be true
true || false;   // true  — OR  — at least one must be true
!true;           // false — NOT — flip the value
```

### `==` vs `.equals()` — the most common bug
```java
// Primitives — use ==
int a = 5, b = 5;
a == b;   // ✅ true

// Objects — NEVER use == for content comparison
String s1 = new String("hello");
String s2 = new String("hello");
s1 == s2;        // ❌ false — compares memory addresses
s1.equals(s2);   // ✅ true  — compares content

// Enum — safe to use ==
AccountStatus status = AccountStatus.ACTIVE;
status == AccountStatus.ACTIVE;   // ✅ true — enums are singletons
```

### Ternary operator — short if/else
```java
// condition ? valueIfTrue : valueIfFalse
String label = balance > 0 ? "Positive" : "Zero or negative";

// Real example — Account.java
double fee = (status == AccountStatus.ACTIVE) ? getMaintenanceFee() : 0;
```

### Real example — `ValidationUtils.java`
```java
// utilities/ValidationUtils.java
public static boolean isValidAmount(double amount) {
    return amount > 0 && amount <= 1_000_000;
    //             ↑ comparison    ↑ && logical AND
}
```

### Increment / Decrement
```java
int count = 0;
count++;   // count = 1 — post-increment
count--;   // count = 0 — post-decrement
++count;   // count = 1 — pre-increment (increments before use)
```

---

## 31. Control Flow — `if/else`, `switch`, `for`, `while`

### What
Control flow statements **decide which code runs and how many times**.

### Why
Without control flow, every program runs top-to-bottom with no decisions or repetition.

### When
- `if/else` — when behavior depends on a condition
- `switch` — when one value matches many cases
- `for` — when you know how many times to repeat
- `while` — when you repeat until a condition is false

### `if / else if / else`
```java
// models/Account.java
public void validateTransaction(double amount) throws BankingException {
    if (status == AccountStatus.FROZEN) {
        throw new InvalidTransactionException("Account is frozen");
    } else if (status == AccountStatus.CLOSED) {
        throw new InvalidTransactionException("Account is closed");
    } else if (!ValidationUtils.isValidAmount(amount)) {
        throw new InvalidTransactionException("Invalid amount: " + amount);
    }
    // if none of the above — valid transaction, continues
}
```

### `switch` — multiple cases on one value
```java
// Cleaner than many if/else chains
String message = switch (status) {
    case ACTIVE    -> "Account is active";
    case FROZEN    -> "Account is frozen — contact support";
    case SUSPENDED -> "Account is suspended";
    case CLOSED    -> "Account is permanently closed";
};
```

### `for` loop — known number of iterations
```java
// Classic for loop
for (int i = 0; i < 10; i++) {
    System.out.println("Iteration: " + i);
}

// Enhanced for loop — iterate a collection
List<Transaction> transactions = account.getTransactions();
for (Transaction t : transactions) {
    System.out.println(t.getAmount());
}
```

### `while` loop — repeat until condition is false
```java
// Keep asking until valid input
Scanner scanner = new Scanner(System.in);
String input = "";
while (!ValidationUtils.isValidAmount(Double.parseDouble(input))) {
    System.out.print("Enter amount: ");
    input = scanner.nextLine();
}
```

### `break` and `continue`
```java
for (Transaction t : transactions) {
    if (t.getAmount() < 0) {
        continue;   // skip this iteration, go to next
    }
    if (t.getAmount() > 100_000) {
        break;      // stop the loop entirely
    }
    System.out.println(t.getAmount());
}
```

### Real example — `SavingsAccount.java`
```java
// models/SavingsAccount.java
public void creditMonthlyInterest() {
    LocalDate today = LocalDate.now();

    // if condition — only run if a new month
    if (today.getMonth() != lastInterestCredit.getMonth() ||
            today.getYear() != lastInterestCredit.getYear()) {

        double interest = balance * (interestRate / 100.0) / 12.0;

        // if condition inside — only credit if positive
        if (interest > 0) {
            balance += interest;
            lastInterestCredit = today;
        }
    }
}
```

---

## 32. String

### What
`String` is a **sequence of characters** — it is an object, not a primitive.
`String` is **immutable** — once created, it cannot be changed.

### Why
Strings are everywhere — names, emails, messages, IDs, JSON.
Understanding immutability and String methods prevents performance bugs and NPEs.

### When
Any time you handle text data.

### String is immutable — every operation creates a NEW String
```java
String name = "Heang";
name.toUpperCase();       // does NOT change name — creates a new String
System.out.println(name); // still "Heang"

// Must reassign to keep the result
name = name.toUpperCase();
System.out.println(name); // "HEANG"
```

### String Pool — why `==` fails
```java
// String literals go into the String Pool — shared
String a = "hello";
String b = "hello";
a == b;         // ✅ true — same object in pool

// new String() bypasses pool — creates new object in heap
String c = new String("hello");
String d = new String("hello");
c == d;         // ❌ false — different objects
c.equals(d);    // ✅ true  — same content

// Rule: ALWAYS use .equals() to compare String content
```

### Common String methods
```java
String s = "  Hello, Heang!  ";

s.length();                    // 17 — character count
s.trim();                      // "Hello, Heang!" — remove leading/trailing spaces
s.toLowerCase();               // "  hello, heang!  "
s.toUpperCase();               // "  HELLO, HEANG!  "
s.contains("Heang");           // true
s.startsWith("  Hello");       // true
s.endsWith("!  ");             // true
s.replace("Heang", "Dara");   // "  Hello, Dara!  "
s.substring(2, 7);             // "Hello"
s.split(", ");                 // ["  Hello", "Heang!  "]
s.indexOf("Heang");            // 9 — position of first match
s.isEmpty();                   // false
s.isBlank();                   // false (isBlank also checks whitespace-only)
String.format("Hi %s", "Heang"); // "Hi Heang"
```

### Real example — `Customer.java`
```java
// models/Customer.java
public String getFullName() {
    return firstName + " " + lastName;   // String concatenation
}

public String generateCustomerId() {
    return "CUST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    //                                            ↑ substring  ↑ toUpperCase
}
```

### `StringBuilder` — when building strings in a loop
```java
// ❌ Bad — creates a new String object on every iteration
String result = "";
for (Transaction t : transactions) {
    result += t.getAmount() + ", ";   // slow — n new String objects created
}

// ✅ Good — StringBuilder modifies in place, one object
StringBuilder sb = new StringBuilder();
for (Transaction t : transactions) {
    sb.append(t.getAmount()).append(", ");
}
String result = sb.toString();
```

### `String.format` — `printf` style
```java
// models/Account.java
@Override
public String toString() {
    return String.format("Account[%s] Balance: %.2f %s | Status: %s",
            accountNumber, balance, currency, status);
    //                           ↑ %.2f = 2 decimal places
}
```

### `format` patterns
```
%s   → String
%d   → integer
%f   → decimal (%.2f = 2 decimal places)
%n   → newline
%b   → boolean
```

---

## 33. Wrapper Classes — Autoboxing & Unboxing

### What
Wrapper classes are **object versions of primitives** — `int` → `Integer`, `double` → `Double`, `boolean` → `Boolean`.

### Why
Collections (`List`, `Map`, `Set`) cannot hold primitives — they need objects.
Generics cannot use primitives — `List<int>` is illegal, `List<Integer>` is required.

### When
Whenever a primitive must go into a collection, Optional, or generics.

### Primitive → Wrapper mapping
| Primitive | Wrapper |
|-----------|---------|
| `int` | `Integer` |
| `long` | `Long` |
| `double` | `Double` |
| `float` | `Float` |
| `boolean` | `Boolean` |
| `char` | `Character` |
| `byte` | `Byte` |
| `short` | `Short` |

### Autoboxing — primitive automatically becomes object
```java
List<Integer> ages = new ArrayList<>();
ages.add(25);        // autoboxing — 25 (int) → Integer automatically
ages.add(30);

int first = ages.get(0);  // unboxing — Integer → int automatically
```

### Unboxing — object automatically becomes primitive
```java
Integer count = 100;
int result = count + 50;   // unboxing — Integer → int for arithmetic
```

### Useful Wrapper methods
```java
// Parse String to number — very common in Spring Boot
int age     = Integer.parseInt("25");
double amt  = Double.parseDouble("150.50");
long id     = Long.parseLong("100");
boolean flag = Boolean.parseBoolean("true");

// Number to String
String s = Integer.toString(25);
String s = String.valueOf(25);   // works for any type

// Min/Max constants
Integer.MAX_VALUE;   // 2147483647
Integer.MIN_VALUE;   // -2147483648
Double.MAX_VALUE;
```

### NullPointerException trap with unboxing
```java
Integer value = null;
int x = value;   // 💥 NullPointerException — unboxing null crashes

// Always check null when unboxing
if (value != null) {
    int x = value;
}
// Or use Optional
Optional.ofNullable(value).orElse(0);
```

### Real example — Spring Boot
```java
// PathVariable comes as String — parse to Long
@GetMapping("/accounts/{id}")
public Account getAccount(@PathVariable Long id) {
    //                               ↑ wrapper — Spring converts String → Long automatically
    return accountRepository.findById(id).orElseThrow();
}
```

---

## 34. Arrays

### What
An array is a **fixed-size, ordered container of the same type**.
Size is set at creation — cannot grow or shrink.

### Why
Fastest way to store multiple values of the same type.
Foundation for understanding collections (ArrayList is backed by an array internally).

### When
Fixed-size data — months of year, days of week, lookup tables.
Prefer `List` for most cases — arrays are used when size is known and fixed.

### Declare and create
```java
// Declare and create empty array
int[] numbers = new int[5];       // [0, 0, 0, 0, 0]
String[] names = new String[3];   // [null, null, null]

// Declare and initialize in one line
int[] scores   = {95, 87, 76, 91, 88};
String[] days  = {"Mon", "Tue", "Wed", "Thu", "Fri"};
```

### Access and modify
```java
int[] scores = {95, 87, 76};

scores[0];          // 95 — first element (index starts at 0)
scores[2];          // 76 — last element
scores.length;      // 3 — number of elements

scores[0] = 100;    // modify first element
scores[5] = 80;     // ❌ ArrayIndexOutOfBoundsException — only 0,1,2 exist
```

### Loop through array
```java
int[] scores = {95, 87, 76, 91};

// Classic for loop — when you need the index
for (int i = 0; i < scores.length; i++) {
    System.out.println("Score " + i + ": " + scores[i]);
}

// Enhanced for loop — when you just need the value
for (int score : scores) {
    System.out.println(score);
}
```

### Array vs List
```java
// Array — fixed size
int[] arr = new int[3];
// arr.add(4) ← ❌ no add method

// List — dynamic size
List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);
list.add(4);   // ✅ grows automatically
```

| | Array | List |
|--|-------|------|
| Size | Fixed | Dynamic |
| Access | `arr[0]` | `list.get(0)` |
| Add element | ❌ | `list.add(x)` |
| Remove | ❌ | `list.remove(x)` |
| Primitives | ✅ | ❌ needs wrapper |
| Use when | size is fixed | size changes |

### Convert array → List and back
```java
String[] arr  = {"a", "b", "c"};
List<String> list = Arrays.asList(arr);    // array → List
String[] back = list.toArray(new String[0]); // List → array
```

---

## 35. Pass by Value vs Pass by Reference

### What
Java is **always pass by value** — but for objects, the value passed is the **reference (address)**, not the object itself.

### Why
This is one of the most misunderstood concepts in Java.
If you don't understand it, you will write methods that fail to change values as expected.

### When
Every time you pass a variable to a method — you need to know what the method can and cannot change.

### Primitives — pass a COPY of the value
```java
public void addBonus(double balance) {
    balance += 1000;   // changes the LOCAL copy only
}

double myBalance = 5000;
addBonus(myBalance);
System.out.println(myBalance);   // still 5000 — original not changed
```

The method gets a copy. Changes to the copy do not affect the original.

### Objects — pass a COPY of the REFERENCE (address)
```java
public void deposit(Account account, double amount) {
    account.balance += amount;   // follows the reference → modifies the real object
}

Account acc = new Account("ACC-001", 1000.0);
deposit(acc, 500);
System.out.println(acc.balance);   // 1500 — real object WAS changed
```

The method gets a copy of the address. Both the original variable and the parameter point to the **same object in heap** — so changes to the object are visible outside.

### But reassigning the reference does NOT affect the original
```java
public void replace(Account account) {
    account = new Account("NEW", 0);   // only changes local copy of reference
}

Account acc = new Account("ACC-001", 1000.0);
replace(acc);
System.out.println(acc.getAccountNumber());   // still "ACC-001" — original reference unchanged
```

### Real example — `Account.java`
```java
// models/Account.java
public synchronized boolean deposit(double amount, String description) throws BankingException {
    validateTransaction(amount);    // passes amount (primitive) — method cannot change it
    balance += amount;              // changes THIS object's field directly — works
    addTransaction(...);            // passes objects — they are modified via reference
    return true;
}
```

### Mental model
```
Primitive:
    myBalance = 5000  →  [5000]
    addBonus(myBalance) → method gets copy: [5000]
                          method changes:   [6000]  (copy only)
    myBalance is still: [5000]

Object:
    acc → [0x100] → Account{balance=1000}
    deposit(acc)  → method gets copy of [0x100]
                    both point to same Account
                    method changes balance → Account{balance=1500}
    acc → [0x100] → Account{balance=1500}  ← original sees the change
```

---

## Quick Reference — Priority 1 Foundations

| Concept | Key rule |
|---------|----------|
| Primitives | `int`, `double`, `boolean` — stored on stack, fast, no null |
| Objects | `String`, `User`, `Account` — stored on heap, can be null |
| `==` | Safe for primitives and enums only — use `.equals()` for objects |
| Local variable | Lives only inside the method |
| Instance variable | One per object — `private double balance` |
| Static variable | Shared across all objects — `static final double RATE` |
| String immutable | Every change creates a new String — reassign to keep result |
| String pool | Literals are shared — `new String()` bypasses pool |
| StringBuilder | Use when building strings in a loop — much faster than `+` |
| Wrapper class | `Integer`, `Double` — needed for collections and generics |
| Autoboxing | `int` → `Integer` automatically when added to collection |
| Array | Fixed size, fast index access, same type only |
| Pass by value | Primitives: copy of value. Objects: copy of reference |
| Object fields | Can change via reference — the object itself is shared |

---

# PRIORITY 2 — OOP Deeper

---

## 36. The 4 OOP Pillars

### What
The four core principles that every Java class should follow.
This is **interview question #1** — you must explain all four clearly.

---

### Pillar 1: Encapsulation — hide data, expose safely

**What:** Keep fields `private`. Expose only what is needed via `public` methods.
**Why:** Prevents outside code from putting the object into an invalid state.

```java
// models/Account.java
public class Account {
    private double balance;   // hidden — nobody touches this directly

    public double getBalance() {        // controlled READ
        return balance;
    }

    public boolean deposit(double amount, String description) {
        if (!ValidationUtils.isValidAmount(amount)) return false;
        balance += amount;              // controlled WRITE — validation enforced
        return true;
    }
}

Account acc = new Account(...);
acc.balance = -9999;     // ❌ private — blocked
acc.deposit(-9999, "x"); // ✅ goes through validation — safely rejected
```

---

### Pillar 2: Inheritance — reuse parent code

**What:** Child class gets all fields and methods from parent via `extends`.
**Why:** Write shared logic once. Fix it once. All children benefit.

```java
// Account — shared logic for all account types
public class Account {
    protected double balance;
    public boolean deposit(double amount, String desc) { ... }   // shared
    public void freeze() { ... }                                  // shared
}

// SavingsAccount only adds what is NEW
public class SavingsAccount extends Account {
    private double interestRate;
    public void creditMonthlyInterest() { ... }   // unique to savings
}
// deposit(), freeze() inherited — no code duplication
```

---

### Pillar 3: Polymorphism — one type, many behaviors

**What:** Parent type variable holds different child objects. Each child runs its own version of a method.
**Why:** Write one loop, handle all types. Add a new type — existing code needs no changes.

```java
// One List, three types
List<Account> accounts = new ArrayList<>();
accounts.add(new SavingsAccount(...));
accounts.add(new CheckingAccount(...));
accounts.add(new BusinessAccount(...));

// One loop — each calls its own canWithdraw()
for (Account acc : accounts) {
    System.out.println(acc.canWithdraw(1000));
    // SavingsAccount  → checks MINIMUM_BALANCE
    // CheckingAccount → checks overdraftLimit
    // BusinessAccount → checks businessLimit
}
```

Two types:
- **Compile-time** (method overloading) — same name, different parameters
- **Runtime** (method overriding) — child replaces parent method, decided at runtime

---

### Pillar 4: Abstraction — hide complexity, show only what matters

**What:** Hide internal details. Expose only a simple interface.
**Why:** Users of your class don't need to know HOW it works — only WHAT it does.

```java
// You call deposit() — you don't know about validateTransaction(),
// addTransaction(), AtomicLong, synchronization inside
account.deposit(500.0, "salary");

// The complexity is hidden inside Account:
public synchronized boolean deposit(double amount, String description) throws BankingException {
    validateTransaction(amount);         // hidden
    balance += amount;                   // hidden
    addTransaction(...);                 // hidden
    return true;
}
```

Achieved via: `abstract` class, `interface`, `private` methods.

---

### 4 Pillars — one-line summary

```
Encapsulation  = hide data, expose via controlled methods
Inheritance    = child reuses parent code
Polymorphism   = one type reference, many runtime behaviors
Abstraction    = hide complexity, show simple interface
```

---

## 37. Composition vs Inheritance

### What
Two ways to reuse code:
- **Inheritance** — IS-A: `SavingsAccount extends Account`
- **Composition** — HAS-A: `Account HAS-A TransactionLogger`

### Why
Inheritance creates tight coupling. Composition is more flexible — you can swap parts.
Rule: **favour composition over inheritance**.

### When
- Inheritance: child truly IS-A parent — `SavingsAccount IS-A Account`
- Composition: class USES another class — `Account USES ValidationUtils`

### Real example — `Account.java` uses composition
```java
// models/Account.java
public class Account {
    // HAS-A — composed of these objects
    private final AtomicLong transactionCounter;   // composed
    protected final List<Transaction> transactions; // composed
}
```

### Composition — swappable parts
```java
// Define behavior as interface
public interface InterestCalculator {
    double calculate(double balance);
}

// Two implementations — can swap at runtime
public class SimpleInterest implements InterestCalculator {
    public double calculate(double balance) { return balance * 0.05; }
}
public class CompoundInterest implements InterestCalculator {
    public double calculate(double balance) { return balance * Math.pow(1.05, 12); }
}

// Account composes the calculator — not inherit it
public class SavingsAccount {
    private final InterestCalculator calculator;   // HAS-A

    public SavingsAccount(InterestCalculator calculator) {
        this.calculator = calculator;   // inject at creation — easy to swap
    }

    public double getInterest() {
        return calculator.calculate(balance);
    }
}

// Swap without changing SavingsAccount
new SavingsAccount(new SimpleInterest());
new SavingsAccount(new CompoundInterest());
```

```
Inheritance = baked in — cannot change at runtime
Composition = plugged in — swap parts without changing the class
```

---

## 38. Comparable vs Comparator

### What
Two ways to sort objects:
- **`Comparable`** — the class defines its own natural sort order (`compareTo`)
- **`Comparator`** — you define sort order externally — multiple sort orders possible

### Why
Collections cannot sort custom objects without knowing how to compare them.
`Comparable` = one fixed order. `Comparator` = many flexible orders.

### When
- `Comparable` — when there is one obvious "natural" order (e.g., sort accounts by account number)
- `Comparator` — when you need multiple sort orders (by balance, by date, by name)

### `Comparable` — built into the class
```java
public class Account implements Comparable<Account> {
    private String accountNumber;
    private double balance;

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
        // negative = this comes first
        // zero     = equal
        // positive = other comes first
    }
}

List<Account> accounts = new ArrayList<>();
Collections.sort(accounts);   // uses compareTo — sorts by accountNumber
```

### `Comparator` — defined outside, multiple orders
```java
List<Account> accounts = new ArrayList<>();

// Sort by balance ascending
accounts.sort(Comparator.comparingDouble(Account::getBalance));

// Sort by balance descending
accounts.sort(Comparator.comparingDouble(Account::getBalance).reversed());

// Sort by accountNumber then by balance
accounts.sort(Comparator.comparing(Account::getAccountNumber)
                        .thenComparingDouble(Account::getBalance));
```

### In Spring Boot — sorting query results
```java
// Repository — sorted by createdDate
List<Transaction> findByAccountNumberOrderByTimestampDesc(String accountNumber);

// Service — sort in memory
transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
```

```
Comparable  = the object knows how to compare itself → one order
Comparator  = external rule for comparison → many orders
```

---

## 39. Nested & Inner Classes

### What
A class defined **inside another class**.
Four types: static nested, inner (non-static), local, anonymous.

### Why
Groups closely related code. Used in Builder pattern, listeners, and anonymous implementations.

### When
- Static nested: helper class only used by the outer class (Builder pattern)
- Anonymous: one-time implementation you don't want to name
- Inner: needs access to outer class instance

### Static nested class — Builder pattern
```java
// design_pattern/builder/Computer.java
public class Computer {
    private final String HDD;
    private final String RAM;
    private final boolean isGraphicsCardEnabled;

    private Computer(ComputerBuilder builder) {   // private — only Builder creates it
        this.HDD = builder.HDD;
        this.RAM = builder.RAM;
        this.isGraphicsCardEnabled = builder.isGraphicsCardEnabled;
    }

    // Static nested class — lives inside Computer, accessed as Computer.ComputerBuilder
    public static class ComputerBuilder {
        private final String HDD;
        private final String RAM;
        private boolean isGraphicsCardEnabled;

        public ComputerBuilder(String hdd, String ram) {
            this.HDD = hdd;
            this.RAM = ram;
        }

        public ComputerBuilder setGraphicsCardEnabled(boolean enabled) {
            this.isGraphicsCardEnabled = enabled;
            return this;   // return this = method chaining
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}

// Usage — method chaining
Computer pc = new Computer.ComputerBuilder("500GB", "16GB")
        .setGraphicsCardEnabled(true)
        .build();
```

### Anonymous class — one-time implementation
```java
// Old way — before lambdas
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};

// Modern way — lambda (same thing, shorter)
Runnable task = () -> System.out.println("Running");
```

---

## 40. Record (Java 16+)

### What
A `record` is a **short way to create an immutable data class** — fields, constructor, getters, `equals`, `hashCode`, `toString` all generated automatically.

### Why
Writing a plain DTO (Data Transfer Object) requires constructor, getters, equals, hashCode, toString — lots of boilerplate.
`record` generates all of it from one line.

### When
DTOs, response models, value objects — any class that just holds data and should be immutable.

### Without record — boilerplate DTO
```java
public class TransactionDto {
    private final String accountNumber;
    private final double amount;
    private final String type;

    public TransactionDto(String accountNumber, double amount, String type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
    }
    public String getAccountNumber() { return accountNumber; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    // equals, hashCode, toString... more boilerplate
}
```

### With record — same result, one line
```java
public record TransactionDto(String accountNumber, double amount, String type) { }

// Java generates automatically:
// constructor, getters (accountNumber(), amount(), type()), equals, hashCode, toString
```

### Usage
```java
TransactionDto dto = new TransactionDto("ACC-001", 500.0, "DEPOSIT");

dto.accountNumber();   // "ACC-001" — getter (no "get" prefix in records)
dto.amount();          // 500.0
dto.type();            // "DEPOSIT"

System.out.println(dto);   // TransactionDto[accountNumber=ACC-001, amount=500.0, type=DEPOSIT]
```

### In Spring Boot — API response DTOs
```java
// Instead of a full class, use record for response body
public record AccountResponse(String accountNumber, double balance, String status) { }

@GetMapping("/accounts/{id}")
public AccountResponse getAccount(@PathVariable String id) {
    Account acc = accountService.findById(id);
    return new AccountResponse(acc.getAccountNumber(), acc.getBalance(), acc.getStatus().name());
}
```

---

## 41. `var` keyword (Java 10+)

### What
`var` lets Java **infer the type automatically** from the right-hand side — you don't need to write it.

### Why
Reduces repetition when the type is obvious from the right-hand side.

### When
Local variables only — where the type is clear from context. Never for fields or parameters.

```java
// Without var — type written twice
ArrayList<Transaction> transactions = new ArrayList<Transaction>();
Map<String, List<Account>> grouped = new HashMap<String, List<Account>>();

// With var — type inferred automatically
var transactions = new ArrayList<Transaction>();   // Java knows it's ArrayList<Transaction>
var grouped = new HashMap<String, List<Account>>();

// In a loop
for (var transaction : account.getTransactions()) {
    System.out.println(transaction.getAmount());   // transaction is Transaction type
}
```

### When NOT to use var
```java
var x = 5;               // ❌ unclear — is this int? long? Integer?
var result = compute();  // ❌ unclear — what does compute() return?

int count = 5;                          // ✅ clear
TransactionDto result = compute();     // ✅ clear
```

`var` is a hint to save typing — never use it when it makes code harder to read.

---

# PRIORITY 3 — Java 8+ Features

---

## 42. Functional Interfaces

### What
A functional interface has **exactly one abstract method** — it can be used as a lambda target.

### Why
Java's built-in functional interfaces (`Function`, `Predicate`, `Consumer`, `Supplier`) are the building blocks for lambdas and streams. You use them every day without noticing.

### When
Anywhere you pass behavior — filter condition, transform logic, action to run, value to supply.

### The 4 main functional interfaces

**`Predicate<T>` — takes T, returns boolean (filter condition)**
```java
Predicate<Account> isActive = acc -> acc.getStatus() == AccountStatus.ACTIVE;
Predicate<Account> hasBalance = acc -> acc.getBalance() > 0;

// Combine predicates
Predicate<Account> activeWithBalance = isActive.and(hasBalance);

// Use in stream
accounts.stream()
        .filter(isActive)
        .collect(Collectors.toList());
```

**`Function<T, R>` — takes T, returns R (transform)**
```java
Function<Account, String> toNumber = acc -> acc.getAccountNumber();
Function<String, Integer> toLength = String::length;

// Chain functions
Function<Account, Integer> accountToLength = toNumber.andThen(toLength);

// Use in stream
accounts.stream()
        .map(toNumber)
        .collect(Collectors.toList());
```

**`Consumer<T>` — takes T, returns nothing (action)**
```java
Consumer<Account> printBalance = acc ->
        System.out.println(acc.getAccountNumber() + ": " + acc.getBalance());

Consumer<Account> freeze = Account::freeze;

// Use
accounts.forEach(printBalance);
accounts.forEach(freeze);
```

**`Supplier<T>` — takes nothing, returns T (factory/lazy value)**
```java
Supplier<Account> defaultAccount = () -> new SavingsAccount("DEFAULT", "C000", 0, Currency.USD, 0);

// Use in Optional
Optional<Account> account = accountRepository.findById("ACC-001");
Account result = account.orElseGet(defaultAccount);   // Supplier called only if empty
```

### In Spring Boot — you see these everywhere
```java
// orElseThrow takes Supplier<Exception>
account.orElseThrow(() -> new AccountNotFoundException("Not found"));

// ifPresent takes Consumer
account.ifPresent(acc -> emailService.sendStatement(acc));

// Security config takes Consumer<HttpSecurity>
http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
```

---

## 43. Method References (`::`)

### What
Method reference is a **shortcut for a lambda that just calls one existing method**.

### Why
Cleaner than a lambda when the lambda body is just a method call.

### When
When a lambda is just `x -> SomeClass.method(x)` or `x -> x.method()` — replace with `::`.

### 4 types of method references

**1. Static method reference**
```java
// Lambda
accounts.stream().map(acc -> Double.toString(acc.getBalance()));
// Method reference
accounts.stream().map(acc -> String.valueOf(acc.getBalance()));

// Another example
List<String> amounts = List.of("100.5", "200.0");
amounts.stream().map(Double::parseDouble);   // Double.parseDouble(s)
```

**2. Instance method on a specific object**
```java
Account myAccount = new SavingsAccount(...);

// Lambda
accounts.stream().filter(acc -> myAccount.equals(acc));
// Method reference
accounts.stream().filter(myAccount::equals);
```

**3. Instance method on any object of that type**
```java
// Lambda
accounts.stream().map(acc -> acc.getAccountNumber());
// Method reference
accounts.stream().map(Account::getAccountNumber);

// Lambda
transactions.stream().map(t -> t.getAmount());
// Method reference
transactions.stream().map(Transaction::getAmount);
```

**4. Constructor reference**
```java
// Lambda
Stream<String> numbers = Stream.of("100", "200");
numbers.map(s -> new BigDecimal(s));
// Constructor reference
numbers.map(BigDecimal::new);
```

### Real example — `Account.java`
```java
// models/Account.java — method reference in stream
double total = transactions.stream()
        .mapToDouble(Transaction::getAmount)   // instead of t -> t.getAmount()
        .sum();
```

---

## 44. Date/Time API

### What
Java 8 introduced a new Date/Time API — `LocalDate`, `LocalDateTime`, `ZonedDateTime`, `Duration`.
Replaces the old `Date` and `Calendar` which were broken and confusing.

### Why
Old `java.util.Date` is mutable and thread-unsafe. The new API is immutable and clear.

### When
Any field with a date or time — `createdAt`, `updatedAt`, `dateOfBirth`, `expiresAt`.

### Core classes

| Class | What it represents |
|-------|-------------------|
| `LocalDate` | Date only — `2026-07-01` |
| `LocalTime` | Time only — `14:30:00` |
| `LocalDateTime` | Date + time — `2026-07-01T14:30:00` |
| `ZonedDateTime` | Date + time + timezone |
| `Duration` | Amount of time between two times |
| `Period` | Amount of time between two dates |

### Real example — `Customer.java` and `Account.java`
```java
// models/Customer.java
private final LocalDate dateOfBirth;         // date only — no time needed
private final LocalDateTime createdDate;     // date + time

// models/SavingsAccount.java
private LocalDate lastInterestCredit;        // track last month interest was credited

public void creditMonthlyInterest() {
    LocalDate today = LocalDate.now();
    if (today.getMonth() != lastInterestCredit.getMonth()) {
        // apply interest
        lastInterestCredit = today;
    }
}
```

### Common operations
```java
// Create
LocalDate date = LocalDate.now();               // today
LocalDate specific = LocalDate.of(2026, 7, 1); // specific date
LocalDateTime dt = LocalDateTime.now();

// Read parts
date.getYear();    // 2026
date.getMonth();   // JULY
date.getDayOfMonth(); // 1

// Compare
date.isBefore(LocalDate.of(2027, 1, 1));   // true
date.isAfter(LocalDate.of(2025, 1, 1));    // true
date.isEqual(LocalDate.now());             // true

// Add / subtract
date.plusDays(7);      // 7 days later
date.minusMonths(1);   // 1 month ago
date.plusYears(1);

// Format
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
dt.format(fmt);   // "2026-07-01 14:30:00"

// Duration between two times
Duration.between(start, end).toMinutes();
```

### In Spring Boot — entity fields
```java
@Entity
public class Account {
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
```

---

# PRIORITY 4 — Concurrency

---

## 45. Thread Basics

### What
A thread is an **independent path of execution** — multiple threads run at the same time (parallel).
JVM starts with one thread: `main`. You create more when needed.

### Why
Without threads, one slow operation (DB call, API call, file read) blocks everything.
Threads let your app do multiple things at once.

### When
Background tasks, async operations, parallel processing, handling multiple HTTP requests.

### Two ways to create a thread

**Way 1: extend Thread**
```java
// concurrency/basics/ThreadBasicsDemo.java
static class CountingThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[" + getName() + "] count " + i);
        }
    }
}

CountingThread t = new CountingThread();
t.run();    // ❌ runs on MAIN thread — just a normal method call
t.start();  // ✅ starts a NEW thread — run() executes in parallel
```

**Way 2: implement Runnable (preferred)**
```java
// concurrency/basics/ThreadBasicsDemo.java
static class PrintTask implements Runnable {
    private final String label;
    PrintTask(String label) { this.label = label; }

    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] task " + label);
    }
}

Thread t = new Thread(new PrintTask("A"), "worker-1");
t.start();

// Or with lambda (modern)
Thread t = new Thread(() -> System.out.println("Running"), "worker-1");
t.start();
```

### Key thread methods
```java
t.start();              // start the thread — DO NOT call run() directly
t.join();               // wait for this thread to finish before continuing
Thread.sleep(200);      // pause current thread for 200ms
Thread.currentThread(); // get reference to current thread
t.getName();            // thread name
t.isAlive();            // is thread still running?
```

### Thread lifecycle
```
NEW → RUNNABLE → RUNNING → BLOCKED/WAITING → TERMINATED
new Thread()  t.start()   CPU picks it   waiting for lock   run() finished
```

---

## 46. `synchronized` — Thread Safety

### What
`synchronized` ensures **only one thread can execute a block of code at a time** — mutual exclusion.

### Why
Without it, multiple threads reading and writing shared data cause **race conditions** — unpredictable, corrupted results.

### When
Any time multiple threads access and modify the same shared data.

### The problem — race condition
```java
// concurrency/synchronization/RaceConditionDemo.java
static class UnsafeCounter {
    long count = 0;
    void increment() { count++; }   // count++ is READ → ADD → WRITE (3 steps — not atomic)
}

// 4 threads × 100,000 increments = expected 400,000
// Actual result: LESS than 400,000 — threads overwrite each other's work
```

### Fix 1 — `synchronized` method
```java
// concurrency/synchronization/RaceConditionDemo.java
static class SyncCounter {
    long count = 0;
    synchronized void increment() { count++; }  // only one thread at a time
}
// Result: always exactly 400,000 ✅
```

### Fix 2 — `AtomicLong` (faster, no explicit lock)
```java
static class AtomicCounter {
    final AtomicLong count = new AtomicLong();
    void increment() { count.incrementAndGet(); }   // hardware-level atomic operation
}
```

### Real example — `Account.java`
```java
// models/Account.java
public synchronized boolean deposit(double amount, String description) throws BankingException {
    //       ↑ only one thread can deposit at a time
    validateTransaction(amount);
    balance += amount;    // safe — no race condition
    return true;
}

public synchronized void withdraw(double amount, String description) throws BankingException {
    //       ↑ deposit and withdraw cannot run at the same time on the same account
    validateTransaction(amount);
    balance -= amount;
}
```

```
Without synchronized:
    Thread A reads balance: 1000
    Thread B reads balance: 1000
    Thread A deposits 500 → writes 1500
    Thread B deposits 300 → writes 1300  ← Thread A's deposit LOST
    Final balance: 1300 (should be 1800)

With synchronized:
    Thread A enters, Thread B waits
    Thread A: reads 1000, writes 1500, exits
    Thread B: reads 1500, writes 1800, exits
    Final balance: 1800 ✅
```

---

## 47. `ExecutorService` — Thread Pools

### What
`ExecutorService` manages a **pool of reusable threads** — submit tasks, pool assigns them to threads.

### Why
Creating `new Thread()` for every task is wasteful and unbounded.
A thread pool has a fixed number of threads — tasks queue up and get processed in order.

### When
Any background task — sending emails, processing files, calling external APIs.

### Real example — `ExecutorServiceDemo.java`
```java
// concurrency/executor/ExecutorServiceDemo.java
ExecutorService pool = Executors.newFixedThreadPool(3);   // 3 reusable threads

try {
    // Submit a Runnable — fire and forget
    pool.submit(() -> System.out.println("ran on " + Thread.currentThread().getName()));

    // Submit a Callable — get result back
    Future<Integer> future = pool.submit(() -> {
        Thread.sleep(100);
        return 6 * 7;      // 42
    });
    int result = future.get();   // blocks until ready — result = 42

} finally {
    pool.shutdown();   // ALWAYS shut down — or JVM won't exit
}
```

### Common pool types
```java
Executors.newFixedThreadPool(4);       // fixed 4 threads
Executors.newSingleThreadExecutor();   // 1 thread — tasks run in order
Executors.newCachedThreadPool();       // grows/shrinks as needed
```

### In Spring Boot — `@Async`
```java
@Service
public class EmailService {

    @Async   // Spring runs this in a thread pool — does not block the caller
    public CompletableFuture<Void> sendStatement(Account account) {
        // send email — takes 2 seconds
        return CompletableFuture.completedFuture(null);
    }
}
```

---

## 48. `CompletableFuture` — Async Pipelines

### What
`CompletableFuture` lets you **chain async operations** without blocking — and run multiple in parallel.

### Why
Old `Future.get()` blocks the thread while waiting.
`CompletableFuture` chains what to do NEXT when the result is ready — non-blocking.

### When
Multiple independent API calls, async service calls, parallel data fetching.

### Real example — `CompletableFutureDemo.java`
```java
// concurrency/completablefuture/CompletableFutureDemo.java

// Chain: produce → transform → transform
CompletableFuture<String> chain = CompletableFuture
        .supplyAsync(() -> 21)               // runs in background, returns 21
        .thenApply(n -> n * 2)              // transform: 42
        .thenApply(n -> "answer = " + n);   // transform: "answer = 42"
System.out.println(chain.get());            // "answer = 42"

// Parallel — 3 calls at the same time
CompletableFuture<String> profile = CompletableFuture.supplyAsync(() -> fetchProfile());
CompletableFuture<String> orders  = CompletableFuture.supplyAsync(() -> fetchOrders());
CompletableFuture<String> notifs  = CompletableFuture.supplyAsync(() -> fetchNotifs());

CompletableFuture.allOf(profile, orders, notifs).join();   // wait for all 3
// Total time ≈ slowest one — NOT sum of all three
```

### Key methods
```java
CompletableFuture.supplyAsync(() -> value)     // run async, returns value
.thenApply(x -> transform(x))                  // transform result
.thenAccept(x -> consume(x))                   // consume result, return void
.thenCompose(x -> anotherFuture(x))            // chain another async call
.exceptionally(ex -> fallback)                 // handle error
.join()                                        // wait for result (unchecked)
.get()                                         // wait for result (checked)
CompletableFuture.allOf(f1, f2, f3).join()    // wait for all
```

---

## 49. `volatile`

### What
`volatile` ensures a field's value is **always read from main memory**, not from a thread's local cache.

### Why
Each thread may cache a field's value. Without `volatile`, one thread changes the value but another thread reads the old cached value.

### When
A simple flag shared between threads — not a counter (use `AtomicLong` for that).

```java
public class TaskRunner {
    private volatile boolean running = true;   // always read from main memory

    public void stop() {
        running = false;   // Thread A writes — immediately visible to Thread B
    }

    public void run() {
        while (running) {  // Thread B reads — sees Thread A's change immediately
            // process task
        }
    }
}
```

```
Without volatile:
    Thread A writes running = false → stored in Thread A's cache
    Thread B reads running          → reads its own cache: still true → infinite loop 💥

With volatile:
    Thread A writes running = false → written to main memory
    Thread B reads running          → reads main memory: false → loop exits ✅
```

---

# PRIORITY 5 — Design Patterns

---

## 50. Singleton Pattern

### What
Singleton ensures **only one instance of a class exists** throughout the application.

### Why
Some things should only exist once — database connection pool, config, logger.
Creating multiple instances wastes resources or causes inconsistency.

### When
Shared resources: config manager, connection pool, cache, logger.

### Real example — `BillPughSingleton.java`
```java
// design_pattern/singleton/BillPughSingleton.java
public class BillPughSingleton {

    private BillPughSingleton() { }   // private constructor — nobody can call new

    // Inner class — loaded only when getInstance() is called (lazy + thread-safe)
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}

// Usage
BillPughSingleton a = BillPughSingleton.getInstance();
BillPughSingleton b = BillPughSingleton.getInstance();
a == b;   // true — same object
```

### In Spring Boot — all beans are Singletons by default
```java
@Service
public class AccountService { }   // Spring creates ONE instance, shared by all

// Same object every time
AccountService s1 = context.getBean(AccountService.class);
AccountService s2 = context.getBean(AccountService.class);
s1 == s2;   // true — Spring singleton
```

---

## 51. Builder Pattern

### What
Builder creates **complex objects step by step** — separates construction from representation.

### Why
Constructors with many parameters are hard to read and error-prone.
Builder uses method chaining — each optional field is set explicitly by name.

### When
Objects with many fields, especially optional ones — API requests, DB queries, config objects.

### Real example — `Computer.java`
```java
// design_pattern/builder/Computer.java
Computer pc = new Computer.ComputerBuilder("500GB", "16GB")   // required
        .setGraphicsCardEnabled(true)   // optional
        .setBluetoothEnabled(false)     // optional
        .build();

// vs constructor — which value is which?
new Computer("500GB", "16GB", true, false);   // ❌ unclear
```

### In Spring Boot — Lombok `@Builder`
```java
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String accountNumber;
    private double balance;
    private AccountStatus status;
}

// Usage
Account acc = Account.builder()
        .accountNumber("ACC-001")
        .balance(1000.0)
        .status(AccountStatus.ACTIVE)
        .build();
```

---

## 52. Factory Pattern

### What
Factory creates objects **without exposing the creation logic** — you ask for a type, factory gives you the right object.

### Why
Decouples object creation from usage. You don't need to know which class to instantiate.

### When
When object creation depends on a type/condition. When you want to swap implementations.

### Real example — `ComputerFactory.java`
```java
// design_pattern/factory/ComputerFactory.java
public class ComputerFactory {

    public static Computer getComputer(String type, String ram, String hdd, String cpu) {
        if ("PC".equalsIgnoreCase(type))     return new PC(ram, hdd, cpu);
        if ("Server".equalsIgnoreCase(type)) return new Server(ram, hdd, cpu);
        return null;
    }
}

// Caller does not know about PC or Server classes
Computer c = ComputerFactory.getComputer("PC", "16GB", "500GB", "Intel i9");
```

### In Spring Boot — factory in repositories
```java
// Spring's JpaRepositoryFactory creates the right repository implementation
// You just define the interface — Spring's factory does the rest
public interface AccountRepository extends JpaRepository<Account, String> { }
```

---

## 53. Strategy Pattern

### What
Strategy defines a **family of algorithms** and makes them interchangeable — select the algorithm at runtime.

### Why
Without Strategy, you use if/else chains to switch between algorithms — hard to extend.
With Strategy, each algorithm is a separate class — add new one without changing existing code.

### When
Multiple ways to do the same job — payment processing, sorting, validation, discount calculation.

### Example — Payment strategies
```java
// Strategy interface
public interface PaymentStrategy {
    void pay(double amount);
}

// Implementations
public class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via credit card " + cardNumber);
    }
}

public class BankTransferStrategy implements PaymentStrategy {
    private String accountNumber;
    public void pay(double amount) {
        System.out.println("Transferred " + amount + " to " + accountNumber);
    }
}

// Context — uses any strategy
public class PaymentProcessor {
    private PaymentStrategy strategy;   // HAS-A strategy — composition

    public PaymentProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void process(double amount) {
        strategy.pay(amount);   // delegates to whichever strategy is set
    }
}

// Runtime — swap strategy without changing PaymentProcessor
PaymentProcessor p = new PaymentProcessor(new CreditCardStrategy("4321"));
p.process(150.0);   // credit card

p = new PaymentProcessor(new BankTransferStrategy("BK-9981"));
p.process(500.0);   // bank transfer
```

### In Spring Boot — Strategy via Spring beans
```java
// Each strategy is a Spring bean
@Component("creditCard")
public class CreditCardStrategy implements PaymentStrategy { ... }

@Component("bankTransfer")
public class BankTransferStrategy implements PaymentStrategy { ... }

// Inject by name
@Service
public class CheckoutService {
    @Qualifier("creditCard")
    private final PaymentStrategy paymentStrategy;
}
```

---

## 54. Observer Pattern

### What
Observer defines a **one-to-many dependency** — when one object changes, all its observers are notified automatically.

### Why
Decouples the event source from the event handlers. Add new handlers without changing the source.

### When
Events — account created, transaction completed, user logged in, order placed.

### Example — Account events
```java
// Observer interface
public interface AccountObserver {
    void onDeposit(String accountNumber, double amount);
}

// Two observers
public class AuditLogger implements AccountObserver {
    public void onDeposit(String accountNumber, double amount) {
        System.out.println("AUDIT: " + accountNumber + " deposited " + amount);
    }
}

public class NotificationService implements AccountObserver {
    public void onDeposit(String accountNumber, double amount) {
        System.out.println("SMS: Your account " + accountNumber + " received " + amount);
    }
}

// Subject — notifies all observers
public class Account {
    private List<AccountObserver> observers = new ArrayList<>();

    public void addObserver(AccountObserver observer) {
        observers.add(observer);
    }

    public void deposit(double amount) {
        balance += amount;
        observers.forEach(o -> o.onDeposit(accountNumber, amount));  // notify all
    }
}
```

### In Spring Boot — ApplicationEvent (built-in Observer)
```java
// Event class
public class AccountCreatedEvent extends ApplicationEvent {
    private final Account account;
    public AccountCreatedEvent(Object source, Account account) {
        super(source);
        this.account = account;
    }
}

// Publisher — fires the event
@Service
public class AccountService {
    private final ApplicationEventPublisher publisher;

    public Account createAccount(String customerId) {
        Account account = new Account(...);
        publisher.publishEvent(new AccountCreatedEvent(this, account));   // notify all listeners
        return account;
    }
}

// Listener — receives the event
@Component
public class WelcomeEmailListener {
    @EventListener
    public void onAccountCreated(AccountCreatedEvent event) {
        emailService.sendWelcome(event.getAccount());
    }
}
```

---

# PRIORITY 6 — JVM & Memory

---

## 55. Stack vs Heap

### What
Two memory areas where Java stores data:
- **Stack** — method calls and local variables. Fast, auto-managed.
- **Heap** — all objects created with `new`. Managed by Garbage Collector.

### Why
Understanding this helps you debug NullPointerExceptions, memory leaks, and pass-by-value behavior.

### When
Every time you declare a variable or create an object — this is happening in the background.

```
STACK (per thread)                   HEAP (shared by all threads)
──────────────────                   ─────────────────────────────
main() frame                         Account{ACC-001, balance=1000}
  id = "ACC-001"  ──────────────────► ↑ reference points here
  amount = 500.0
  account → ────────────────────────►

deposit() frame (called by main)
  amount = 500.0  (copy of primitive)
  (finished → frame popped off stack)
```

### Rules
```
Primitive → stored directly on STACK
Object    → stored on HEAP, reference (address) on STACK
Method call → new frame pushed on STACK, removed when method returns
```

### Real example — `Account.java`
```java
public boolean deposit(double amount, String description) {
    // amount      → primitive → stack frame (copy — changing it does not affect caller)
    // description → reference → stack frame points to String object on heap
    // balance     → this.balance = instance field → heap (same object as caller)
    balance += amount;   // modifies heap object — visible to caller
    return true;
}
```

### Stack overflow
```java
// Infinite recursion — stack keeps growing until memory runs out
public void infinite() {
    infinite();   // 💥 StackOverflowError — stack is full
}
```

---

## 56. Garbage Collection

### What
Garbage Collector (GC) **automatically frees heap memory** occupied by objects that are no longer reachable.

### Why
You never call `free()` in Java — GC handles memory for you.
Understanding GC helps you avoid memory leaks and write GC-friendly code.

### When
GC runs in the background automatically. You cannot control exactly when, but you can avoid patterns that stress it.

### Object becomes eligible for GC when no reference points to it
```java
Account acc = new Account("ACC-001", 1000.0);   // object on heap
acc = null;      // reference removed → object eligible for GC
acc = new Account("ACC-002", 2000.0);   // old object can be collected

// concurrency/garbage_collection/GarbageCollectionDemo.java shows this in action
```

### Memory leak — object still referenced but never used
```java
// Static map grows forever — objects never become unreachable
public class Cache {
    private static final Map<String, Account> cache = new HashMap<>();

    public void store(String key, Account account) {
        cache.put(key, account);   // added but never removed
    }
    // cache grows until OutOfMemoryError 💥
}

// Fix — use WeakHashMap or eviction policy
private static final Map<String, Account> cache = new WeakHashMap<>();
// GC can collect values when no other references exist
```

### GC-friendly habits
```java
// Close resources — prevents memory leaks
try (FileReader file = new FileReader("data.txt")) {
    // file auto-closed — no leak
}

// Avoid unnecessary object creation in loops
// ❌ Bad — creates new StringBuilder every iteration
for (int i = 0; i < 1000; i++) {
    String s = new StringBuilder().append("item").append(i).toString();
}

// ✅ Good — reuse StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.setLength(0);   // clear
    String s = sb.append("item").append(i).toString();
}
```

---

# PRIORITY 7 — Advanced

---

## 57. Generics Wildcards — `? extends T` and `? super T`

### What
Wildcards make generics more flexible — accept a range of types instead of one exact type.

### Why
Without wildcards, a `List<SavingsAccount>` cannot be passed where `List<Account>` is expected — even though `SavingsAccount extends Account`.

### When
Writing utility methods that work on collections of a type hierarchy.

### The problem
```java
public void printBalances(List<Account> accounts) {
    accounts.forEach(acc -> System.out.println(acc.getBalance()));
}

List<SavingsAccount> savings = new ArrayList<>();
printBalances(savings);   // ❌ ERROR — List<SavingsAccount> is not List<Account>
```

### `? extends T` — upper bound (read only)
```java
// Accepts List<Account>, List<SavingsAccount>, List<CheckingAccount>
public void printBalances(List<? extends Account> accounts) {
    accounts.forEach(acc -> System.out.println(acc.getBalance()));   // ✅ read
    accounts.add(new SavingsAccount());   // ❌ cannot add — type unknown
}

List<SavingsAccount> savings = new ArrayList<>();
printBalances(savings);   // ✅ now works
```

### `? super T` — lower bound (write only)
```java
// Accepts List<Account>, List<Object> — anything that is a supertype of SavingsAccount
public void addAccount(List<? super SavingsAccount> accounts) {
    accounts.add(new SavingsAccount("ACC-001", "C001", 1000.0, Currency.USD, 3.5));   // ✅ write
}
```

### PECS rule — Producer Extends, Consumer Super
```
If you READ from a collection → use `? extends T`
If you WRITE to a collection  → use `? super T`
If you do both                → use exact type `T`
```

---

## 58. SOLID Principles

### What
Five design principles that make code easier to maintain, extend, and test.

### Why
Code that violates SOLID becomes hard to change — one small change breaks many things.
Your project `design_principle/version1 → version3` shows the evolution toward SOLID.

### When
Every class you design — ask yourself: does this follow SOLID?

---

**S — Single Responsibility Principle (SRP)**
One class, one reason to change.

```java
// ❌ Violates SRP — Account handles business logic AND persistence AND email
public class Account {
    public void deposit(double amount) { balance += amount; }
    public void saveToDatabase() { ... }     // persistence — not Account's job
    public void sendEmail() { ... }          // notification — not Account's job
}

// ✅ Each class has one responsibility
public class Account         { void deposit(double amount) { ... } }
public class AccountRepository { void save(Account acc) { ... } }
public class NotificationService { void sendEmail(Account acc) { ... } }
```

---

**O — Open/Closed Principle (OCP)**
Open for extension, closed for modification.

```java
// ❌ Add new account type → must modify existing code
public double calculateFee(Account account) {
    if (account instanceof SavingsAccount) return 5.0;
    if (account instanceof CheckingAccount) return 10.0;
    // add BusinessAccount → modify this method
}

// ✅ Each type knows its own fee — extend by adding a class, not modifying existing
public abstract class Account {
    public abstract double getMaintenanceFee();
}
public class SavingsAccount extends Account {
    public double getMaintenanceFee() { return 5.0; }
}
public class CheckingAccount extends Account {
    public double getMaintenanceFee() { return 10.0; }
}
// Add BusinessAccount → just add new class, nothing else changes
```

---

**L — Liskov Substitution Principle (LSP)**
Child class must be usable wherever parent is expected — without breaking behavior.

```java
// ✅ SavingsAccount substitutes Account everywhere
Account acc = new SavingsAccount("ACC-001", "C001", 1000.0, Currency.USD, 3.5);
acc.deposit(500.0, "salary");    // works correctly
acc.getBalance();                 // works correctly
```

---

**I — Interface Segregation Principle (ISP)**
Don't force a class to implement methods it doesn't need.

```java
// ❌ Fat interface — CashPayment must implement refund() even though it can't
public interface Payment {
    void pay(double amount);
    void refund(double amount);   // CashPayment doesn't support this
}

// ✅ Split into focused interfaces
public interface Payable  { void pay(double amount); }
public interface Refundable { void refund(double amount); }

public class CreditCardPayment implements Payable, Refundable { ... }  // both
public class CashPayment implements Payable { ... }                     // pay only
```

---

**D — Dependency Inversion Principle (DIP)**
Depend on abstractions, not concrete classes.

```java
// ❌ High-level class depends on concrete low-level class
public class AccountService {
    private MySQLAccountRepository repository = new MySQLAccountRepository();
    // hard to test, hard to swap database
}

// ✅ Depend on interface — Spring injects the implementation
public class AccountService {
    private final AccountRepository repository;   // interface — not concrete class

    public AccountService(AccountRepository repository) {
        this.repository = repository;   // Spring injects MySQLAccountRepository
    }
}
```

---

## 59. File I/O

### What
File I/O means **reading from and writing to files** on disk.

### Why
Real apps read config files, write logs, export reports, process uploaded files.

### When
Config loading, report generation, file upload/download, data export.

### Write to file
```java
// fileio/WriteFileDemo.java
try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
    writer.write("ACC-001, Heang, 1500.00");
    writer.newLine();
    writer.write("ACC-002, Dara, 3200.00");
}
// auto-closed — no finally needed
```

### Read from file
```java
// fileio/ReadFileDemo.java
try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

### Modern way — `Files` utility (Java 7+)
```java
Path path = Path.of("accounts.txt");

// Read all lines
List<String> lines = Files.readAllLines(path);

// Write all lines
Files.write(path, List.of("ACC-001, Heang", "ACC-002, Dara"));

// Read entire file as String
String content = Files.readString(path);

// Check if file exists
Files.exists(path);

// Copy / move / delete
Files.copy(source, target);
Files.move(source, target);
Files.delete(path);
```

### In Spring Boot — `MultipartFile` upload
```java
@PostMapping("/upload")
public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
    byte[] bytes = file.getBytes();
    Path path = Path.of("uploads/" + file.getOriginalFilename());
    Files.write(path, bytes);
    return ResponseEntity.ok("Uploaded: " + file.getOriginalFilename());
}
```

---

## Quick Reference — All Priorities

| Priority | Topic | One-line rule |
|----------|-------|--------------|
| P1 | Primitives | `int/double/boolean` on stack — fast, no null |
| P1 | Objects | On heap — can be null, have methods |
| P1 | `==` vs `.equals()` | `==` for primitives/enum, `.equals()` for objects |
| P1 | String | Immutable — every change = new object |
| P1 | StringBuilder | Use in loops — much faster than `+` |
| P1 | Wrapper | `Integer/Double` needed for collections/generics |
| P1 | Array | Fixed size — prefer `List` for most cases |
| P1 | Pass by value | Primitives: copy. Objects: copy of reference |
| P2 | Encapsulation | `private` fields, `public` methods |
| P2 | Inheritance | IS-A — share code via `extends` |
| P2 | Polymorphism | One reference, many runtime behaviors |
| P2 | Abstraction | Hide complexity, show simple interface |
| P2 | Composition | HAS-A — prefer over inheritance |
| P2 | Comparable | One natural sort order — built into class |
| P2 | Comparator | External, multiple sort orders |
| P2 | Record | Immutable DTO — one line replaces 50 lines |
| P3 | Predicate | `T → boolean` — filter condition |
| P3 | Function | `T → R` — transform |
| P3 | Consumer | `T → void` — action |
| P3 | Supplier | `() → T` — produce value |
| P3 | Method ref `::` | Shortcut for lambda that calls one method |
| P3 | LocalDate | Date only — use for `dateOfBirth`, `lastCreditDate` |
| P3 | LocalDateTime | Date + time — use for `createdAt`, `updatedAt` |
| P4 | Thread | `start()` not `run()` — `join()` to wait |
| P4 | synchronized | One thread at a time — fixes race conditions |
| P4 | ExecutorService | Thread pool — submit tasks, pool manages threads |
| P4 | CompletableFuture | Chain async steps, run parallel with `allOf` |
| P4 | volatile | Shared flag — always read from main memory |
| P5 | Singleton | One instance — all Spring beans are singletons |
| P5 | Builder | Step-by-step construction — method chaining |
| P5 | Factory | Create objects without knowing the exact class |
| P5 | Strategy | Swap algorithms at runtime |
| P5 | Observer | One event → many listeners |
| P6 | Stack | Local variables, method frames — auto-managed |
| P6 | Heap | All objects — managed by GC |
| P6 | GC | Frees unreachable objects — avoid holding references |
| P7 | `? extends T` | Read from collection of subtypes |
| P7 | `? super T` | Write to collection of supertypes |
| P7 | SOLID | SRP, OCP, LSP, ISP, DIP — design every class by these |
| P7 | File I/O | `try-with-resources` + `Files` utility |
