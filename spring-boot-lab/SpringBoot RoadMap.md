# Java + Spring Boot Roadmap (2026 — Real Version)
> Last updated: 2026-04-03
> Goal: Job-ready backend developer — Cambodia 🇰🇭 & Korea 🇰🇷 market focus

---

## Priority Legend
| Label | Meaning |
|---|---|
| 🔥 MUST | Non-negotiable. Do it first. |
| ✅ IMPORTANT | Do it after MUST. High job value. |
| 💡 LATER | Learn after you get a job or feel ready. |
| ❌ SKIP (now) | Too early. Come back later. |

---

## 1. Java Fundamentals 🔥

![java_logo_icon_168609](https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/be8bc731-8025-42ee-9a58-1db5f82c71e5)

### Must Master (Job-Level)
1. OOP — encapsulation, inheritance, polymorphism, abstraction 🔥
2. Collections Framework — `List`, `Map`, `Set`, `Queue` (used every single day)
3. Exception Handling — checked vs unchecked, custom exceptions
4. Streams + Lambda (Java 8+) — filter, map, collect, reduce
5. Multithreading basics — Thread, Runnable, synchronized, ExecutorService

### Important (2026 Standard)
6. Virtual Threads (Project Loom) ✅ — modern backend concurrency, lighter than OS threads
7. Records ✅ — clean DTO replacement, immutable data carriers
8. Pattern Matching ✅ — `instanceof` pattern matching, switch expressions
9. Sealed Classes — model closed type hierarchies cleanly
10. Optional — avoid NullPointerException properly

### Still Worth Knowing
11. Generics — type-safe collections and APIs
12. File I/O — `Path`, `Files`, `BufferedReader`
13. Design Patterns — Singleton, Factory, Builder, Strategy (used in Spring internally)
14. Clean Code Basics — meaningful names, small methods, no magic numbers

### Low Priority (Skip for Now)
- ❌ Deep GC tuning (senior level only)
- ❌ Old Java APIs (pre-Java 8 patterns)
- ❌ Deep memory profiling (learn after you're employed)

**Reference:** https://docs.oracle.com/en/java/javase/index.html

---

## 2. Spring Core ✅

<img width="682" alt="Spring Framework" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/3ae8b521-bb06-4a8c-8623-bfdf40a82847">

Focus on concepts. Don't over-study — Spring Boot handles most of this for you.

1. Inversion of Control (IoC) — understand WHY it exists, not just how
2. Dependency Injection (DI) — constructor injection (preferred), field injection
3. Bean lifecycle — creation, initialization, destruction
4. Annotation-based configuration — `@Configuration`, `@ComponentScan`, `@Bean`
5. Spring MVC — `DispatcherServlet` → Controller → Service → Repository flow
6. Profiles and Conditional Beans — `@Profile`, `@Conditional`

> Skip deep AOP at first ❌ — understand it exists, learn it when you hit a real use case (logging, transactions).

**Reference:** https://docs.spring.io/spring-framework/reference/index.html

---

## 3. Spring Boot — Main Weapon 🔥

<img width="713" alt="Spring Boot" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/fbf79135-c34e-4c3c-867e-fbc5501f9339">

This is where you spend most of your time.

### Must Master
1. Project structure — layers: Controller / Service / Repository / Entity / DTO
2. REST API building — end-to-end from request to DB
3. Core annotations — `@RestController`, `@Service`, `@Repository`, `@Component`
4. Request mapping — `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
5. Global Exception Handling — `@ControllerAdvice`, `@ExceptionHandler`
6. `application.yml` — understand property binding, nested config
7. Logging with Logback — log levels, rolling file appender, MDC for trace IDs
8. Auto-configuration — understand how Spring Boot starters work
9. DevTools — hot reload during development
10. Actuator basics — `/health`, `/info`, `/metrics` endpoints

### Important (2026)
11. Observability — Micrometer + tracing (replace Sleuth in Boot 3.x) ✅
12. Config separation — `application-dev.yml` vs `application-prod.yml` ✅
13. Spring Boot 3.x migration points — Jakarta EE namespace, AOT compilation awareness
14. Bean Validation — `@Valid`, `@NotNull`, `@Size`, custom validators

**Reference:** https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/

---

## 4. Database — Upgrade This 💥 🔥

<img width="650" alt="Spring Data" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/d6bab765-56e9-44da-8e50-a7efb61b7f70">

> This section is weak in most beginner roadmaps. Companies test this heavily.

### SQL (Must Know)
1. `SELECT`, `INSERT`, `UPDATE`, `DELETE`
2. `JOIN` — INNER, LEFT, RIGHT (VERY common in interviews)
3. `GROUP BY`, `HAVING`, aggregate functions
4. `INDEX` — how they work, when to use, composite indexes
5. Subqueries and CTEs (`WITH` clause)
6. Transaction basics — `BEGIN`, `COMMIT`, `ROLLBACK`, isolation levels

### Databases
7. PostgreSQL 🔥 — standard in Korean tech companies, JSON support, strong performance
8. MySQL — still widely used, know the differences vs PostgreSQL
9. Oracle — common in large Korean enterprises and government projects ✅

### In Spring — ORM & Persistence
10. JPA basics — `@Entity`, `@Id`, `@OneToMany`, `@ManyToOne`
11. Spring Data JPA — `JpaRepository`, query methods, `@Query`
12. MyBatis 🔥 — **VERY IMPORTANT in Korea**. Many legacy and enterprise systems use it.
    - Mapper XML files
    - `@Mapper`, `@Select`, `@Insert`, `@Update`
    - Dynamic SQL with `<if>`, `<foreach>`
13. Transaction management — `@Transactional`, propagation types (REQUIRED, REQUIRES_NEW)
14. Query optimization basics — explain plan, N+1 problem, fetch strategies
15. Pagination — `Pageable`, offset vs cursor-based
16. Database migration — Flyway or Liquibase (version-control your schema)

> MyBatis vs JPA: In Korea, expect to see both in the same company. Know both.

---

## 5. Spring Security 🔥

<img width="829" alt="Spring Security" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/ba7492cc-bd44-460f-9472-6f792588f834">

### Must
1. JWT Authentication 🔥 — access token + refresh token pattern
2. Login / Signup API — password hashing (BCrypt), token issuance
3. Role-based authorization — `@PreAuthorize`, `hasRole()`, `hasAuthority()`
4. Security filter chain — `SecurityFilterChain`, `OncePerRequestFilter`
5. CORS configuration — needed for frontend integration
6. CSRF — when to disable, when to keep

### Optional (Good Bonus)
7. OAuth2 — Google / Kakao login (very common in Korean apps) ✅
8. Method-level security — `@Secured`, `@PreAuthorize`
9. Session management vs stateless (JWT)

**Reference:** https://docs.spring.io/spring-security/reference/index.html

---

## 6. Testing ✅

<img width="740" alt="Spring Boot Test" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/d6bab765-56e9-44da-8e50-a7efb61b7f70">

### Must
1. JUnit 5 — `@Test`, `@BeforeEach`, `@AfterEach`, assertions
2. Unit tests — test Service layer logic in isolation
3. Controller tests — `@WebMvcTest`, `MockMvc`, test HTTP layer

### Good to Know
4. Mockito basics — `@Mock`, `@InjectMocks`, `when().thenReturn()`
5. Integration tests — `@SpringBootTest` with test DB (H2 or Testcontainers)
6. Repository tests — `@DataJpaTest`

> TDD is a bonus skill. Focus on writing tests that actually catch bugs.

**Reference:** https://docs.spring.io/spring-framework/reference/testing.html

---

## 7. REST API Design — Very Important 🔥

<img width="903" alt="RESTful APIs" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/bbb0bed9-5f30-4ac8-8d56-6d8024b3a8e1">

> This is what companies test in technical interviews. Don't skip this.

1. REST principles — stateless, resource-based URIs, correct HTTP verbs
2. URI naming conventions — `/users/{id}/orders` not `/getUserOrders`
3. HTTP status codes — know the difference between 400, 401, 403, 404, 409, 500
4. Standardized error response format:
   ```json
   {
     "status": 400,
     "code": "INVALID_INPUT",
     "message": "Email is required",
     "timestamp": "2026-04-03T10:00:00"
   }
   ```
5. Pagination response format — `page`, `size`, `totalElements`, `totalPages`
6. API versioning strategies — URI versioning (`/api/v1/`), header versioning
7. Request validation — validate at controller, return clear error messages
8. Idempotency — understand which operations are idempotent and why
9. API documentation — OpenAPI 3.0 with SpringDoc (`springdoc-openapi`)
10. HATEOAS basics — links in responses (nice to know, not always required)

---

## 8. Build Tools + Dev Workflow 🔥

<img width="613" alt="Docker and Kubernetes" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/621580af-4893-4a91-8978-7ebcc16f2d9f">

### Must
1. Git 🔥 — commit, branch, merge, rebase, PR workflow
   - Git branching strategy: `main`, `develop`, `feature/*`, `hotfix/*`
   - Writing clear commit messages
   - Resolving merge conflicts
2. Maven or Gradle — `pom.xml` / `build.gradle`, dependency management, build lifecycle
3. Postman — test APIs manually, organize collections, use environments
4. Swagger / OpenAPI — auto-generate API docs from annotations

### Important (2026)
5. Docker ✅ — now expected in most backend roles
   - `Dockerfile` for Spring Boot app
   - `docker-compose.yml` — run app + DB together
   - Basic commands: `build`, `run`, `ps`, `logs`, `exec`
6. IntelliJ IDEA — shortcuts, debugger, database tools, HTTP client

### Bonus
7. Linux basics — `ls`, `cd`, `grep`, `tail -f` logs, `ps aux`, `kill`
8. AWS basics ✅ — EC2 (deploy), S3 (file storage), RDS (managed DB), IAM (permissions)
   - Enough to deploy your project and talk about it in interviews

---

## 9. Logging & Monitoring ✅

<img width="1027" alt="Logging and Monitoring" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/b8b6c680-ab57-4ab7-aa56-53d408424dfa">

### Must Know
1. Log levels — `TRACE` < `DEBUG` < `INFO` < `WARN` < `ERROR`
2. Logback configuration — `logback-spring.xml`, rolling file policy
3. Structured logging — JSON logs for production environments
4. MDC (Mapped Diagnostic Context) — attach `traceId`, `userId` to all log lines
5. Micrometer metrics — counters, gauges, timers
6. Spring Boot Actuator — health checks, custom health indicators

### Optional (Learn Later)
- 💡 ELK Stack (Elasticsearch + Logstash + Kibana) — centralized logging
- 💡 Prometheus + Grafana — production metrics dashboards

---

## 10. CI/CD — Minimum ✅

<img width="691" alt="CI/CD with Jenkins" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/923b3157-2893-4da0-a6cf-8d0ac3be4942">

> You don't need to be a DevOps expert. Just understand the concept and basics.

1. What is CI/CD and why it matters
2. Basic pipeline stages — build → test → deploy
3. GitHub Actions basics — trigger on push, run Maven build, run tests
4. Understand artifacts — JAR file packaging
5. Deployment to a server — copy JAR, run with `java -jar`

> 💡 Jenkins deep knowledge comes later. For now, GitHub Actions is enough.

---

## 11. Advanced Topics (After Job-Ready) 💡

![SpringBootProducerConsumer](https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/bb59f0d3-f748-4844-b916-afd065c0e280)

> Learn these after your first job. They will make much more sense with real project experience.

### Messaging
- Apache Kafka — event streaming, producer/consumer
- RabbitMQ — message queue, pub/sub patterns
- When to use async messaging vs sync REST

### Microservices
- API Gateway (Spring Cloud Gateway)
- Service Discovery (Eureka)
- Config Server
- Circuit Breaker (Resilience4j)
- Distributed Tracing (Zipkin + Micrometer)

### Reactive (WebFlux)
- Project Reactor — Mono & Flux
- Backpressure handling
- When to use reactive vs Virtual Threads

### Spring Batch
- Batch job design — Job, Step, Reader, Processor, Writer
- Scheduling with `@Scheduled`

### GraphQL
- When REST is not enough
- Spring for GraphQL

---

## 12. AI Integration (2026) 🤖 ✅

<img width="245" height="206" alt="Spring AI" src="https://github.com/user-attachments/assets/a079f460-48d3-4186-a4a7-7a5d334d6f0d">

> AI is now part of backend. You don't need to be an AI expert — just know how to use APIs.

1. OpenAI API integration — chat completions, function calling
2. Spring AI — `ChatClient`, prompt templates, model abstraction
3. Practical use cases:
   - Chat API endpoint in your backend
   - Product recommendation API
   - Document summarization
   - Smart search with embeddings
4. RAG basics — Retrieval-Augmented Generation with a vector DB
5. LLM limitations — hallucinations, token limits, cost management

> Don't go deep into model training. Focus on API consumption and integration patterns.

**Reference:** https://docs.spring.io/spring-ai/reference/index.html

---

## 13. Real Projects — Most Important 🔥🔥🔥

<img width="823" alt="Spring Boot Best Practices" src="https://github.com/bhuvnesharya/Perfect-Roadmap-To-Learn-Java-SpringBoot-In-2024/assets/23649263/9e2b52b6-46db-48c9-a21a-2bdfa3f89e54">

> Portfolio projects get you hired. Code quality > quantity.

### Project 1: User Management System
**What to build:**
- Register / Login with JWT
- CRUD for user profiles
- Role-based access (ADMIN, USER)
- Password reset flow

**Why it matters:** Every company has auth. This proves you know the full stack.

---

### Project 2: E-Commerce Backend
**What to build:**
- Product catalog (CRUD + search + pagination)
- Order management (create, update status, history)
- Mock payment flow
- Inventory tracking

**Tech stack:** Spring Boot + PostgreSQL + JPA + JWT + Docker

---

### Project 3: DRMS (Distributor-Retailer Management System) — Your Project
**What to build:**
- Distributor and Retailer registration
- Product distribution tracking
- Order placement from Retailer to Distributor
- Reports and analytics endpoints
- Multi-role access control

**Why it matters:** Real business logic. Shows you can design domain-driven APIs.

---

### Project Quality Checklist
- [ ] Clean layered architecture (Controller / Service / Repository)
- [ ] Global exception handler with proper error responses
- [ ] Input validation on all endpoints
- [ ] Swagger/OpenAPI documentation
- [ ] Logging with traceId
- [ ] Unit + integration tests
- [ ] Docker Compose to run locally
- [ ] README with API usage examples
- [ ] `.env`-based config (never hardcode secrets)

---

## 14. Job Market — Skills Demand 🇰🇭 Cambodia & 🇰🇷 Korea

### High Demand — Both Markets
| Skill | Priority |
|---|---|
| Java + Spring Boot | 🔥 Core requirement everywhere |
| REST API design | 🔥 Tested in every technical interview |
| Git | 🔥 Daily workflow, non-negotiable |
| JWT Authentication | 🔥 Every web service needs it |
| PostgreSQL / MySQL | 🔥 Standard in most companies |
| Docker (basic) | ✅ Now expected in most backend roles |
| AWS (basic) | ✅ EC2, S3, RDS — talk about it in interviews |

---

### 🇰🇭 Cambodia Market

**What companies use:**
| Skill | Notes |
|---|---|
| Java + Spring Boot | Growing fast, especially fintech and startups |
| MySQL | Most common DB in Cambodian companies |
| Laravel / PHP | Still dominant, but Java is rising |
| REST API | Standard for mobile app backends (ABA, Wing, etc.) |
| React / Vue | Full-stack knowledge is a big plus |
| Redis | Caching for fintech/banking apps |
| AWS | Widely adopted by startups and NGOs |

**Cambodia Dev Culture Notes:**
- Fintech is the hottest sector — ABA Bank, Wing, Pi Pay, ACLEDA use Java backends
- Full-stack ability (frontend + backend) is a big hiring advantage
- English proficiency matters for international companies and NGOs
- Startups often use MySQL + Spring Boot + React stack
- Mobile-first mindset — your APIs will mostly serve Android/iOS apps
- GitHub portfolio is important — most companies will ask for it
- Salaries are growing fast, especially for developers with real project experience

---

### 🇰🇷 Korea Market

**What companies use:**
| Skill | Notes |
|---|---|
| MyBatis | 🔥 Very common — many legacy and enterprise systems |
| Oracle / PostgreSQL | Enterprise (공공기관) still uses Oracle heavily |
| Spring Boot 2.x & 3.x | Both versions alive in the wild |
| Kakao / Naver OAuth | More relevant than Google OAuth here |
| Kotlin | Growing in Korean backend teams |
| Kafka | Event-driven systems in larger companies |

**Korea Dev Culture Notes:**
- Companies often use JPA and MyBatis together — be comfortable with both
- Oracle DB is standard in government and large enterprise (공공기관)
- Spring Boot 2.x is still alive in many companies — know the differences with 3.x
- Code review culture is strong — write clean, readable, documented code
- Technical interviews often include SQL query writing and system design questions
- Korean startups are more modern (PostgreSQL, Docker, AWS) vs large enterprises

---

## 15. Interview Prep 🎯

### Technical Topics to Practice
1. Explain IoC and DI with a real example
2. JWT flow — access token + refresh token lifecycle
3. N+1 problem — what it is, how to fix it (fetch join, `@EntityGraph`)
4. Transaction propagation — REQUIRED vs REQUIRES_NEW use cases
5. REST vs RPC — when would you choose each
6. Index types — B-tree, Hash — when does an index NOT help
7. Thread safety — what makes code thread-unsafe, how to fix
8. Design a URL shortener (system design basics)

### Coding Practice
- Solve LeetCode Easy/Medium — focus on arrays, strings, HashMap, two pointers
- Write SQL queries — JOIN, GROUP BY, window functions
- Implement a small Spring Boot feature from scratch (timed practice)

---

## 16. Stay Updated

- Spring Blog: https://spring.io/blog
- Baeldung: https://www.baeldung.com (best Spring tutorials)
- YouTube: Amigoscode, Daily Code Buffer, Tech World with Nana (Docker/K8s)
- Cambodian community: Cambodian Developers Community (Facebook), CamDev, DevCambodia
- Korean community: Okky.kr, inflearn.com
- Follow Spring release notes — Boot 3.x changes matter
- GitHub: star and read open-source Spring Boot projects

---

## Learning Order Recommendation

```
Phase 1 (Weeks 1-4):   Java Fundamentals → Spring Core → Spring Boot basics
Phase 2 (Weeks 5-8):   Database (SQL + JPA + MyBatis) → REST API Design
Phase 3 (Weeks 9-12):  Spring Security (JWT) → Testing → Build Tools + Docker
Phase 4 (Weeks 13-20): Build Projects 1, 2, 3 → Polish portfolio
Phase 5 (Ongoing):     AI Integration → Advanced Topics → Interview prep
```

> The goal is not to finish every section. The goal is to be hireable.
> Build projects. Push to GitHub. Apply.