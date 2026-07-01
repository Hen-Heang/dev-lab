# Exercise: Abstract Class & Polymorphism
## Topic: Payment System

---

## What you will practice

- Reading and understanding an abstract class
- Implementing abstract methods in child classes
- Using polymorphism (one list, many types)
- Implementing an interface on selected classes only

---

## Files given to you (READ — do not change)

| File | Role |
|------|------|
| `Payment.java` | Abstract class — the blueprint |
| `Refundable.java` | Interface — refund contract |
| `ExerciseMain.java` | Main class — shows expected output |

---

## Files you must complete (look for TODO)

| File | What you implement |
|------|--------------------|
| `CreditCardPayment.java` | Constructor + 3 abstract methods + refund |
| `BankTransferPayment.java` | Constructor + 3 abstract methods + refund |
| `CashPayment.java` | Constructor + 3 abstract methods (no refund) |
| `PaymentProcessor.java` | processBatch() + refundAll() using polymorphism |

---

## Rules

1. Do NOT change `Payment.java`, `Refundable.java`, or `ExerciseMain.java`
2. Each TODO must be replaced with real code
3. Run `ExerciseMain` and match the expected output exactly

---

## Expected output when you run ExerciseMain

```
===== PROCESSING PAYMENTS =====
[CREDIT CARD] Paid $150.00 via card **** 4321 (fee: $3.00)
[BANK TRANSFER] Transferred $500.00 to account BK-9981
[CASH] $75.00 cash received by cashier: Dara

===== REFUNDING =====
[CREDIT CARD] Refunded $150.00 to card **** 4321
[BANK TRANSFER] Refunded $500.00 to account BK-9981

===== SUMMARY =====
Total payments: 3
Total refundable: 2
```

---

## Hints (read only if stuck)

<details>
<summary>Hint 1 — CreditCardPayment</summary>

- Add a `private String cardNumber` field
- `getPaymentType()` returns "CREDIT CARD"
- `execute()` prints the line shown in expected output
- `getFee()` returns `amount * 0.02`
- `refund()` prints the refund line
</details>

<details>
<summary>Hint 2 — Polymorphism in PaymentProcessor</summary>

- `processBatch` takes `List<Payment>` — loop and call `payment.processPayment()`
- `refundAll` takes `List<Refundable>` — loop and call `r.refund(r.getAmount())`
</details>
