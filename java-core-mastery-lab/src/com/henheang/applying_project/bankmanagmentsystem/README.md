# Bank Management System

A console-based banking application built with Java Core, demonstrating
OOP principles, exception handling, file persistence, and clean architecture.

---

## Package Structure

```
bankmanagmentsystem/
│
├── BankManagementSystem.java       ← Entry point, UI menus, user interaction
│
├── enums/                          ← Fixed constant values (no logic)
│   ├── AccountStatus.java          ← ACTIVE, FROZEN, SUSPENDED, CLOSED
│   ├── Currency.java               ← USD, EUR, GBP, JPY, CAD
│   ├── LoanStatus.java             ← PENDING, APPROVED, ACTIVE, PAID_OFF, DEFAULTED
│   └── TransactionType.java        ← DEPOSIT, WITHDRAWAL, TRANSFER_IN/OUT, etc.
│
├── exceptions/                     ← Custom error types for banking rules
│   ├── BankingException.java       ← Base exception for all banking errors
│   ├── AccountNotFoundException.java
│   ├── InsufficientFundsException.java
│   └── InvalidTransactionException.java
│
├── models/                         ← Core data classes (the "things" in the system)
│   ├── Bank.java                   ← Manages all accounts, customers, loans
│   ├── Customer.java               ← Customer profile and password
│   ├── Account.java                ← Abstract base class for all account types
│   ├── SavingsAccount.java         ← Earns monthly interest, min balance $500
│   ├── CheckingAccount.java        ← Has overdraft protection, min balance $100
│   ├── BusinessAccount.java        ← Higher limits, stores business name & tax ID
│   ├── Transaction.java            ← Single transaction record with reference number
│   └── Loan.java                   ← Loan with monthly payment calculation
│
└── utilities/                      ← Helper/tool classes (reusable logic)
    ├── ValidationUtils.java        ← Validates email, phone, name, amount
    ├── SecurityUtils.java          ← Hashes passwords using SHA-256
    ├── ConsoleColor.java           ← ANSI color codes for colored terminal output
    └── DataPersistence.java        ← Saves and loads all bank data to/from file
```

---

## How to Run

```
# Normal mode
java BankManagementSystem

# Load demo data first, then start
java BankManagementSystem --demo

# Generate a system report (uses demo data)
java BankManagementSystem --report

# Run a stress test (100 customers, 1000 transactions)
java BankManagementSystem --stress

# Run daily maintenance (interest + fees)
java BankManagementSystem --maintenance
```

---

## Features

### Account Types
| Type     | Min Balance | Daily Limit  | Monthly Limit | Special Feature         |
|----------|-------------|--------------|---------------|-------------------------|
| Savings  | $500        | $5,000       | $50,000       | Monthly interest (3.5%) |
| Checking | $100        | $10,000      | $100,000      | Overdraft up to $1,000  |
| Business | $2,500      | $50,000      | $500,000      | Business name & Tax ID  |

### Security
- Passwords are hashed with **SHA-256** before storing — the plain password is never saved
- Fraud detection flags transfers that are >80% of balance or near daily limits

### Transactions
Every deposit, withdrawal, and transfer creates a `Transaction` record with:
- Unique reference number (e.g. `TXN20260402143022001`)
- Timestamp, amount, type, balance after, related account

### Loan System
- Apply for loans with 12–60 month terms
- Interest rates: 5.5% (12m) → 7.5% (60m)
- Monthly payment calculated using standard amortization formula
- Admin must approve before funds are disbursed

---

## Data Persistence

All data is saved automatically to `bank_data.dat` in the working directory after every:
- Customer registration
- Account creation
- Deposit / Withdrawal / Transfer
- Logout / Exit

On next startup, data is loaded from this file automatically.

### Copy data to another PC
1. Find `bank_data.dat` in the folder where you run the program
2. Copy it to the same folder on the other PC
3. Run the program — all customers, accounts, and transactions will be restored

---

## How Each Class Connects

```
BankManagementSystem
    └── uses Bank
            ├── stores Map<id, Customer>
            ├── stores Map<number, Account>
            │       ├── SavingsAccount
            │       ├── CheckingAccount
            │       └── BusinessAccount
            │               └── each holds List<Transaction>
            └── stores Map<id, Loan>

DataPersistence  → saves/loads the entire Bank object to bank_data.dat
SecurityUtils    → used by Customer to hash + verify passwords
ValidationUtils  → used by Bank to validate inputs before saving
ConsoleColor     → used by BankManagementSystem for colored UI output
```

---

## List and Map

Two of the most important data structures in Java. Both store multiple objects,
but they work very differently and are used for different purposes.

---

### `List` — ordered collection, accessed by position

**What it is:** A sequence of items where order matters. Each item has an index (0, 1, 2...).
**When to use:** When you need items in a specific order, or when you show all items together.

```java
// Create
List<String> names = new ArrayList<>();

// Add items — they stay in the order you added them
names.add("Alice");   // index 0
names.add("Bob");     // index 1
names.add("Charlie"); // index 2

// Access by position
String first = names.get(0);   // "Alice"
String second = names.get(1);  // "Bob"

// Loop through all in order
for (String name : names) {
    System.out.println(name);   // Alice, Bob, Charlie — always in this order
}

// Check size
int total = names.size();   // 3

// Remove
names.remove(0);   // removes "Alice", Bob becomes index 0
```

**How this project uses List — `Transaction` history inside `Account.java`:**
```java
// Transactions are stored in a List because order matters (oldest → newest)
protected final List<Transaction> transactions =
        Collections.synchronizedList(new ArrayList<>());

// Every deposit/withdraw adds to the end of the list
transactions.add(new Transaction(...));

// Display statement — shown in time order (first in = first shown)
for (Transaction tx : transactions) {
    statement.append(tx.toString()).append("\n");
}

// Filter by date range using Stream on the List
List<Transaction> periodTxns = transactions.stream()
    .filter(t -> !t.getTimestamp().toLocalDate().isBefore(startDate))
    .collect(Collectors.toList());
```

Why `List` here and not `Map`?
- Transactions don't need to be looked up by ID — they are always shown **all together in order**
- The position (first, second, last) tells you the history timeline

---

### `Map` — key-value pairs, accessed by key

**What it is:** A dictionary where each item has a unique key. You look up items by their key — not by position.
**When to use:** When you need to find a specific item quickly by its ID or name.

```java
// Create — Map<KeyType, ValueType>
Map<String, Customer> customers = new HashMap<>();

// Put — store with a key
customers.put("CUST3001CC79", customer1);
customers.put("CUSTAB12CD34", customer2);

// Get — retrieve instantly by key (no loop needed)
Customer c = customers.get("CUST3001CC79");   // direct jump — instant

// Check if key exists
boolean exists = customers.containsKey("CUST3001CC79");   // true

// Loop through all values
for (Customer customer : customers.values()) {
    System.out.println(customer.getFullName());
}

// Loop through all keys
for (String id : customers.keySet()) {
    System.out.println(id);
}

// Loop through both key and value together
for (Map.Entry<String, Customer> entry : customers.entrySet()) {
    System.out.println(entry.getKey() + " → " + entry.getValue().getFullName());
}

// Remove
customers.remove("CUST3001CC79");

// Size
int total = customers.size();
```

**How this project uses Map — inside `Bank.java`:**
```java
// 3 Maps — key is always the unique ID, value is the object
private final Map<String, Account>  accounts;   // key = "ACCT00010000"
private final Map<String, Customer> customers;  // key = "CUST3001CC79"
private final Map<String, Loan>     loans;      // key = "LOANAB12CD34"

// Login — find customer by ID instantly
Customer customer = customers.get(customerId);
if (customer == null) throw new BankingException("Customer not found");

// Deposit — find account by account number instantly
Account account = accounts.get(accountNumber);

// Approve loan — find loan by loan ID instantly
Loan loan = loans.get(loanId);

// Get total balance — loop through all account values
double total = accounts.values().stream()
    .mapToDouble(Account::getBalance)
    .sum();
```

Why `Map` here and not `List`?
- You always look up a customer/account/loan **by their ID** — `Map.get(id)` is instant
- With a `List` you would have to loop through every item every time — very slow with many records

---

### `HashMap` vs `ConcurrentHashMap`

#### First — what is a Thread?

A **thread** is like a worker doing a task. A normal program has 1 worker (1 thread).
A server (like Spring Boot) has **many workers at the same time** — one per HTTP request.

```
Single-threaded (normal program):
  Worker 1 → reads map → writes map → done ✓

Multi-threaded (Spring Boot server):
  Worker 1 → reads map →              → writes map ✗ (corrupt!)
  Worker 2 →             → writes map →
  (both workers touched the map at the same time → data is now wrong)
```

#### What "not thread-safe" actually means

Imagine `HashMap` as a shared notebook. Two people writing at the same time on the same line:

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Alice", 100);

// Thread 1 and Thread 2 both run at the same time:

// Thread 1:                        // Thread 2:
scores.put("Alice", 200);           scores.put("Alice", 300);

// Result: could be 200, could be 300, could be CORRUPTED — unpredictable
```

This is called a **race condition** — two threads race to write, and whoever wins
determines the result. In a bank, this could mean wrong balances or lost transactions.

#### How `ConcurrentHashMap` fixes it

`ConcurrentHashMap` uses **locking** — when one thread is writing a key,
other threads must wait before writing that same key.

```java
Map<String, Account> accounts = new ConcurrentHashMap<>();

// Thread 1: updating "ACCT00010000"
// Thread 2: updating "ACCT00010001"
// → BOTH can run at the same time because they touch DIFFERENT keys ✓

// Thread 1: updating "ACCT00010000"
// Thread 2: also updating "ACCT00010000"
// → Thread 2 WAITS until Thread 1 finishes ✓ (no corruption)
```

#### Full comparison

| | `HashMap` | `ConcurrentHashMap` |
|--|-----------|---------------------|
| Thread-safe | No | Yes |
| Speed (single thread) | Faster | Slightly slower |
| Speed (many threads) | Breaks / corrupts | Safe and efficient |
| Allows `null` key | Yes | No |
| Use in Spring Boot | Never for shared state | Always for shared state |
| Use in simple scripts | Fine | Fine |

#### When you will see this in Spring Boot

In Spring Boot, every HTTP request runs in its own thread. If you store shared
data in a bean (singleton), it is accessed by many threads at the same time:

```java
// Spring Boot — BAD (HashMap in a shared singleton bean)
@Service
public class CacheService {
    private Map<String, User> cache = new HashMap<>();   // NOT safe — multiple requests hit this
}

// Spring Boot — GOOD
@Service
public class CacheService {
    private Map<String, User> cache = new ConcurrentHashMap<>();   // safe for all requests
}
```

**Rule:** Any `Map` that is a field inside a Spring `@Service`, `@Component`,
or `@Repository` class must be `ConcurrentHashMap` — because those classes
are shared across all HTTP requests.

---

### `ArrayList` vs `Collections.synchronizedList` vs `CopyOnWriteArrayList`

The same thread-safety problem exists for `List`.

#### `ArrayList` — not thread-safe

```java
List<Transaction> list = new ArrayList<>();

// Thread 1: list.add(tx1)
// Thread 2: list.add(tx2)   ← at the same time
// Result: one transaction could be lost, or the list structure corrupts
```

#### `Collections.synchronizedList` — makes ArrayList thread-safe

```java
// Wraps ArrayList — every method (add, get, remove) is locked one at a time
List<Transaction> list = Collections.synchronizedList(new ArrayList<>());

// Safe for add/remove — one thread at a time
list.add(tx1);   // Thread 1 waits if Thread 2 is currently adding

// WARNING — iterating still needs manual synchronization
synchronized (list) {
    for (Transaction t : list) {   // must lock the list while looping
        System.out.println(t);
    }
}
```

Used in `Account.java` for the transaction history — deposits and withdrawals
from different threads won't corrupt the list.

#### `CopyOnWriteArrayList` — best for "read a lot, write rarely"

```java
// Every write makes a fresh COPY of the entire list — reads never blocked
List<String> list = new CopyOnWriteArrayList<>();

list.add("item");   // creates a new copy of the list internally

// Reading is always safe — no locking needed at all
for (String s : list) {   // safe without synchronized block
    System.out.println(s);
}
```

| | `ArrayList` | `synchronizedList` | `CopyOnWriteArrayList` |
|--|-------------|--------------------|-----------------------|
| Thread-safe | No | Yes | Yes |
| Read speed | Fast | Medium (locks) | Very fast (no lock) |
| Write speed | Fast | Medium (locks) | Slow (copies whole list) |
| Safe to iterate without lock | No | No | Yes |
| Best for | Single thread | Many reads + writes | Many reads, few writes |

#### When you will see this in Spring Boot

```java
// Spring Boot — list of active WebSocket sessions (many reads, rare writes)
@Component
public class SessionManager {
    // Users connect/disconnect rarely, but we read the list on every message
    private List<Session> sessions = new CopyOnWriteArrayList<>();   // best fit

    public void addSession(Session s)    { sessions.add(s); }
    public void removeSession(Session s) { sessions.remove(s); }
    public List<Session> getAll()        { return sessions; }   // safe, no lock needed
}

// Spring Boot — transaction log (many reads + many writes)
@Service
public class AuditService {
    private List<String> logs = Collections.synchronizedList(new ArrayList<>());

    public void log(String entry) { logs.add(entry); }
}
```

---

### Full picture — which to use when

```
Is it accessed by multiple threads (Spring service, shared field)?
│
├── NO  → HashMap / ArrayList  (simple, fast, single-threaded)
│
└── YES → Do you read more or write more?
          │
          ├── Balanced reads & writes
          │     Map  → ConcurrentHashMap
          │     List → synchronizedList
          │
          └── Read far more than write
                Map  → ConcurrentHashMap  (still best for Map)
                List → CopyOnWriteArrayList
```

---

### When to use List vs Map — decision guide

| Question | Answer | Use |
|----------|--------|-----|
| Do I look up items by a unique ID/key? | Yes | `Map` |
| Do I need items in a specific order? | Yes | `List` |
| Do I show all items together? | Yes | `List` |
| Do I need to check "does this ID exist?" | Yes | `Map` |
| Do I need the 1st, 2nd, 3rd item? | Yes | `List` |
| Do multiple threads access it? | Yes | `ConcurrentHashMap` or `synchronizedList` |

**In this project:**

| Data | Structure | Reason |
|------|-----------|--------|
| All customers | `ConcurrentHashMap` | Looked up by customer ID |
| All accounts | `ConcurrentHashMap` | Looked up by account number |
| All loans | `ConcurrentHashMap` | Looked up by loan ID |
| Transactions per account | `synchronizedList` | Shown in time order, not looked up by ID |
| Loan payments | `ArrayList` | Shown in order, single-threaded access |
| Fraudulent transaction logs | `synchronizedList` | Appended in order, thread-safe |

---

## Getters and Setters

### What are they?
- **Getter** — a `public` method that **reads** a `private` field
- **Setter** — a `public` method that **writes** a `private` field

### Why not just make fields `public`?

```java
// BAD — field is public, anyone can change it directly with no rules
public double balance = 5000.0;

// Somewhere else in the code — no one can stop this
account.balance = -99999999;   // corruption — no validation possible
account.balance = 0;           // someone zeroed the balance directly
```

```java
// GOOD — field is private, only controlled through methods
private double balance = 5000.0;

// The only way to change balance is through deposit/withdraw — which have rules
account.deposit(500, "Payroll");    // validated, logged, transaction recorded
account.withdraw(200, "ATM");       // checks minimum balance, daily limit, status
```

Making a field `private` and exposing it through getters/setters is called **Encapsulation** — one of the 4 pillars of OOP.

---

### Getters in this project

Getters return the value of a field without allowing it to be changed.

```java
// Customer.java — all fields are private
private final String customerId;
private String email;
private String hashedPassword;   // never exposed — no getter for this!

// Getters — read-only access to each field
public String getCustomerId() { return customerId; }
public String getEmail()      { return email; }
public String getFullName()   { return firstName + " " + lastName; }  // combines two fields
```

Notice:
- `hashedPassword` has **no getter** — you can never read the raw hash from outside
- `getFullName()` is not just returning a field — it **computes** the result from two fields
- `customerId` is `final` — it has a getter but **no setter** because it must never change

---

### Setters in this project

Setters allow controlled updates — the class decides what is allowed.

```java
// Customer.java — only email, phone, address can be updated
public void setEmail(String email)     { this.email = email; }
public void setPhone(String phone)     { this.phone = phone; }
public void setAddress(String address) { this.address = address; }

// firstName, lastName, dateOfBirth, customerId — NO setters
// These are final — they can never change after the customer is created
```

Notice what has **no setter on purpose**:
| Field | Why no setter |
|-------|--------------|
| `customerId` | Identity — must never change |
| `firstName` / `lastName` | Legal name — fixed at registration |
| `dateOfBirth` | Cannot change |
| `hashedPassword` | Not updated directly — use `updatePassword()` instead |

---

### Why `updatePassword()` instead of `setPassword()`?

```java
// If we had setPassword() — caller must know to hash it first (easy to forget)
customer.setPassword("newpass123");           // WRONG — stores plain text!
customer.setPassword(hash("newpass123"));     // caller must remember to hash

// updatePassword() handles hashing internally — caller just passes plain text safely
public void updatePassword(String newPassword) {
    this.hashedPassword = SecurityUtils.hashPassword(newPassword);
}

customer.updatePassword("newpass123");   // always hashed — no way to do it wrong
```

This is the key benefit of encapsulation — the class **enforces its own rules** so callers cannot misuse it.

---

### Getters in `Account.java`

`Account` also protects its fields — especially `balance`:

```java
// balance is protected — not public
protected double balance;

// Read balance — anyone can read it
public double getBalance() { return balance; }

// Change balance — ONLY through deposit() or withdraw() which have full validation
public synchronized boolean deposit(double amount, String description) throws BankingException {
    validateTransaction(amount);       // check amount is valid
    if (status != AccountStatus.ACTIVE) { ... }  // check account is active
    balance += amount;                 // only NOW we change balance
    addTransaction(...);               // record the transaction
    LOGGER.info(...);                  // log the event
}
```

If `balance` were `public`, any code could do `account.balance += 1000000` with no validation, no logging, and no transaction record.

---

### Summary: Getter vs Setter rules

| Rule | Reason |
|------|--------|
| All fields are `private` | No direct access from outside the class |
| Use `final` when field must never change | `customerId`, `accountNumber`, `createdDate` |
| Provide getter when others need to read the value | `getBalance()`, `getEmail()` |
| Provide setter only when the field is allowed to change | `setEmail()`, `setPhone()` |
| No setter — use a dedicated method when logic is involved | `updatePassword()`, `deposit()`, `withdraw()` |
| Never expose sensitive fields | `hashedPassword` has no getter at all |

---

## OOP Concepts Used

| Concept        | Where it appears                                              |
|----------------|---------------------------------------------------------------|
| Abstraction    | `Account` is abstract — subclasses define their own rules    |
| Inheritance    | `SavingsAccount`, `CheckingAccount`, `BusinessAccount` extend `Account` |
| Polymorphism   | `Bank` stores `Account` type, works with all subtypes        |
| Encapsulation  | All fields are `private`, accessed only through getters/setters |
| Exception      | Custom exceptions like `InsufficientFundsException` for clear errors |
| Serialization  | `implements Serializable` on all models for file persistence  |

---

## Java Technologies Used

### 1. `java.util.Map` + `ConcurrentHashMap`
**What it is:** A data structure that stores key-value pairs, like a dictionary.
**Why used:** To store and quickly look up customers, accounts, and loans by their ID.
**`ConcurrentHashMap` specifically:** Thread-safe — multiple operations can happen at the same time without corrupting data.
```java
// Key = account number (String), Value = the Account object
Map<String, Account> accounts = new ConcurrentHashMap<>();
accounts.put("ACCT00010000", savingsAccount);   // store
Account a = accounts.get("ACCT00010000");        // retrieve instantly
```

---

### 2. `java.util.concurrent.atomic.AtomicLong`
**What it is:** A `long` number that is safe to read/update from multiple threads at the same time.
**Why used:** To generate unique, auto-incrementing account numbers without duplicates.
```java
AtomicLong accountCounter = new AtomicLong(10000);
// getAndIncrement() returns current value then adds 1 — thread-safe
long nextId = accountCounter.getAndIncrement(); // 10000, 10001, 10002...
```

---

### 3. `java.util.stream` (Stream API)
**What it is:** A way to process collections (lists, maps) using chained operations — filter, map, sort, sum, etc.
**Why used:** To query data without writing long for-loops. Makes code shorter and more readable.
```java
// Example: get total balance of all active accounts
double total = accounts.values().stream()
    .filter(a -> a.getStatus() == AccountStatus.ACTIVE)  // keep only active
    .mapToDouble(Account::getBalance)                     // get each balance
    .sum();                                               // add them all up

// Example: get all loans for a specific customer
List<Loan> myLoans = loans.values().stream()
    .filter(loan -> loan.getAccountNumber().equals(myAccountNumber))
    .collect(Collectors.toList());
```

---

### 4. `java.security.MessageDigest` (SHA-256 Hashing)
**What it is:** A cryptographic tool that converts a string into a fixed-length hash that cannot be reversed.
**Why used:** To store passwords safely — the real password is never saved, only its hash.
```java
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] hashedBytes = md.digest("myPassword".getBytes());
// "myPassword" → "89e495e7941cf9e40e6980d14a16bf023ccd4c91"  (cannot go back)

// To verify login: hash what user types, compare to stored hash
boolean match = hashPassword(input).equals(storedHash);
```

---

### 5. `java.util.regex.Pattern`
**What it is:** A pattern matcher that checks if a string follows a specific format (email, phone, etc.).
**Why used:** To validate user input before saving — prevent bad data entering the system.
```java
Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

// Returns true if the email matches the pattern, false otherwise
boolean valid = EMAIL_PATTERN.matcher("heang@gmail.com").matches(); // true
boolean valid2 = EMAIL_PATTERN.matcher("not-an-email").matches();   // false
```

---

### 6. `java.time` (Date & Time API)
**What it is:** Modern Java date/time classes — `LocalDate` (date only), `LocalDateTime` (date + time).
**Why used:** To record exactly when each transaction happened and to calculate dates for loans and interest.
```java
LocalDateTime now = LocalDateTime.now();           // 2026-04-02T14:30:00
LocalDate today = LocalDate.now();                 // 2026-04-02
LocalDate nextMonth = today.plusMonths(1);         // 2026-05-02

// Format for display
String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
// → "2026-04-02 14:30:00"
```

---

### 7. `java.io.Serializable` + `ObjectOutputStream` / `ObjectInputStream`
**What it is:** Java's built-in way to convert objects into bytes (save to file) and back (load from file).
**Why used:** To persist all bank data between sessions without a database.
```java
// SAVE — convert Bank object → bytes → write to file
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("bank_data.dat"));
out.writeObject(bank);   // entire Bank with all customers/accounts saved

// LOAD — read bytes from file → convert back to Bank object
ObjectInputStream in = new ObjectInputStream(new FileInputStream("bank_data.dat"));
Bank bank = (Bank) in.readObject();   // fully restored
```
> `transient` keyword skips a field during serialization (used for `Logger` since it cannot be saved).

---

### 8. `java.util.logging.Logger`
**What it is:** Java's built-in logging tool — records what happens in the system to a log file or console.
**Why used:** To track important events (deposits, logins, errors) for debugging without cluttering `System.out`.
```java
Logger LOGGER = Logger.getLogger(Bank.class.getName());

LOGGER.info("New account created: " + accountNumber);     // normal info
LOGGER.severe("System error: " + e.getMessage());          // serious error
// Output goes to console or log file, separate from user-facing output
```

---

### 9. `synchronized` keyword
**What it is:** Ensures only one thread can execute a block of code at a time.
**Why used:** To prevent two transfers from happening at the same time on the same account, which could cause wrong balances.
```java
// Only one thread can run deposit() on this account at a time
public synchronized boolean deposit(double amount, String description) {
    balance += amount;   // safe — no other thread can change balance at the same time
}

// Transfer locks BOTH accounts before modifying either
synchronized (fromAccount) {
    synchronized (toAccount) {
        fromAccount.balance -= amount;
        toAccount.balance += amount;
    }
}
```

---

### 10. Switch Expression (Java 14+)
**What it is:** A modern version of `switch` that returns a value directly.
**Why used:** Cleaner than old `switch` with `break` — less code, no fall-through bugs.
```java
// Old switch (verbose, easy to forget break)
String type;
switch (choice) {
    case 1: type = "savings"; break;
    case 2: type = "checking"; break;
    default: type = "unknown";
}

// New switch expression (clean, returns value directly)
String type = switch (choice) {
    case 1 -> "savings";
    case 2 -> "checking";
    default -> throw new BankingException("Invalid type");
};
```

---

### 11. Pattern Matching `instanceof` (Java 16+)
**What it is:** Checks the type of an object AND casts it in one line.
**Why used:** To handle `SavingsAccount` and `BusinessAccount` differently when displaying accounts.
```java
// Old way (check then cast separately)
if (account instanceof SavingsAccount) {
    SavingsAccount sa = (SavingsAccount) account;
    System.out.println(sa.getInterestRate());
}

// New pattern matching (check + cast in one line)
if (account instanceof SavingsAccount sa) {
    System.out.println(sa.getInterestRate());   // sa is already cast
}
```

---

### 12. `enum` (Enumeration)
**What it is:** A special type that holds a **fixed set of named constants** — values that never change and are known at compile time.
**Why used:** To restrict a field to only valid values. Without enum, you could accidentally pass `"ACTVE"` (typo) or `"deleted"` (invalid) as a String and the compiler would not catch it.

```java
// WITHOUT enum — compiler cannot protect you
String status = "ACTVE";    // typo — no error, but logic breaks silently
String status = "deleted";  // not a valid status — no error at all

// WITH enum — compiler catches all mistakes immediately
AccountStatus status = AccountStatus.ACTVE;    // compile error — caught!
AccountStatus status = AccountStatus.deleted;  // compile error — doesn't exist!
AccountStatus status = AccountStatus.ACTIVE;   // correct
```

**This project uses 4 enums:**

| Enum | Values | Used In | Purpose |
|------|--------|---------|---------|
| `AccountStatus` | `ACTIVE, FROZEN, SUSPENDED, CLOSED` | `Account` | Controls what operations are allowed on the account |
| `Currency` | `USD, EUR, GBP, JPY, CAD` | `Account`, `Transaction` | Defines which currency the account holds |
| `LoanStatus` | `PENDING, APPROVED, ACTIVE, PAID_OFF, DEFAULTED` | `Loan` | Tracks the lifecycle stage of a loan |
| `TransactionType` | `DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST, FEE, LOAN_DISBURSEMENT, LOAN_PAYMENT, REVERSAL` | `Transaction` | Labels what kind of transaction was recorded |

**Enum works perfectly with switch — compiler warns if you miss a case:**
```java
// If you add a new status to the enum, compiler tells you to handle it here too
switch (account.getStatus()) {
    case ACTIVE    -> processTransaction();
    case FROZEN    -> throw new InvalidTransactionException("Account is frozen");
    case CLOSED    -> throw new InvalidTransactionException("Account is closed");
    case SUSPENDED -> throw new InvalidTransactionException("Account is suspended");
}
```

**Enum can also carry data and methods:**
```java
// TransactionType carries a human-readable display name
enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER_IN("Transfer In");

    private final String displayName;

    TransactionType(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}

// Used in Transaction.toString() to show readable label instead of "TRANSFER_IN"
type.getDisplayName()  // → "Transfer In"
```

---

### Summary Table

| Technology               | Package                       | Used For                              |
|--------------------------|-------------------------------|---------------------------------------|
| `ConcurrentHashMap`      | `java.util.concurrent`        | Thread-safe storage of data           |
| `AtomicLong`             | `java.util.concurrent.atomic` | Thread-safe ID generation             |
| Stream API               | `java.util.stream`            | Querying collections cleanly          |
| `MessageDigest` SHA-256  | `java.security`               | Password hashing                      |
| `Pattern` / Regex        | `java.util.regex`             | Input validation                      |
| `LocalDate/DateTime`     | `java.time`                   | Recording timestamps                  |
| `Serializable` / IO      | `java.io`                     | Saving/loading data to file           |
| `Logger`                 | `java.util.logging`           | System event logging                  |
| `synchronized`           | Java keyword                  | Thread safety for transactions        |
| Switch Expression        | Java 14+                      | Cleaner conditional logic             |
| Pattern Matching         | Java 16+                      | Cleaner type checking and casting     |
| `enum`                   | Java keyword                  | Type-safe fixed set of constants      |