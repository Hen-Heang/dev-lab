---
title: saas-olv — Tech Stack & Java Concepts to Learn
description: Full inventory of technologies, Java language features, Spring features, and design patterns found in the real saas-olv enterprise project — a study/review checklist
category: egov-sample
tags: [egov-sample, spring-boot, mybatis, security, design-patterns, java21]
---

# 🧭 saas-olv — Tech Stack & Java Concepts to Learn

> **Source project:** `saas-olv` (company project — READ ONLY, never edited).
> **Purpose:** a personal study map. What technologies the project uses, and which
> Java / Spring concepts I can learn from reading its code.
> Companion to [the screen-build flow guide](./README.md) and
> [OOP in saas-olv](./oop-in-saas-olv.md).

---

## 0. Big Picture — What kind of project is it?

A **multi-module enterprise web system** built on the Korean government framework
(eGovFramework) on top of Spring Boot. Four Gradle modules:

| Module | Role | Packaging | Port |
|--------|------|-----------|------|
| `olv-core` | shared library (VO, util, config, security, common services) | `jar` (no bootJar) | – |
| `olv-oper` | admin web (CRUD screens) | `war` → `adlunch.war` | 8081 |
| `olv-pfom` | user portal | `war` | 8080 |
| `olv-api` | REST API (external linkage) | `war` | – |

Key idea: **`olv-core` holds everything common; the 3 web modules depend on it.**
That's the "shared kernel" pattern — learn once, reused everywhere.

---

## 1. Technology Inventory (the "what")

### Build & tooling
- **Gradle multi-module** — `settings.gradle` (`include`), root `build.gradle` with
  `subprojects { }` to apply shared config to every module.
- **Java 21 toolchain** — `java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }`
- **Spring Boot 3.3.5** + `io.spring.dependency-management` (BOM-managed versions).
- **WAR vs JAR packaging** — core is a plain `jar`; web modules build `war` with
  `providedRuntime` Tomcat (servlet container supplied externally at deploy time).
- **Private Nexus repo** instead of Maven Central (corporate environment).

> 🎓 Concept to learn: Gradle dependency **configurations** —
> `api` vs `implementation` vs `runtimeOnly` vs `compileOnly` vs `providedRuntime`.
> See table in §5.

### Frameworks & libraries
| Area | Library |
|------|---------|
| Web MVC | Spring MVC (`spring-boot-starter-web`) |
| Template | Thymeleaf + `thymeleaf-layout-dialect` |
| Security | **Spring Security 6.x** (eGov security replaced by hand-written config) |
| DB access | **MyBatis** (`mybatis-spring-boot-starter`) + PostgreSQL |
| Validation | Bean Validation (`spring-boot-starter-validation`, `@Valid`) |
| Scheduling | Spring `@Scheduled` + Quartz starter |
| Object storage | **AWS SDK v2 — S3** (NCP Object Storage, S3-compatible) |
| Crypto | Jasypt, BouncyCastle, eGov crypto |
| Excel | Apache POI (`poi`, `poi-ooxml`) |
| OAuth (social login) | ScribeJava |
| Realtime | `spring-boot-starter-websocket` |
| i18n / dates | ICU4j |
| Testing | JUnit 5 (`spring-boot-starter-test`) |
| Gov framework | eGovFramework RTE 5.0.0 (MVC, FDL, data-access) |

---

## 2. Java Language Concepts to Learn (the gold) 🥇

These are the things worth studying *as Java*, independent of any framework.

### 2.1 Generics — bounded type parameters
`CmmInVO<T extends CmmVO>` — a generic request wrapper where `T` is constrained to
subclasses of `CmmVO`. Jackson binds the JSON `body` into the correct InVO type.
```java
public class CmmInVO<T extends CmmVO> implements Serializable {
    private T body;                       // any InVO type
    private Map<String, Object> page;
    public T getBody() { return body; }
}
// usage: @RequestBody CmmInVO<SmpBoardInVO> request
```
> 🎓 Learn: bounded generics (`<T extends X>`), why they give compile-time safety.

### 2.2 Static factory methods
Instead of `new`, expose a named creator:
```java
// CmmOutVO
public static CmmOutVO of(Object body, CmmPaginationInfo paginationInfo) {
    CmmOutVO r = new CmmOutVO();
    r.body = body;
    r.page = paginationInfo.toPageMap();
    return r;
}
```
> 🎓 Learn: the *static factory method* idiom (Effective Java Item 1) — readable
> names, can return cached/subtype instances.

### 2.3 Enums with state + behavior
`ApiResponseCode` — not just constants, but each value carries `code` + `message`,
plus a static lookup `of()` and a behavior method `isSuccess()`.
```java
public enum ApiResponseCode {
    SUCCESS("W0000", "정상 승인/취소"),
    F2001("F2001", "토큰 인증 실패"),
    UNKNOWN("UNKNOWN", "알 수 없는 응답코드");

    private final String code; private final String message;
    ApiResponseCode(String code, String message) { ... }

    public static ApiResponseCode of(String code) {   // reverse lookup
        for (ApiResponseCode r : values()) if (r.code.equals(code)) return r;
        return UNKNOWN;                                // safe default
    }
    public boolean isSuccess() { return SUCCESS.code.equals(code) || ...; }
}
```
> 🎓 Learn: enums are full classes — fields, constructors, methods. This is the
> "typesafe enum" / lookup pattern. Far better than `String` constants.

### 2.4 Custom annotations (+ reflection/AOP)
`@Token` — a marker annotation read at runtime by an interceptor.
```java
@Retention(RetentionPolicy.RUNTIME)   // must survive to runtime to be read
@Target(ElementType.METHOD)           // only valid on methods
public @interface Token {
    String apiNo() default "tokenIssue";   // annotation attribute w/ default
}
```
Used as `@Token(apiNo="...")` on a controller method; an interceptor inspects it.
> 🎓 Learn: `@Retention`, `@Target`, annotation attributes & defaults, and how
> meta-programming reads them via reflection.

### 2.5 ThreadLocal + utility-class idiom + static nested class
`SessionHolder` keeps per-request user info in a `ThreadLocal`, accessible from any
layer without passing parameters around.
```java
public final class SessionHolder {
    private SessionHolder() {}                         // ① no instances (utility class)
    private static final ThreadLocal<Context> CONTEXT = new ThreadLocal<>();

    public static class Context { ... }               // ② static nested class
    public static void init(Context c){ CONTEXT.set(c); }
    public static void clear(){ CONTEXT.remove(); }   // ③ MUST clear to avoid leaks
    public static String getUserId(){ Context c=CONTEXT.get(); return c!=null?c.getUserId():null; }
}
```
> 🎓 Learn: `ThreadLocal` (one value per thread), the private-constructor utility
> class idiom, static nested classes, and the **memory-leak risk** if you forget
> `remove()` (interceptor clears it at request end).

### 2.6 Custom exception hierarchy (checked vs unchecked)
```java
public class CmmBizException extends RuntimeException {   // unchecked
    private final String code;                            // extra data on exception
    public CmmBizException(String code, String msg){ super(msg); this.code=code; }
    public CmmBizException(String code, String msg, Throwable cause){ super(msg,cause); this.code=code; }
}
```
Two types: `CmmBizException` (business, → HTTP 400) and `CmmException` (system, → 500).
> 🎓 Learn: extending `RuntimeException`, carrying a domain error `code`, wrapping a
> `cause`, and why Spring favors *unchecked* exceptions.

### 2.7 Lambdas & functional style
The whole Spring Security config is written as nested lambdas (the new 6.x DSL),
and error handlers are lambdas implementing functional interfaces:
```java
scheduler.setErrorHandler(t -> log.error("스케줄 오류", t));
.authenticationEntryPoint((request, response, ex) -> { ... });
```
> 🎓 Learn: lambdas = inline implementations of functional (single-method) interfaces.

---

## 3. Spring / Framework Concepts to Learn

### 3.1 Dependency Injection (two styles)
- **Field injection**: `@Autowired private SmpBoardMapper mapper;`
- **Constructor injection** (preferred, testable):
  ```java
  public CmmFileS3ServiceImpl(S3Client s3, GlobalsProperties props){ ... }
  ```

### 3.2 Java-based configuration (`@Configuration` + `@Bean`)
No XML — `MyBatisConfig`, `SecurityConfig`, `SchedulingConfig`, etc. build beans in code.

### 3.3 Conditional beans 🌟
This is a standout learning point — *which* implementation loads depends on config:
```java
@Service
@ConditionalOnProperty(name="globals.file.storage-type", havingValue="s3")
public class CmmFileS3ServiceImpl implements CmmFileStorageService { ... }

// + CmmFileLocalServiceImpl with havingValue="local"
```
Also `@ConditionalOnMissingBean(UserDetailsService.class)` — only register a default
if a module hasn't provided its own.
> 🎓 Learn: `@ConditionalOnProperty`, `@ConditionalOnMissingBean` — runtime bean
> selection. This *is* the Strategy pattern, wired by Spring.

### 3.4 Global exception handling — `@ControllerAdvice`
`CmmExceptionHandler` centralizes ALL error handling. One brilliant detail:
it returns **JSON for AJAX requests** but an **HTML alert+back script for normal page
requests** — content negotiation by hand.
```java
@ControllerAdvice
public class CmmExceptionHandler {
    @ExceptionHandler(CmmBizException.class)
    public Object handle(HttpServletRequest req, CmmBizException ex){
        return shouldRespondJson(req)
            ? buildJsonError(BAD_REQUEST, ex.getCode(), ex.getMessage())
            : buildHtmlAlertAndBack(ex.getMessage(), BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) ...  // Bean Validation errors
    @ExceptionHandler(Exception.class) ...                        // catch-all
}
```
> 🎓 Learn: `@ControllerAdvice` + `@ExceptionHandler`, returning `Object`/`ResponseEntity`,
> handling validation errors (`MethodArgumentNotValidException`, `BindException`).

### 3.5 Spring Security 6.x
- `SecurityFilterChain` bean (no more `WebSecurityConfigurerAdapter`).
- **Two chains** chosen by property: `web` (form/session) vs `api` (JWT/stateless).
- CSRF, CORS, security headers, `SessionCreationPolicy.STATELESS`, custom
  `authenticationEntryPoint` / `accessDeniedHandler`.
> 🎓 Learn: the modern lambda DSL, session vs stateless auth, CSRF/CORS basics.

### 3.6 Servlet Filter & HandlerInterceptor (request pipeline)
- `XssFilter implements Filter` + `@Order(HIGHEST_PRECEDENCE)` — wraps every request.
- `AuthInterceptor implements HandlerInterceptor` — `preHandle()` session check.
- API module: `ApiAccessInterceptor`, `ApiTrafficLogInterceptor`.
> 🎓 Learn: Filter (servlet-level) vs Interceptor (Spring MVC-level), and `@Order`.

### 3.7 MyBatis configuration
`MyBatisConfig` builds `SqlSessionFactory` + `SqlSessionTemplate`, sets
`mapUnderscoreToCamelCase=true` (snake_case → camelCase auto-map), type aliases,
mapper XML locations.
> 🎓 Learn: how `@Mapper` interface + XML namespace/id wiring works under the hood.

### 3.8 Scheduling
`@EnableScheduling` + a shared `ThreadPoolTaskScheduler` bean; services use
`@Scheduled(fixedRate=…, initialDelay=…)`.

### 3.9 Externalized config — `@ConfigurationProperties`
`GlobalsProperties` binds `globals.*` from `application.yml` into typed nested objects
(`getSecurity()`, `getFile().getS3().getBucket()`).
> 🎓 Learn: type-safe config binding vs `@Value`.

---

## 4. Design Patterns spotted in the project 🧩

| Pattern | Where | Note |
|---------|-------|------|
| **Strategy** | `CmmFileStorageService` → `S3` / `Local` impl, chosen by `@ConditionalOnProperty` | swap storage without changing callers |
| **Static Factory Method** | `CmmOutVO.of(...)`, `ApiResponseCode.of(...)` | named creation |
| **Builder** | AWS SDK `PutObjectRequest.builder()...build()`, `SqlSessionFactoryBean` | fluent object construction |
| **Template Method** | MyBatis `SqlSessionTemplate`, eGov RTE base classes | framework handles boilerplate, you fill the gap |
| **Decorator / Wrapper** | `XssRequestWrapper` wraps `HttpServletRequest` | sanitize params transparently |
| **Chain of Responsibility** | Servlet Filter chain, MVC interceptor chain | each link can stop or pass |
| **DTO / VO** | `InVO` (input) vs `OutVO` (output) | separate request/response shapes |
| **Singleton** | every `@Service` / `@Component` bean | Spring-managed, one instance |
| **Facade / Layered** | Controller → Service → Mapper | each layer hides the one below (DIP) |
| **Context Object (ThreadLocal)** | `SessionHolder` | ambient per-request data |

---

## 5. Reference: Gradle dependency configurations

| Config | Meaning | Example in project |
|--------|---------|--------------------|
| `api` | compile + **leaks** to modules that depend on this one | eGov RTE in olv-core |
| `implementation` | compile, **not** exposed downstream | POI in olv-oper |
| `runtimeOnly` | only at runtime, not compile | `postgresql` driver |
| `compileOnly` | compile only, not packaged | `ksbiz.jar` (provided by Tomcat at runtime) |
| `providedRuntime` | runtime, but container provides it | `spring-boot-starter-tomcat` for WAR |
| `testImplementation` | test classpath only | `spring-boot-starter-test` |

---

## 6. My suggested study order (for practice in dev-lab)

> ✅ All ten are now **scaffolded and runnable**. Items 1–6, 9, 10 are pure-Java in
> `java-core-mastery-lab` (package `com.henheang.saasolv`). Items 7–8 are full Spring
> apps in `spring-boot-lab`. Don't touch saas-olv.

| # | Exercise | Where it's built | Pri |
|---|----------|------------------|-----|
| 1 | **Generics** — `ApiRequest<T extends BaseVO>` + static factory `ApiResponse.of` | `java-core-mastery-lab` `…/saasolv/ex01_generics` | ⚡ |
| 2 | **Enums with behavior** — `StatusCode` w/ `of()` + `isSuccess()` | `…/saasolv/ex02_enums` | ⚡ |
| 3 | **Custom exception + central handler** — biz vs system, JSON vs page | `…/saasolv/ex03_exceptions` | ⚡ |
| 4 | **Strategy + config selection** — `StorageService` local/s3 | `…/saasolv/ex04_strategy` | 🌟 |
| 5 | **Filter/Interceptor chain** — Chain of Responsibility | `…/saasolv/ex05_chain` | |
| 6 | **ThreadLocal context** — `SessionHolder`, proves the leak | `…/saasolv/ex06_threadlocal` | |
| 7 | **Spring Security 6** — dual web/api `SecurityFilterChain` by config | `spring-boot-lab` `4. Security/#3 spring-security-olv-dual-chain` | 🌟 |
| 8 | **MyBatis mapper** — `@Mapper`+XML, snake→camel, LIMIT/OFFSET (H2) | `spring-boot-lab` `3. Data Access/#12 spring-boot-mybatis-olv` | ⚡ |
| 9 | **Custom annotation + reflection** — `@RequiresRole` enforced by reflection | `…/saasolv/ex07_annotation` | |
| 10 | **Config binding** — `@ConfigProperty` fields bound via reflection | `…/saasolv/ex08_config` | |

> ✅ Priority legend: ⚡ = do first (used constantly), 🌟 = high-value/impressive concept.
> Each exercise file ends with **🔧 PRACTICE IDEAS** to extend by hand.

---

## 7. Honest takeaways

- The project is **textbook enterprise Spring**: interfaces everywhere, DI, layered,
  config-in-code, centralized cross-cutting concerns (security, XSS, exceptions, audit).
- The *richest* learning isn't the CRUD screens — it's `olv-core`: generics, conditional
  beans, the Strategy storage abstraction, custom annotation + interceptor, ThreadLocal
  context, and the dual web/api security design.
- VOs are deliberately "anemic" (data only) — this stack puts logic in services, not in
  the data objects. Normal for this style; see [oop-in-saas-olv](./oop-in-saas-olv.md).
