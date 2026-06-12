# Web Application with Spring Boot — Learning Path (Basic to Advanced)
> Study these projects in order. Each one builds on the previous.
> Folder: `2. Web Application with Spring Boot`
> Prerequisite: finish `1. Introduction To Spring Boot` first.

---

## Why This Section?

Spring Core taught you *how Spring manages objects*. This section teaches you *how Spring turns an HTTP request into a response*. By the end you should be able to draw the request flow from memory and know when to reach for blocking MVC vs. reactive WebFlux.

---

## Learning Order

```
Step 1 → #1 spring-boot-3-bean-config   (Wiring beans into a real feature)
Step 2 → #2 spring-boot-3-web-app        (Spring MVC: the request flow)
Step 3 → #3 spring-boot-webflux          (Reactive, non-blocking web — advanced)
```

---

## Step 1 — `#1 spring-boot-3-bean-config`

### What to learn
- How DI from section 1 is used to build a small feature (a calculator with pluggable `Operation`s)
- Coding to an interface (`Operation`) so Spring can inject many implementations
- `List<Operation>` injection — Spring collects *all* beans of a type

### Key concept
```
One interface (Operation) → many implementations (Addition, Subtraction, ...)
Spring injects them all as a List → you pick the right one at runtime.
This is the Strategy pattern, powered by the IoC container.
```

### Questions to answer after studying
1. How does Spring inject every `Operation` implementation into one `List<Operation>`?
2. Why code against the `Operation` interface instead of the concrete classes?
3. What would break if `Addition` was missing `@Component`?

### Practice exercise
Add a `Modulus` (`%`) operation. You should NOT have to edit the calculator class — only add a new bean. If you do, your design is correctly open/closed.

---

## Step 2 — `#2 spring-boot-3-web-app` ← CORE OF THIS SECTION

### What to learn
- The full MVC request flow (memorize this):
  ```
  HTTP Request
      ↓
  DispatcherServlet   (front controller — Spring owns this)
      ↓
  @RestController     (TodoController)
      ↓
  @Service            (TodoService — business logic)
      ↓
  Repository          (CommonRepository — data access)
      ↓
  HTTP Response
  ```
- `@PathVariable` vs `@RequestParam` vs `@RequestBody`
- Returning `ResponseEntity<T>` for full control over status + headers
- Local exception handling with a custom exception (`TodoErrorException`)

### Questions to answer after studying
1. Who creates the `DispatcherServlet`, and what is its job?
2. When do you use `@PathVariable` vs `@RequestParam`?
3. What does `ResponseEntity.status(HttpStatus.CREATED).body(...)` send to the client?
4. Why keep business logic in `TodoService` instead of the controller?

### Practice exercise
Add input validation: put `@NotBlank` on the todo title, add `@Valid` to the controller method, and return `400 Bad Request` with a clear message when validation fails. (This is the gap that `3/#3 spring-boot-3-data-jpa` fills with a global handler.)

---

## Step 3 — `#3 spring-boot-webflux` ← ADVANCED / OPTIONAL

### What to learn
- Reactive programming: `Mono<T>` (0–1 item) and `Flux<T>` (0–N items)
- Non-blocking I/O — one thread serves many requests
- Two ways to define reactive endpoints:
  - Annotated `@RestController` (`ProductController`)
  - Functional routing (`ProductRouter` + `ProductHandler`)

### Key concept
```
MVC (Step 2):   1 request = 1 thread, thread BLOCKS while waiting for DB/IO
WebFlux:        1 thread handles MANY requests, nothing blocks
                → better under high concurrency, harder to reason about
```

### Questions to answer after studying
1. What is the difference between `Mono` and `Flux`?
2. Why does blocking code (e.g. `Thread.sleep`, a JDBC call) ruin a reactive app?
3. When is WebFlux actually worth it vs. plain MVC?
4. What is the difference between the annotated and functional endpoint styles?

### Practice exercise
Add a `GET /products/stream` endpoint that returns a `Flux<Product>` as `text/event-stream` and emits one item per second. Watch them arrive live in the browser.

---

## Summary Table

| Step | Project | Core Concept | Priority |
|---|---|---|---|
| 1 | #1 bean-config | DI → real feature, Strategy pattern | ✅ |
| 2 | #2 web-app | MVC request flow, ResponseEntity | 🔥 MOST IMPORTANT |
| 3 | #3 webflux | Reactive, non-blocking (Mono/Flux) | 💡 LATER |

---

## After This Section → Move to Data Access

You can now serve HTTP requests. Next (`3. Data Access with Spring`) you learn to persist that data properly — JDBC → JPA → relationships → caching → search.