# Mentor advice — 2026-07-02 (focus correction)

> Session summary: reviewed generic "focus on backend depth" advice
> and corrected it for MY specific target (Korean-owned bank in
> Cambodia, Oracle PL/SQL core banking). Action items became
> `FOCUS.md` and `notes/feynman/TEMPLATE.md`. This file keeps the
> reasoning so future-me remembers WHY.

## The one correction to generic advice

Generic backend roadmaps put SQL at "Level 3" and Docker/AWS as
required steps. For MY target that's wrong:

- **SQL + Oracle PL/SQL is tied for #1 with Java/Spring**, not
  level 3. Korean banks (KB PRASAC, Shinhan, Woori) run core banking
  on Oracle, and much of the business logic lives in stored
  procedures, not Java. "Good at Spring, strong at Oracle" beats
  "good at Spring, okay at SQL" in this market every time.
- **Docker/AWS matter LESS for me**, not more — banks run mostly
  on-prem. Learn Docker only enough to run Oracle XE / Postgres
  locally. Linux basics matter more than both.

## Why depth beats breadth (the core argument)

Developers who learn everything (K8s, Rust, Go, AI frameworks, ...)
are still average after 5 years. Developers who learn one stack
deeply become the person everyone asks. My bottleneck is not AI,
English, Korean, React, or Docker — it is deep understanding of how
backend systems work. Once that is deep, Docker/AWS/Kafka/AI are
easy to add because I know where they fit.

## The "why layer" I must reach in Spring

Not more annotations — being able to answer:

- What happens between the HTTP request arriving and my controller
  method running? (DispatcherServlet, filters, interceptors)
- Why does `@Transactional` silently fail on a private method or
  self-invocation? (proxies: JDK dynamic vs CGLIB)
- Why does lazy loading throw outside a transaction?
- What causes N+1 and how do I see it in the SQL log?

Every one of these is a real interview question at enterprise
Java shops, and every one is answerable inside my own projects.

## Oracle practice that beats any course

Install Oracle XE locally (free), then rebuild one module of my
flagship project's data layer as PL/SQL packages: procedures,
functions, cursors, exception handling, `BULK COLLECT` / `FORALL`.

## How to take notes (what works vs what feels productive)

- **Never** copy topic lists or advice verbatim — feels productive,
  teaches nothing (this file is the one exception: it records
  decisions, not concepts).
- Write a concept note ONLY after building something, using
  `notes/feynman/TEMPLATE.md`. The two highest-value fields:
  "in my own words" and "what surprised me" — if I can't fill them,
  I read about it but didn't learn it.
- Weekly review from MEMORY first, then re-read. What I can't
  re-explain goes back on this week's list.

## Use AI as a tool, not a competitor

Generate boilerplate, review my code, explain internals, debug
stack traces, create test cases. My value = knowing whether the
AI's output is correct. That judgment comes from the depth above.

## Decisions made this session

1. One flagship project (DRMS-style); `AuthHub`, `heang-api-center`,
   `heang-dev-lab`, `spring-boot-lab`, `spring_jwt_authentication`
   are frozen — harvest code from them, don't grow them.
2. `java-core-mastery-lab` stays as the practice scratchpad.
3. Filter every new learning idea through `FOCUS.md`.
4. Immediate next step: `exercises/phase1-pass-by-value/TASK.md` —
   write `PassByValueDemo.java`, then write my first Feynman note
   from the template.
