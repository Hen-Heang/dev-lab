# Testing with Spring Boot — Learning Path (Basic to Advanced)
> Folder: `6. Testing with Spring Boot`
> Prerequisite: sections 2–3 (you test controllers, services, repositories).

---

## Why This Section?

Writing code that works once is easy. Proving it still works after every change is what separates a junior from a hireable backend dev — and "do you write tests?" is asked in almost every interview. This section has one well-built project (`#1 spring-boot-testing`) covering the full testing pyramid, including Testcontainers.

---

## The Testing Pyramid (the one diagram to memorize)

```
        /\        Few   — slow, full app
       /  \       Integration tests (@SpringBootTest)
      /____\
     /      \     Some  — one layer at a time
    /        \    Slice tests (@WebMvcTest, @DataJpaTest)
   /__________\
  /            \  Many  — fast, no Spring context
 /              \ Unit tests (plain JUnit + Mockito)
/________________\
```

Test the bottom heavily (fast), the middle moderately, the top sparingly (slow).

---

## Project — `#1 spring-boot-testing`

The app is a small Product CRUD (`ProductController` → `ProductServiceImpl` → `ProductRepository`). Study the tests in this order — each tests a different layer of the pyramid.

### Step 1 — Unit test the service (bottom of pyramid)
**What to learn**
- Plain JUnit 5 — `@Test`, `assertEquals`, `assertThrows`
- **Mockito** — `@Mock` the repository, `@InjectMocks` the service, stub with `when(...).thenReturn(...)`, verify with `verify(...)`
- Testing the `ResourceNotFoundException` path

**Key concept**
```
A unit test has NO Spring context. You mock the repository so the test is fast
and only exercises the SERVICE's logic — nothing touches a real DB.
```

**Questions**
1. Why mock the repository instead of using a real one in a unit test?
2. What is the difference between `when().thenReturn()` and `verify()`?
3. How do you assert that a method throws `ResourceNotFoundException`?

### Step 2 — Slice test the web layer (`@WebMvcTest`)
**What to learn**
- `@WebMvcTest` loads ONLY the controller layer (no service, no DB)
- `MockMvc` to fire fake HTTP requests: `mockMvc.perform(get("/products/1"))`
- `@MockBean` to supply a fake service
- Asserting status + JSON: `.andExpect(status().isOk())`, `jsonPath("$.name")`

**Questions**
1. What does `@WebMvcTest` load vs. `@SpringBootTest`?
2. Why use `@MockBean` for the service here?
3. How do you assert a field in the JSON response body?

### Step 3 — Slice test the data layer (`@DataJpaTest`)
**What to learn**
- `@DataJpaTest` spins up only JPA + an embedded/test DB
- Testing repository query methods against a real database engine

**Questions**
1. What does `@DataJpaTest` configure automatically?
2. Why does it roll back each test by default?

### Step 4 — Integration test with Testcontainers (top of pyramid)
**What to learn**
- `spring-boot-testcontainers` — spin up a REAL database in Docker for the test
- `@SpringBootTest` loads the whole application context
- Why this beats an in-memory H2 (tests against the SAME DB you run in prod)

**Key concept**
```
Testcontainers starts a throwaway Postgres/MySQL in Docker for the test run,
then destroys it. Your integration tests run against the real database engine
→ no "works on H2, breaks on Postgres" surprises.
```

**Questions**
1. Why is Testcontainers more trustworthy than H2 for integration tests?
2. What does `@SpringBootTest` load that the slice annotations don't?
3. What is the trade-off (speed vs. confidence) of full integration tests?

### Practice exercise
Add a `PUT /products/{id}` update endpoint, then write all four levels for it:
1. Unit test the service update logic (mock repo)
2. `@WebMvcTest` for the controller mapping + validation
3. `@DataJpaTest` proving the row actually changes
4. A Testcontainers `@SpringBootTest` hitting the real endpoint end-to-end

---

## Cheat Sheet — Which Annotation?

| You want to test... | Use | Loads |
|---|---|---|
| Service logic only | plain JUnit + Mockito | nothing (fast) |
| Controller + JSON | `@WebMvcTest` + `MockMvc` | web layer only |
| Repository queries | `@DataJpaTest` | JPA + test DB |
| Whole app end-to-end | `@SpringBootTest` (+ Testcontainers) | everything (slow) |

---

## Summary Table

| Step | Layer | Tools | Priority |
|---|---|---|---|
| 1 | Service (unit) | JUnit 5 + Mockito | 🔥 |
| 2 | Web (slice) | @WebMvcTest + MockMvc | 🔥 |
| 3 | Data (slice) | @DataJpaTest | ✅ |
| 4 | End-to-end | @SpringBootTest + Testcontainers | ✅ |

---

## After This Section → Spring Cloud & Integration

You can now prove your code works. Next (`7. Spring Integration and Spring Cloud`) you add observability (Actuator, Zipkin, Prometheus) and operational concerns.