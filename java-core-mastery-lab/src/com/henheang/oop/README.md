# OOP Concepts — Java Core Mastery Lab

---

## The 4 Pillars of OOP

| # | Pillar | One-line summary | Your code |
|---|---|---|---|
| 1 | **Encapsulation** | Hide data, expose only what is needed | `encapsulation/BankAccount.java` |
| 2 | **Inheritance** | Child class reuses parent class fields and methods | `inheritance/Animal → Dog, Cat` |
| 3 | **Polymorphism** | Same method name, different behavior per class | `polymorphism/Shape → Circle, Rectangle` |
| 4 | **Abstraction** | Hide complexity, show only the interface | `abstraction/Vehicle → Car, Duck` |

---

## 1. Encapsulation

> **"Keep your data private. Control access through methods."**

```
BankAccount
├── private double balance   ← cannot be touched from outside
├── deposit(amount)          ← controlled way to add money
├── withdraw(amount)         ← controlled way to remove money
└── getBalance()             ← read-only access
```

**Rules:**
- Fields are `private`
- Use `getters` to read, `setters` to write (only when needed)
- Validation logic lives inside the class — callers cannot bypass it

---

## 2. Inheritance

> **"Child class gets everything from parent. Extend or override as needed."**

```
Animal (parent)
├── name, sound              ← shared fields
├── makeSound()              ← shared method
│
├── Dog (child)              ← inherits everything
│   └── fetch()              ← Dog-only method
│
└── Cat (child)              ← inherits everything
    └── purr()               ← Cat-only method
```

**Rules:**
- Use `extends` keyword
- Child calls `super(...)` in constructor to initialize parent fields
- `@Override` to replace a parent method with child-specific behavior
- Java allows only ONE parent class (single inheritance)

---

## 3. Polymorphism

> **"One type, many forms. The same method call behaves differently depending on the object."**

```
Shape[] shapes = { new Circle(), new Rectangle() };

for (Shape s : shapes) {
    s.area();   // Circle calculates π*r², Rectangle calculates w*h
                // same call, different result — that is polymorphism
}
```

**Two types:**
- **Runtime polymorphism** — `@Override` in subclass, decided at runtime
- **Compile-time polymorphism** — method overloading (same name, different parameters)

---

## 4. Abstraction

> **"Define WHAT must be done. Let subclasses decide HOW."**

```
Vehicle (abstract)
├── abstract fuelType()      ← no body — MUST be implemented by subclass
├── move()                   ← shared logic, already implemented
│
├── Car   → fuelType() = "gasoline"
└── Duck  → fuelType() = "water" + fly() + swim()
```

**Two tools for abstraction:**

| Tool | Keyword | Can have fields? | Can have concrete methods? | Multiple? |
|---|---|---|---|---|
| Abstract class | `abstract class` | Yes | Yes | No (extends one only) |
| Interface | `interface` | No (only constants) | No (only signatures) | Yes (implements many) |

**When to use which:**
- Use **abstract class** when subclasses share common fields or logic
- Use **interface** when you want to define an ability a class CAN have (`Flyable`, `Swimmable`)

---

## How the 4 Pillars Work Together

```
Abstraction   → defines the blueprint (what must exist)
Encapsulation → protects the data inside the blueprint
Inheritance   → shares the blueprint across related classes
Polymorphism  → lets different classes respond to the same call differently
```

---

## Common Use in Spring Boot

Spring Boot uses OOP concepts everywhere. Here is what you will see most:

### Encapsulation — used in every class
```java
@Entity
public class User {
    @Id
    private Long id;          // private field
    private String name;

    public String getName() { return name; }   // getter
    public void setName(String name) { this.name = name; }  // setter
}
```
Every `@Entity`, `@RequestBody`, `DTO`, and model class uses encapsulation.

---

### Abstraction (Interface) — the most important one in Spring Boot
```java
// You define the interface
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

// Spring Boot generates the implementation for you at runtime
// You never write the HOW — Spring handles it
```
Also seen in:
- `@Service` classes implementing a service interface
- `@Repository` extending `JpaRepository`
- `SecurityFilterChain`, `UserDetailsService`

---

### Inheritance — used in exceptions and base classes
```java
// Custom exception inherits from RuntimeException
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);   // calls RuntimeException constructor
    }
}
```
Also seen in:
- `@ControllerAdvice` base classes
- Extending `WebSecurityConfigurerAdapter` (older Spring Security)
- Base entity classes with shared `createdAt`, `updatedAt` fields

---

### Polymorphism — used in services and dependency injection
```java
public interface PaymentService {
    void pay(double amount);
}

@Service
public class CreditCardService implements PaymentService {
    public void pay(double amount) { /* credit card logic */ }
}

@Service
public class PayPalService implements PaymentService {
    public void pay(double amount) { /* paypal logic */ }
}

// Controller only knows PaymentService — not which one
@RestController
public class OrderController {
    private final PaymentService paymentService;  // polymorphism here

    public OrderController(PaymentService paymentService) {
        this.paymentService = paymentService;     // Spring injects the right one
    }
}
```

---

## Priority for Spring Boot Learning

| Priority | Concept | Why |
|---|---|---|
| Must know | **Interface (Abstraction)** | Spring Boot is built on interfaces — Repository, Service, Security |
| Must know | **Encapsulation** | Every model/entity/DTO uses private fields + getters/setters |
| Must know | **Polymorphism** | Dependency injection relies on it — inject interface, get implementation |
| Good to know | **Inheritance** | Custom exceptions, base entity classes, Spring Security config |