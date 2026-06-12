# Data Access with Spring — Learning Path (Basic to Advanced)
> Study these projects in order. Each one builds on the previous.
> Folder: `3. Data Access with Spring`
> Prerequisite: finish `2. Web Application with Spring Boot` first.

---

## Why This Section?

This is the biggest, most job-critical section. Almost every backend job is "take an HTTP request, read/write a database, return a response." You'll go from raw SQL (JDBC) → the JPA abstraction → modeling relationships → speeding it up with caching → search engines. Take your time here.

---

## Learning Order

```
Step 1 → #2 spring-boot-3-jdbc            (Raw JDBC — see what JPA saves you from)
Step 2 → #3 spring-boot-3-data-jpa         (JPA + global exception handling) ← KEY
Step 3 → #5 Spring Data JPA                (Repositories deep-dive + relationships) ← KEY
Step 4 → #4 spring-data-rest               (Auto-generated REST from repositories)
Step 5 → #6 Spring-Boot-Data-Redis         (Redis as a data store)
Step 6 → #8 spring-boot-data-redis-cache   (Redis as a cache — @Cacheable) ← HIGH VALUE
Step 7 → #7 spring-boot-redis-stack        (Redis OM / object mapping)
Step 8 → #9 spring-boot-elasticsearch      (Full-text search)
Step 9 → #10 spring-boot-graphql           (GraphQL instead of REST)
Step 10 → #11 spring-batch                 (Batch / ETL jobs — advanced)
```

---

## Step 1 — `#2 spring-boot-3-jdbc`

### What to learn
- `JdbcTemplate` — write SQL by hand, map rows to objects yourself
- Why this gets painful fast (boilerplate, no relationship handling)

### Key concept
```
JDBC = you write every SQL string and map every column.
This step exists so you APPRECIATE what JPA automates in Step 2.
```

### Questions to answer
1. What does `JdbcTemplate` give you over raw `Connection`/`Statement`?
2. What is a `RowMapper`?
3. Why is hand-written SQL error-prone for large apps?

---

## Step 2 — `#3 spring-boot-3-data-jpa` ← KEY PROJECT

### What to learn
- `@Entity`, `@Id`, `@GeneratedValue` — map a class to a table
- `JpaRepository<T, ID>` — CRUD for free, no SQL
- Service + `impl` split (`TodoService` / `TodoServiceImpl`)
- **Global exception handling** — `@ControllerAdvice` + `@ExceptionHandler` returning a clean `ErrorMessage` (this is the fix for the gap in `2/#2`)

### Key concept
```
You write an interface that extends JpaRepository → Spring writes the implementation.
findById, save, deleteById, findAll ... all free.
@ControllerAdvice catches exceptions app-wide → consistent error JSON.
```

### Questions to answer
1. Where does the implementation of `TodoRepository` come from?
2. What is the difference between local `@ExceptionHandler` and `@ControllerAdvice`?
3. What does `@GeneratedValue(strategy = IDENTITY)` do?
4. Why split `TodoService` (interface) from `TodoServiceImpl`?

### Practice exercise
Add a derived query method `List<Todo> findByCompletedTrue()` to the repository — no SQL. Expose it as `GET /todos/completed`.

---

## Step 3 — `#5 Spring Data JPA` ← KEY (THREE sub-projects)

This folder has three projects — do them in order:

1. **`#1 spring-data-jpa`** — derived query methods, `@Query`, paging & sorting basics.
2. **`#2 spring jpa one to one relationship`** — `@OneToOne` (Student ↔ Course), DTO mapping so you never leak entities to the API.
3. **`#3 spring-data-jpa-relationship`** — the big one: `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne` across Author/Book/Category/Photo, plus pagination (`PageClazz`).

### Key concept
```
Relationship mapping is where JPA earns its keep.
@OneToMany / @ManyToOne / @ManyToMany model your real domain.
ALWAYS return DTOs, never entities → avoids lazy-loading + infinite-recursion bugs.
```

### Questions to answer
1. What is the difference between `FetchType.LAZY` and `FetchType.EAGER`?
2. What is the N+1 query problem and how do you fix it?
3. Why map entities to DTOs before returning them from a controller?
4. Which side of a `@OneToMany` owns the foreign key?

### Practice exercise
In `#3`, add a `@ManyToMany` "tags" relationship to `Book` and return tags inside `BookResponseDto` — without causing infinite JSON recursion.

---

## Step 4 — `#4 spring-data-rest`

### What to learn
- Spring Data REST exposes a `JpaRepository` as a full REST API automatically — zero controller code.

### Questions to answer
1. What endpoints does Spring Data REST generate from a `BookRepository`?
2. When is auto-generated REST a good idea — and when is it a bad one?

---

## Step 5 — `#6 Spring-Boot-Data-Redis`

### What to learn
- Redis as a primary key-value store via `RedisTemplate` / `ReactiveRedisTemplate`
- Storing and retrieving objects (`Employee`) by key

### Questions to answer
1. What kind of data is Redis good at, vs. a relational DB?
2. What does serialization config (`RedisConfig`) control?

---

## Step 6 — `#8 spring-boot-data-redis-cache` ← HIGH JOB VALUE

### What to learn
- `@EnableCaching`, `@Cacheable`, `@CachePut`, `@CacheEvict`
- Redis as a *cache layer* in front of a JPA database (not the source of truth)

### Key concept
```
@Cacheable  → check cache first; only hit the DB on a miss, then store the result
@CacheEvict → remove stale entries when data changes
This is the #1 way real apps make slow endpoints fast.
```

### Questions to answer
1. What happens on the 1st vs. 2nd call to a `@Cacheable` method?
2. When must you use `@CacheEvict` to avoid serving stale data?
3. What is a cache key, and how do you customize it?

### Practice exercise
Add `@CacheEvict` to the update + delete methods and prove (with logs) that a stale employee is never returned after an update.

---

## Step 7 — `#7 spring-boot-redis-stack`

### What to learn
- Redis OM / object mapping over Redis Stack (richer than plain key-value)
- Consuming an external API and caching structured objects (User/Product/Address)

### Questions to answer
1. How does Redis Stack differ from plain Redis?
2. What does object mapping give you over manual serialization?

---

## Step 8 — `#9 spring-boot-elasticsearch`

### What to learn
- `@Document` + `ElasticsearchRepository` for full-text search
- Why a search engine beats `LIKE '%term%'` in SQL

### Questions to answer
1. When do you reach for Elasticsearch instead of a SQL `WHERE`?
2. What is an inverted index (one sentence)?

---

## Step 9 — `#10 spring-boot-graphql`

### What to learn
- GraphQL schema + `@QueryMapping` / `@MutationMapping`
- Client asks for exactly the fields it needs (vs. fixed REST responses)

### Questions to answer
1. What problem does GraphQL solve that REST struggles with (over/under-fetching)?
2. What is a schema resolver?

---

## Step 10 — `#11 spring-batch` ← ADVANCED

### What to learn
- `Job` → `Step` → `ItemReader` → `ItemProcessor` → `ItemWriter`
- Reading a CSV (`ProductCsvRow`), transforming, and writing to the DB in chunks

### Key concept
```
Batch = process large data sets in chunks, restartable, with read/process/write stages.
Used for imports, reports, nightly ETL — not for live HTTP requests.
```

### Questions to answer
1. What is the difference between a `Job` and a `Step`?
2. What does chunk-oriented processing (`chunk(100)`) mean?
3. How does Spring Batch resume a failed job?

---

## Summary Table

| Step | Project | Core Concept | Priority |
|---|---|---|---|
| 1 | #2 jdbc | Raw SQL / JdbcTemplate | ✅ |
| 2 | #3 data-jpa | JPA + global exception handling | 🔥 KEY |
| 3 | #5 Spring Data JPA | Repositories + relationships + DTOs | 🔥 KEY |
| 4 | #4 data-rest | Auto-generated REST | 💡 |
| 5 | #6 data-redis | Redis as store | ✅ |
| 6 | #8 redis-cache | @Cacheable caching | 🔥 HIGH VALUE |
| 7 | #7 redis-stack | Redis OM | 💡 |
| 8 | #9 elasticsearch | Full-text search | 💡 |
| 9 | #10 graphql | GraphQL API | 💡 |
| 10 | #11 spring-batch | Batch / ETL | 💡 LATER |

---

## After This Section → Move to Security

You can now store and query data. Next (`4. Security with Spring Boot`) you lock it down — who can call which endpoint, and how to issue/verify JWTs.