# 📊 java-core-mastery-lab — Status & Learning Priority

> A review of what this lab already covers, what's missing or thin, and the
> **order to learn/practice next**. Based on the 50-concept roadmap in
> `Java_Main_GuideLine.md`. (162 Java files across 14 topic packages.)

---

## 1. Coverage snapshot

| Topic package | Status | Note |
|---------------|:------:|------|
| `oop` (abstraction, encapsulation, inheritance, polymorphism, interfaces) | ✅ strong | 18 files, all 4 pillars + equals/hashCode |
| `java8` (lambda, streams, functional, file I/O, security) | ✅ strong | 23 files |
| `applying_project` (bank, student, calculator) | ✅ strong | 24 files — good integration practice |
| `design_principle` (v1→v2→v3 SOLID refactor) | ✅ good | nice progressive refactor |
| `saasolv` (ex01–ex09) | ✅ new | generics, enums, exceptions, strategy, chain, threadlocal, annotation, config, ulid |
| `generic` | 🟡 partial | cache example; add wildcards `? extends/super`, bounded methods |
| `collection` | 🟡 partial | Map/Deque/dedup; **missing List & Set basics, iterators, Comparator** |
| `enumeration` | 🟡 ok | has constructor enums; could add EnumMap/EnumSet |
| `design_pattern` (builder, factory, prototype, singleton) | 🟡 partial | **missing Strategy, Observer, Adapter, Decorator, Template Method** |
| `fileio` / `garbage_collection` / `reserved_keywords` | 🟡 thin | 1–2 files each, fine as primers |
| `exception` | 🔴 **thin** | only `AppException.java`; subfolders `basics/chaining/custom/tryresources` are **empty** |
| `dsa` | 🔴 **empty** | no files at all — biggest content gap |
| **concurrency / multithreading** | 🔴 **missing** | only incidental (ThreadSafeSingleton, ThreadLocal). No threads/Runnable/ExecutorService/synchronized package |
| **modern Java (records, sealed, switch-expr, text blocks)** | 🔴 **missing** | project runs Java 21 but uses none of it |
| **JUnit tests** | 🔴 **missing** | 0 test files anywhere (see note below) |

---

## 2. Top improvements (ranked by value)

1. **Fill `exception/`** — it has empty folders begging to be filled: `basics`
   (try/catch/finally), `chaining` (wrap a cause), `custom` (extend RuntimeException),
   `tryresources` (AutoCloseable). Core topic, currently almost absent. ⚡
2. **Add a `concurrency/` package** — `Runnable` vs `Thread`, `synchronized`,
   `ExecutorService`/thread pool, `CompletableFuture`, `AtomicInteger`. Huge for
   interviews and enterprise; currently the biggest skill gap. ⚡
3. **Build out `dsa/`** — Big-O, array/linked-list, stack/queue, binary search,
   sorting, recursion. Empty today; essential for coding tests. ⚡
4. **Round out `design_pattern/`** — add Strategy, Observer, Adapter, Decorator,
   Template Method (you already meet several of these in `saasolv`). 🌟
5. **A `modernjava/` package** — `record`, `sealed` interfaces, `switch` expressions,
   pattern matching, text blocks, `var`. You're on Java 21 — use it. 🌟
6. **Strengthen `collection/`** — List/Set basics, `Iterator`, `Comparator`/`Comparable`,
   `Collections.sort`, when-to-use table.
7. **Testing** — no JUnit here because it's a plain-`javac` project (no build tool/jar).
   → Practice tests in **`spring-boot-lab`** instead (it has `spring-boot-starter-test`);
   your `BoardMapperTest` / `WebModeSecurityTest` there are good templates.

---

## 3. Priority order to LEARN & PRACTICE

> Ordered for steady momentum: solidify fundamentals → fill the loud gaps →
> level up to modern/advanced. ⚡ = do first, 🌟 = high value.

### Phase A — Solidify the core you already have (review, fast)
1. `oop` — re-run all 5 pillars; explain each out loud. ⚡
2. `java8` — lambdas, Stream `map/filter/collect`, `Optional`. ⚡ (used daily)
3. `collection` — add the missing List/Set/Comparator pieces while reviewing.

### Phase B — Fill the loud gaps (most growth here)
4. **`exception`** — fill the 4 empty subfolders. ⚡ → then redo `saasolv/ex03`.
5. **`concurrency`** (new) — threads → `ExecutorService` → `CompletableFuture`. ⚡🌟
6. **`dsa`** (new) — Big-O + array/list/stack/queue + binary search + 1 sort. ⚡
7. `saasolv/ex06_threadlocal` — revisit *after* concurrency; it'll click deeper.

### Phase C — Patterns & modern language
8. **`design_pattern`** — add Strategy & Observer first (most common). 🌟
   → ties to `saasolv/ex04_strategy` and `ex05_chain`.
9. **`modernjava`** (new) — convert a few existing VOs to `record`; try `sealed`
   + `switch` pattern matching. 🌟
10. `generic` — wildcards `? extends/super`, bounded methods → revisit `saasolv/ex01`.

### Phase D — Apply & prove
11. `saasolv/ex07_annotation`, `ex08_config`, `ex09_ulid` — the “impressive” trio
    (reflection, config binding, ULID). Do `ex09` as the capstone bit-manipulation kata.
12. `applying_project` — extend the bank/student app using everything above.
13. **Testing** — jump to `spring-boot-lab`, write JUnit tests for one feature.

---

## 4. The one-line recommendation

> You're strong on **OOP + Java 8 + small projects**. The fastest growth now is the
> three red gaps in this order: **exception handling → concurrency → DSA**, then
> **design patterns → modern Java**. Practice tests live in `spring-boot-lab`.

> Related: `dev-learning-notes/notes/egov-sample/` (saas-olv study notes & the
> `more-to-learn` deep-dive that inspired several items above).
