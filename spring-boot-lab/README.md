# Rean Spring Framework — Learn Spring Boot from Basic to Advanced

A hands-on lab of ~49 mini-projects that walk Spring Boot from first principles to
production topics. Each section is a self-contained step on the path to a job-ready
backend developer.

> 📺 Companion videos: [YouTube Channel](https://www.youtube.com/@thearacode/playlists)

---

## ⚙️ Getting Started

**Prerequisites**
- Java 17+ (JDK)
- Maven 3.9+ (or use the bundled `mvnw` wrapper in each project)
- Git

**Clone the repo**
```bash
git clone https://github.com/Hen-Heang/spring-boot-lab.git
cd spring-boot-lab
```

**Run any project**
```bash
cd "<section>/<#N project>"      # e.g. cd "0. Spring Framework Tips/#4 spring-boot-rating-limit"
./mvnw spring-boot:run           # Windows: mvnw.cmd spring-boot:run
```
The app starts on `http://localhost:8080` by default.

---

## 🚀 Start Here

1. Read **[`SpringBoot RoadMap.md`](./SpringBoot%20RoadMap.md)** — the big picture (what to learn, in what priority).
2. Work through the sections **in order** using the Learning Path in each one (table below).
3. Each Learning Path tells you which `#N` project to open, the key concept, questions to answer, and a practice exercise.

**Run any project:**
```bash
cd "<section>/<#N project>"
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

---

## 📚 Curriculum (study top to bottom)

| # | Section | Learning Path | You'll learn |
|---|---|---|---|
| 0 | Spring Framework Tips | [tips](./0.%20Spring%20Framework%20Tips/0.%20Spring%20Framework%20Tips.md) | Rate limiting, file upload, Excel, real-world REST API |
| 1 | Introduction To Spring Boot | [Spring Core Learning Path](./1.%20Introduction%20To%20Spring%20Boot/Spring%20Core%20Learning%20Path.md) | IoC, DI, beans, config & profiles, AOP, events |
| 2 | Web Application with Spring Boot | [Learning Path](./2.%20Web%20Application%20with%20Spring%20Boot/Learning%20Path.md) | MVC request flow, ResponseEntity, WebFlux |
| 3 | Data Access with Spring | [Learning Path](./3.%20Data%20Access%20with%20Spring/Learning%20Path.md) | JDBC → JPA → relationships → Redis cache → Elasticsearch → GraphQL → Batch |
| 4 | Security with Spring Boot | [Learning Path](./4.%20Security%20with%20Spring%20Boot/Learning%20Path.md) | Auth/authz, UserDetailsService, full JWT |
| 5 | Messaging with Spring Boot | [Learning Path](./5.%20Messaging%20with%20Spring%20Boot/Learning%20Path.md) | RabbitMQ & Kafka, async decoupling |
| 6 | Testing with Spring Boot | [Learning Path](./6.%20Testing%20with%20Spring%20Boot/Learning%20Path.md) | Unit/slice/integration, Mockito, Testcontainers |
| 7 | Spring Integration & Spring Cloud | [Learning Path](./7.%20Spring%20Integration%20and%20Spring%20Cloud/Learning%20Path.md) | Actuator, Prometheus, Zipkin tracing |
| 8 | Spring AI | [Learning Path](./8.%20Spring%20AI/Learning%20Path.md) | ChatClient, prompts, LLM endpoints |
| ★ | microservices with spring boot 3 | _(capstone)_ | Eureka, API gateway, config server, circuit breaker, RabbitMQ |

> **Suggested pace:** sections 1–3 are the foundation (spend the most time here),
> 4–6 are high job-value, 7–8 + microservices are "level up" once the basics are solid.

---

## 🧭 Conventions in this lab

- Folders are numbered (`0.` … `8.`) and projects within them are tagged `#1`, `#2`, … in study order.
- Every section has a **Learning Path** markdown with the same 4 parts:
  *learning order → key concept → questions to answer → practice exercise.*
- Most projects use **Java 17 + Spring Boot 3.3.x** (a few older ones remain — noted in the roadmap).

---

## 🔗 Reference Links

### Official
- [Spring.io](https://spring.io/projects/spring-boot)
- [Spring Framework Docs](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data Commons](https://docs.spring.io/spring-data/commons/docs/current/reference/html/)
- [Community](https://spring.io/community) · [GitHub](https://github.com/spring-projects)

### Blogs & Tutorials
- Spring Guru — [site](https://springframework.guru) · [blog](https://springframework.guru/blog/)
- Callicoder — [Actuator + Prometheus + Grafana](https://www.callicoder.com/spring-boot-actuator-metrics-monitoring-dashboard-prometheus-grafana) · [OAuth2 social login](https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-3/) · [@ConfigurationProperties](https://www.callicoder.com/spring-boot-configuration-properties-example/)
- Stackabuse — [Configuring Properties](https://stackabuse.com/spring-boot-configuring-properties/) · [Redis HashOperations CRUD](https://stackabuse.com/spring-boot-with-redis-hashoperations-crud-functionality/)
- Programmingtechie — [Microservices Tutorial](https://programmingtechie.com/2021/03/24/spring-boot-microservices-project-tutorial-part-1/) · [JUnit 5 Tutorial](https://programmingtechie.com/2020/12/26/junit-5-complete-tutorial/)
- Medium — [JPA Entity Graphs](https://medium.com/swlh/jpa-entity-graphs-with-spring-boot-30cb110ba4f8) · [Best Practices](https://medium.com/@raviyasas/spring-boot-best-practices-for-developers-3f3bdffa0090)
- [SpringBoot2 enables HTTP/2](https://www.pengwf.com/2020/04/29/other/springboot2-http2/)
- [Spring Cloud Zipkin & Sleuth](https://www.mindbowser.com/spring-cloud-zipkin-and-sleuth-using-spring-boot/)
- Mindbowser — [What is Spring Batch](https://mirbozorgi.com/en/spring-batch) · [Redis Cache with Annotations](https://www.mindbowser.com/spring-boot-with-redis-cache-using-annotation/)
- Piotr Minkowski — [Rate Limiting with Spring Cloud Gateway](https://piotrminkowski.com/2021/05/21/secure-rate-limiting-with-spring-cloud-gateway/)

---

```bash
សុភាសិតខ្មែរ តក់ៗពេញបំពង់
```
