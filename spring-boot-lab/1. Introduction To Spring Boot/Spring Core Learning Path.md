# Spring Core — Learning Path (Basic to Advanced)
> Study these projects in order. Each one builds on the previous.
> Folder: `1. Introduction To Spring Boot`

---

## Why Learn Spring Core Before Spring Boot?

Spring Boot is just Spring Core with auto-configuration on top.
If you don't understand the core, Spring Boot feels like magic — you won't know why things work or how to fix them when they break.

---

## Learning Order

```
Step 1 → #1 spring-boot-3-getting-started     (What is Spring Boot?)
Step 2 → #3 spring-boot-ioc-and-di            (How Spring manages objects)
Step 3 → 2/#1 spring-boot-3-bean-config       (How to define beans manually)
Step 4 → #4 spring-boot-config-and-profiles   (How to configure your app)
Step 5 → 2/#2 spring-boot-3-web-app           (How Spring MVC handles HTTP)
Step 6 → #5 spring-aop-basics                 (How to add cross-cutting behavior)
Step 7 → #6 spring-events-and-conditional     (How beans talk to each other)
```

---

## Step 1 — `#1 spring-boot-3-getting-started`

### What to learn
- What `@SpringBootApplication` does (it combines 3 annotations)
- How the embedded Tomcat starts
- What `CommandLineRunner` is

### Key concept
```java
@SpringBootApplication
// = @Configuration + @EnableAutoConfiguration + @ComponentScan
// Spring Boot scans your package and auto-configures everything it finds
```

### Questions to answer after studying
1. What happens when you call `SpringApplication.run()`?
2. What is auto-configuration? Name one example.
3. What does `@ComponentScan` do?

---

## Step 2 — `#3 spring-boot-ioc-and-di` ← MOST IMPORTANT

### What to learn
- IoC Container — Spring creates and owns your objects (beans)
- 3 types of injection: Constructor ✅, Setter, Field ❌
- `@Primary` vs `@Qualifier`
- Bean scopes: Singleton vs Prototype
- Bean lifecycle: `@PostConstruct`, `@PreDestroy`

### Run these endpoints and understand WHY each returns what it does
```
GET  http://localhost:8080/api/di/default-payment   → PayPal (@Primary)
GET  http://localhost:8080/api/di/order             → Stripe (@Qualifier)
GET  http://localhost:8080/api/di/scope             → 2 different instance IDs (Prototype)
```

### Questions to answer after studying
1. Why is constructor injection better than field injection?
2. You have 3 beans of the same type. How does Spring know which one to inject?
3. What is the difference between Singleton and Prototype scope?
4. When does `@PostConstruct` run? When does `@PreDestroy` run?
5. Why doesn't `@PreDestroy` fire on Prototype beans?

### Practice exercise
Add a third payment service `KhqrPaymentService` (Cambodia's QR payment).
- Name it `"khqr"`
- Make it the new `@Primary`
- Add a new endpoint `/api/di/khqr-payment` that uses `@Qualifier("khqr")`

---

## Step 3 — `2/#1 spring-boot-3-bean-config`

### What to learn
- `@Configuration` class — a factory for beans
- `@Bean` method — manually define a bean instead of `@Component`
- When to use `@Bean` vs `@Component`

### Key concept
```
Use @Component / @Service / @Repository → for YOUR classes
Use @Bean inside @Configuration        → for THIRD-PARTY classes you don't own
                                          (e.g., ObjectMapper, RestTemplate, PasswordEncoder)
```

### Questions to answer after studying
1. Can you have multiple `@Bean` methods in one `@Configuration` class?
2. What happens if two `@Bean` methods return the same type?
3. Why can't you put `@Component` on a class from an external library?

### Practice exercise
Add a `@Configuration` class that defines:
- A `ObjectMapper` bean with `SerializationFeature.INDENT_OUTPUT` enabled
- A `RestTemplate` bean

---

## Step 4 — `#4 spring-boot-config-and-profiles`

### What to learn
- `@Value("${property}")` — inject a single property
- `@ConfigurationProperties(prefix = "app")` — bind a group of properties to a class
- `@Profile("dev")` / `@Profile("prod")` — activate beans based on environment
- How to switch profiles: `--spring.profiles.active=prod`

### Run these endpoints
```
GET  http://localhost:8080/api/config/info       → config values
GET  http://localhost:8080/api/config/profiles   → which profile is active
POST http://localhost:8080/api/config/notify     → which service fires (dev vs prod)
```

### Switch profiles and compare results
```bash
# Dev profile (console notification)
java -jar app.jar --spring.profiles.active=dev

# Prod profile (email notification)
java -jar app.jar --spring.profiles.active=prod
```

### Questions to answer after studying
1. What is the difference between `@Value` and `@ConfigurationProperties`?
2. You have a `DatabaseConfig` class with 10 properties. Which approach do you use?
3. What happens if you don't set an active profile?
4. Can a bean have multiple profiles? e.g., `@Profile({"dev", "test"})`

### Practice exercise
Add a new profile `"test"` with a `MockNotificationService` that just returns `"[TEST] Message received: " + message` without logging anything. It should be active for both `"test"` and `"dev"` profiles.

---

## Step 5 — `2/#2 spring-boot-3-web-app`

### What to learn
- Spring MVC request flow:
  ```
  HTTP Request
      ↓
  DispatcherServlet  (front controller — Spring manages this)
      ↓
  @RestController    (your controller)
      ↓
  @Service           (your business logic)
      ↓
  @Repository        (your data access)
      ↓
  HTTP Response
  ```
- `@RestController` = `@Controller` + `@ResponseBody`
- `@PathVariable` vs `@RequestParam` vs `@RequestBody`
- Returning `ResponseEntity<T>` for full HTTP control

### Questions to answer after studying
1. What is `DispatcherServlet`? Who creates it?
2. What is the difference between `@Controller` and `@RestController`?
3. When do you use `@PathVariable` vs `@RequestParam`?
4. What does `ResponseEntity.created(location).build()` return to the client?

### What this project is missing (important to notice)
- No DTO layer — controller directly uses the entity
- No `@GlobalExceptionHandler` — uses local `@ExceptionHandler` only
- No input validation (`@Valid`)

These are things you will fix in Spring Boot real projects.

---

## Step 6 — `#5 spring-aop-basics` ← NEW PROJECT

### What to learn
- AOP = Aspect-Oriented Programming
- Add behavior to methods WITHOUT modifying those methods
- Real use cases: logging, timing, security checks, transaction management

### Key terms
| Term | Meaning |
|---|---|
| Aspect | The class containing the cross-cutting logic |
| Advice | What to do (`@Before`, `@After`, `@Around`) |
| Pointcut | Which methods to intercept |
| Join Point | The actual method being intercepted |

### Run these endpoints
```
GET  http://localhost:8080/api/aop/users/1         → see logging aspect fire
GET  http://localhost:8080/api/aop/users/slow       → see timing aspect fire
GET  http://localhost:8080/api/aop/orders/place     → see @Around aspect fire
```

---

## Step 7 — `#6 spring-events-and-conditional` ← NEW PROJECT

### What to learn
- Spring Events — beans communicate without direct dependency
- `@EventListener` — react to events in a decoupled way
- `@ConditionalOnProperty` — activate beans based on config values
- `@Conditional` — custom activation logic

### Key concept
```
Without Events:
  UserService → directly calls EmailService, SmsService, AuditService
  (tight coupling — UserService knows about all of them)

With Events:
  UserService → publishes UserRegisteredEvent
  EmailService, SmsService, AuditService → each listens independently
  (loose coupling — UserService knows nothing about the listeners)
```

---

## Summary Table

| Step | Project | Core Concept | Priority |
|---|---|---|---|
| 1 | #1 getting-started | What Spring Boot IS | 🔥 |
| 2 | #3 ioc-and-di | IoC, DI, Scopes, Lifecycle | 🔥 MOST IMPORTANT |
| 3 | 2/#1 bean-config | @Configuration, @Bean | ✅ |
| 4 | #4 config-profiles | @Value, @ConfigProps, @Profile | ✅ |
| 5 | 2/#2 web-app | MVC request flow | 🔥 |
| 6 | #5 aop-basics | Cross-cutting concerns | ✅ |
| 7 | #6 events-conditional | Loose coupling, Conditional beans | ✅ |

---

## After All 7 Steps → Move to Spring Boot Main Topics

Once you understand all 7 steps, these Spring Boot topics will feel natural:

- `@Transactional` — built on AOP (Step 6)
- `spring-boot-autoconfigure` — built on `@Conditional` (Step 7)
- `SecurityFilterChain` — built on the filter chain (same idea as AOP)
- `@KafkaListener` — built on the Events pattern (Step 7)