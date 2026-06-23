# Task — Pass-by-value with objects

Write a small `main()` program (plain Java, one file) that proves
this rule with a banking-flavored example.

## Requirements

1. Create a tiny `Account` class with one field `balance` (double is
   fine for this exercise — it's not about money precision today) and
   a `deposit(double amt)` method that adds to `balance`, plus a
   `getBalance()`.
2. Write a method `attemptReplace(Account acc)` that:
   - calls `acc.deposit(50)`
   - then does `acc = new Account();` (a brand-new account with
     balance 0)
3. In `main`:
   - create an `Account` with balance 100
   - call `attemptReplace(yourAccount)`
   - print `yourAccount.getBalance()`
4. Before running it — write down (as a comment) what you PREDICT the
   printed balance will be, and why.
5. Run it. Was your prediction right?

## Where to put it

`exercises/phase1-pass-by-value/PassByValueDemo.java`

Stop here and write your attempt. Tell me when you're done — I will
review it (PAIR MODE), not rewrite it.
