# equals() + hashCode() — Notes

> Built around `EqualHashCodeDemo.java` and `BankAccount.java` in this folder.

---

## The experiment

```java
BankAccount a = new BankAccount("Heang", "Acc-001", 100);
BankAccount b = new BankAccount("Heang", "Acc-001", 100);

Set<BankAccount> accounts = new HashSet<>();
accounts.add(a);
accounts.add(b);

System.out.println("Accounts: " + a.equals(b));   // true
System.out.println("Accounts Size: " + accounts.size()); // 1
```

| | WITHOUT overrides | WITH overrides (my code) |
|---|---|---|
| Compared by | memory address | `accountNumber` |
| `a.equals(b)` | `false` | `true` |
| Set size | `2` | `1` |

The ONLY thing that changed the result is **my definition of what "equal" means** for a BankAccount.

---

## Why the output is `true` / `1`

`a` and `b` are two **separate objects** (different memory) but same account number `"Acc-001"`.

When `accounts.add(b)` runs, the set asks *"do I already have something equal to b?"* in **2 steps**:

1. **`hashCode()`** → `Objects.hash(getAccountNumber())` → same number for a and b → jump to the same bucket, finds `a`.
2. **`equals()`** → `"Acc-001".equals("Acc-001")` → `true` → b is a **duplicate** → rejected.

Result: only `a` is stored → size `1`.

> Same hashCode does NOT prove equality (rare collisions exist), so `equals()` always confirms. Both steps are needed.

---

## My overrides (in BankAccount.java)

```java
import java.util.Objects;

@Override
public boolean equals(Object o) {
    if (this == o) return true;                       // same object
    if (o == null || getClass() != o.getClass()) return false;
    BankAccount other = (BankAccount) o;
    return getAccountNumber().equals(other.getAccountNumber());
}

@Override
public int hashCode() {
    return Objects.hash(getAccountNumber());
}
```

### The CONTRACT (never break this)
- If `a.equals(b)` is true → `a.hashCode()` MUST equal `b.hashCode()`.
- Use the **same field(s)** in both methods (here: `accountNumber`).

---

## HashSet is secretly a HashMap

```java
// inside the JDK:
public class HashSet<E> {
    private HashMap<E, Object> map;            // a HashMap!
    private static final Object PRESENT = new Object();

    public boolean add(E e) {
        return map.put(e, PRESENT) == null;    // element stored as the KEY
    }
}
```

- `HashSet` = a `HashMap` where you only care about the keys.
- `HashMap` requires **unique keys** → it detects duplicate keys with the SAME 2-step `hashCode()` + `equals()` process.
- So learning this on a Set teaches HashMap too.

Map version of the same lesson:
```java
Map<BankAccount, Double> balances = new HashMap<>();
balances.put(a, 100.0);
balances.put(b, 200.0);   // same key as a → OVERWRITES, not added
balances.size();          // 1   (and a's value is now 200.0)
```

---

## Is this commonly used? → YES, everywhere

- `HashMap` / `HashSet` duplicate checks and key lookups (constant use)
- `list.contains(x)`, Streams `.distinct()`, `Collectors.groupingBy`
- Caching (caches are maps keyed by objects)
- **JPA / Hibernate entities** — `Set<Order> orders`, entity comparison. Famous bug source if wrong.

### For Spring Boot / banking goal
Standard pro advice: override `equals`/`hashCode` on the **business key (ID)** — exactly what I did with `accountNumber`. Banks treat account number as the unique identifier, so this is the correct real-world design.

```java
@Entity
public class Account {
    @Id
    private String accountNumber;   // equals/hashCode based on this
}
```

---

## TL;DR
1. A collection doesn't know what "duplicate" means — **you** define it via `equals()` + `hashCode()`.
2. `hashCode()` finds the bucket (fast), `equals()` confirms the match (exact). Keep them consistent.
3. `HashSet` is a `HashMap` of keys; both use the same logic.
4. Override on the unique ID — this is core Java + core Spring Boot/JPA.
