# Bank-Readiness — java-core-mastery-lab

> What to fix in this project to match the Cambodia bank goal
> (see `dev-learning-notes/curriculum/CAMBODIA-CAREER-PLAN.md` and
> `ROADMAP.md`). Target: Korean-owned banks first (Oracle PL/SQL,
> core banking). This repo is my **core Java** proof. Fix it in
> priority order. Mentor reviews; I write the code.
>
> **This repo stays PLAIN JAVA on purpose** — no Maven/Gradle, no
> JUnit, no frameworks. I practise pure Java here and verify with
> small `main()` demos and hand-written checks. The framework version
> (Spring Boot, Maven, JUnit, PL/SQL) is a different repo — see P7.

## What is already good (keep it)

- `applying_project/bankmanagmentsystem` — real domain: Account,
  SavingsAccount, CheckingAccount, BusinessAccount, Customer, Loan,
  Transaction; a clean custom exception hierarchy (`BankingException`
  → `InsufficientFundsException`, `InvalidTransactionException`,
  `AccountNotFoundException`).
- `Account` uses `synchronized`, `AtomicLong`, `synchronizedList`,
  `Serializable` with `serialVersionUID`, streams, `LocalDateTime`.
- Good spread of topics: oop, collection, generic, java8,
  design_pattern, design_principle, enumeration.

## P1 — Money must be BigDecimal, never double (DISQUALIFIER)

This is the #1 thing a bank interviewer fails you on. Right now
`balance`, `amount`, limits, fees — all `double` across the bank
system. `double` loses cents (`0.1 + 0.2 != 0.3`). My own ROADMAP
Phase 1 says "BigDecimal for money (never double!)" — my flagship
project breaks my own rule.

- [ ] Change `double` → `BigDecimal` for: `balance`, `amount`,
      `dailyTransactionLimit`, `monthlyTransactionLimit`,
      `currentMonthlyTransactions`, minimum balance, fees, interest,
      loan amounts. Files: `models/*.java`, `BankManagementSystem.java`,
      `utilities/ValidationUtils.java`.
- [ ] Compare with `.compareTo()`, never `==` or `<`/`>`.
- [ ] Set scale + `RoundingMode` in ONE agreed place (e.g.
      `setScale(2, RoundingMode.HALF_EVEN)`).
- [ ] Construct from `String`/`valueOf`, not `new BigDecimal(0.1)`.

Interview line to be able to say: *why double is wrong for money, and
exactly how BigDecimal fixes it.*

## P2 — Verify behaviour with plain-Java checks (no framework)

This repo stays plain Java, so I do NOT add Maven or JUnit here. I
still prove my code works — using core Java only:

- [ ] Write a small demo class with a `main()` (e.g.
      `AccountDemo.main`) that runs scenarios and prints results:
      deposit increases balance; withdraw decreases; over-balance
      withdraw throws `InsufficientFundsException`; frozen/closed
      account rejects a transaction.
- [ ] Use plain `assert` statements (run with `java -ea`) or a tiny
      hand-written `check(condition, message)` helper to fail loudly
      when a result is wrong. This teaches me what JUnit does *under
      the hood*.
- [ ] These checks also lock in the P1 BigDecimal change — run them
      after converting `double` → `BigDecimal`.

> The real **JUnit + Maven** practice happens in the framework repo
> (P7), where I use the `/java-junit` skill. Keeping this lab pure
> means I understand the fundamentals before the tools hide them.

## P3 — equals() / hashCode() on domain models

ROADMAP Phase 1 (Weeks 9–10). `Account` and `Customer` have none, so
they break as `HashSet`/`HashMap` keys. Bank interview classic.

- [ ] Implement `equals`/`hashCode` on `Account` (by `accountNumber`),
      `Customer` (by `customerId`), `Transaction` (by id). Use the
      business identity, not every field.
- [ ] Be ready to explain the equals/hashCode contract out loud.

## P4 — Prove the concurrency is correct (don't just claim it)

`Account.deposit/withdraw` are `synchronized` — good — but nothing
proves it. The double-withdrawal / lost-update race is THE bank
question, and it leads straight into SQL `SELECT ... FOR UPDATE`
(see `/plsql`).

- [ ] Plain-Java demo: spin up N threads (`Thread`/`ExecutorService`)
      that deposit concurrently, `join()` them, then check the final
      balance is exact (no lost updates). Try it once WITHOUT
      `synchronized` to watch it fail, then with it to see it pass.
- [ ] Note the link: this is the same problem PL/SQL solves with row
      locks in a transfer procedure.

## P5 — Rebuild exception depth

`src/com/henheang/exception/` has collapsed to one file
(`AppException.java`). ROADMAP Phase 1 (Weeks 11–12).

- [ ] Re-create small demos: checked vs unchecked, exception chaining
      (`cause`), try-with-resources, a custom exception hierarchy.
      (The bank system's `exceptions/` package is a good model — study
      WHY it is shaped that way.)

## P6 — Housekeeping

- [ ] Add a `.gitignore` (at least `out/` and `.idea/`). Compiled
      `out/` artifacts should not be in git. (Keep `*.iml` if I rely
      on the IntelliJ module to run plain Java.)
- [ ] Expand `README.md` from a link list into: what this repo is,
      a package map, and how to compile/run the demos with plain
      `javac`/`java` (and `-ea` to enable asserts).

## P7 — The bridge to the bank stack (not in this repo)

This repo is pure core Java — that's correct for Phase 1. The same
domain (Account, Transaction, **transfer**) becomes the bank capstone
in **Spring Boot + Oracle PL/SQL**: REST API + a PL/SQL transfer
procedure (lock → debit → credit → ledger) + a Bakong/KHQR QR flow.
That work lives in `spring-boot-lab` / `spring_jwt_authentication`,
not here. Keep this repo as the clean core-Java foundation.

## Suggested order

P1 (money) → P2 (plain-Java demo checks, lock in P1) → P3 (equals/
hashCode) → P4 (concurrency demo) → P5 (exceptions) → P6 → P7.
</content>
