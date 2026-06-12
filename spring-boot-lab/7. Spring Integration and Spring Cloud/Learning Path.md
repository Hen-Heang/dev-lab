# Spring Integration & Spring Cloud — Learning Path (Basic to Advanced)
> Study these projects in order.
> Folder: `7. Spring Integration and Spring Cloud`
> Prerequisite: a working REST + JPA app (sections 2–3).

---

## Why This Section?

Building an app is half the job; running and observing it in production is the other half. This section covers operational concerns — health checks, metrics, distributed tracing, and graceful restart. These are exactly the "day 2" skills that make you look senior, and they're the on-ramp to the `microservices with spring boot 3` project.

---

## Learning Order

```
Step 1 → #3 spring-boot-actuator-prometheus   (Health + metrics — start here) ← FOUNDATION
Step 2 → #2 spring-cloud-sleuth-zipkin         (Distributed tracing)
Step 3 → #1 reload-spring-application           (Graceful shutdown & context restart) ← ADVANCED
```

> Do Actuator first — Sleuth/Zipkin and most cloud features build on the Actuator endpoints.

---

## Step 1 — `#3 spring-boot-actuator-prometheus` ← FOUNDATION

### What to learn
- `spring-boot-starter-actuator` exposes operational endpoints: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`
- Custom health indicator (`ExternalServiceHealthIndicator`) — report a dependency's status
- Custom business metrics (`OrderMetrics`) with Micrometer — counters/timers
- Scraping metrics into Prometheus, visualizing in Grafana

### Key concept
```
Actuator = the app describing its own health/metrics over HTTP.
Micrometer = a vendor-neutral metrics facade ("SLF4J for metrics").
Prometheus scrapes /actuator/prometheus on a schedule → Grafana draws the dashboards.
```

### Run it
- Hit `GET /actuator/health` → see UP/DOWN, including your custom indicator
- Hit `GET /actuator/prometheus` → see raw metrics text
- Place some orders → watch your custom `OrderMetrics` counter climb

### Questions to answer
1. What is the difference between a liveness and a readiness probe?
2. Why expose metrics in Prometheus format instead of inventing your own?
3. What does a custom `HealthIndicator` let you report?
4. Counter vs. Gauge vs. Timer — when do you use each?

### Practice exercise
Add a `Timer` metric around the "place order" path and view its percentiles (p95/p99) at `/actuator/metrics`.

---

## Step 2 — `#2 spring-cloud-sleuth-zipkin`

### What to learn
- Distributed tracing: every request gets a **trace id**; each hop gets a **span id**
- Automatic propagation of trace ids across service calls and into logs
- Exporting traces to **Zipkin** to see a request's full timeline
- Note: this also has a clean layered example — DTO request/response, pagination, and a proper `@ControllerAdvice` (`ControllerAdvisor`) worth studying on its own

### Key concept
```
In one service, a stack trace is enough. Across many services it isn't.
A TRACE id ties together every span (hop) of a single request, even across services.
Zipkin shows the timeline → you see WHICH service was slow.
```

### Run it
- Start Zipkin (Docker: `docker run -d -p 9411:9411 openzipkin/zipkin`)
- Call the `CourseController` endpoints
- Open `http://localhost:9411` and find your trace; note the trace id in the app logs

### Questions to answer
1. What is the difference between a trace and a span?
2. How does the trace id end up in your log lines?
3. Why is tracing essential once you have more than one service?
4. (Note) Spring Cloud Sleuth is now **Micrometer Tracing** in Boot 3 — what changed?

### Practice exercise
Make `CourseController` call a second endpoint (or a stub service) and confirm both hops share ONE trace id in Zipkin.

---

## Step 3 — `#1 reload-spring-application` ← ADVANCED / NICHE

### What to learn
- Graceful shutdown (`GracefulShutdown`) — finish in-flight requests before dying
- Restarting/refreshing the application context at runtime (`RestartController`, `spring-cloud-context`)

### Key concept
```
Graceful shutdown: on stop, stop accepting NEW requests but let in-flight ones finish
→ no dropped requests during a deploy/rollout.
Context restart: rebuild beans without a full JVM restart (used with config refresh).
```

### Questions to answer
1. Why does graceful shutdown matter during a rolling deployment?
2. What is the risk of killing a process mid-request?
3. When would you refresh the context instead of restarting the whole app?

---

## Summary Table

| Step | Project | Core Concept | Priority |
|---|---|---|---|
| 1 | #3 actuator-prometheus | Health + metrics + Grafana | 🔥 FOUNDATION |
| 2 | #2 sleuth-zipkin | Distributed tracing | ✅ |
| 3 | #1 reload-application | Graceful shutdown / restart | 💡 LATER |

---

## After This Section

You now understand observability and operations. Combine everything in the top-level **`microservices with spring boot 3`** project (Eureka discovery, API gateway, config server, circuit breaker, RabbitMQ) — and explore **`8. Spring AI`** to add an LLM-powered endpoint.
