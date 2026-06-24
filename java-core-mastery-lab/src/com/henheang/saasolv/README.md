# saas-olv — Core Java Practice Exercises

Standalone, runnable Java exercises that recreate the **most valuable patterns**
found in the real `saas-olv` enterprise project — but in *plain Java* (no Spring,
no DB), so you can run each one with a single `main()` and study it in isolation.

> Study map / theory: see
> `dev-learning-notes/notes/egov-sample/tech-stack-and-concepts.md`

## How to run

Each exercise has an `ExNN...` class with a `main()`. From an IDE just run that
class. From the command line (Java 21):

```bash
# from java-core-mastery-lab/
javac -d out/practice $(find src/com/henheang/saasolv -name "*.java")
java  -cp out/practice com.henheang.saasolv.ex01_generics.Ex01Generics
```

## The exercises

| # | Package | Concept | Mirrors in saas-olv |
|---|---------|---------|---------------------|
| 01 | `ex01_generics` | Bounded generics `<T extends BaseVO>` + static factory | `CmmInVO<T>`, `CmmOutVO.of()` |
| 02 | `ex02_enums` | Enum with state, behavior, lookup factory | `ApiResponseCode` |
| 03 | `ex03_exceptions` | Custom biz/system exceptions + central handler | `CmmBizException`, `CmmException`, `CmmExceptionHandler` |
| 04 | `ex04_strategy` | Strategy chosen by config value | `CmmFileStorageService` + `@ConditionalOnProperty` |
| 05 | `ex05_chain` | Filter/Interceptor chain (Chain of Responsibility) | `XssFilter`, `AuthInterceptor`, `LoggingInterceptor` |
| 06 | `ex06_threadlocal` | ThreadLocal request context + utility-class idiom | `SessionHolder` |
| 07 | `ex07_annotation` | Custom annotation read by reflection | `@Token` + `ApiAccessInterceptor` |
| 08 | `ex08_config` | Type-safe config binding via reflection | `GlobalsProperties` (`@ConfigurationProperties`) |
| 09 | `ex09_ulid` | Sortable unique IDs: SecureRandom + ReentrantLock + bit-packing | `IdGenService.generateUlid()` |

Each `ExNN` file ends with **🔧 PRACTICE IDEAS** — extend the code there to learn by doing.

## The Spring-specific exercises live in `spring-boot-lab`

Two items from the study plan need a real Spring/DB project, so they were built
there as runnable Maven apps:

- **Spring Security 6** — dual `SecurityFilterChain` (web vs stateless API) chosen
  by config → `spring-boot-lab/4. Security with Spring Boot/#3 spring-security-olv-dual-chain`
- **MyBatis mapper** — `@Mapper` interface + XML namespace/id, snake→camel mapping,
  LIMIT/OFFSET paging on H2 → `spring-boot-lab/3. Data Access with Spring/#12 spring-boot-mybatis-olv`

The plain-Java exercises above already teach the *underlying concepts*
(chain of responsibility for filters, strategy/DI for swappable beans,
annotations+reflection for the security/token model).
