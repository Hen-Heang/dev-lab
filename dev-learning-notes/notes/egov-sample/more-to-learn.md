---
title: saas-olv — More to Learn (Deep-Dive Topics, Round 2)
description: A second, deeper pass over the saas-olv project surfacing topics NOT covered in the OOP / tech-stack / deployment notes — frontend AJAX, POI Excel, crypto, ID generation, interceptor lifecycle, external API integration, and the testing gap
category: egov-sample
tags: [egov-sample, deep-dive, poi, crypto, ulid, interceptor, frontend, thymeleaf]
---

# 🔬 saas-olv — More to Learn (Round 2, the deeper cuts)

> I re-read the project looking specifically for things the first three notes
> *didn't* cover. These are real, useful topics found in the actual code.
> (Read-only — saas-olv was not modified.)
>
> Already covered elsewhere → don't repeat: [tech stack & concepts](./tech-stack-and-concepts.md) ·
> [OOP](./oop-in-saas-olv.md) · [deployment](./deployment.md) · [screen flow](./README.md).

---

## 0. Quick map of what's new here

| # | Topic | Where in saas-olv | Why it matters |
|---|-------|-------------------|----------------|
| 1 | Frontend: custom AJAX + paging | `static/js/com/common.js` (`CommAjax`, `commPaging`) | how every screen talks to the server |
| 2 | Thymeleaf layout + fragments | `templates/layout/*`, `templates/fragments/*` | page composition, no copy-paste |
| 3 | Excel export with Apache POI | `cmm/excel/ExcelView.java` | huge-file streaming + encryption |
| 4 | Cryptography (3 kinds) | `lnk/cmm/crypto/BizplayAes256Util`, `CryptoConfig`, security headers | protect data in transit/at rest |
| 5 | ID generation strategies | `cmm/service/IdGenService.java` | sequence / random / UUID / **ULID** |
| 6 | Interceptor lifecycle & order | `WebMvcConfig`, `ApiTrafficLogInterceptor` | cross-cutting concerns done right |
| 7 | External API integration | `olv-api` `lnk/*` package | tokens, encrypted payloads, traffic logs |
| 8 | Direct JDBC + concurrency | `IdGenService` (JDBC, `ReentrantLock`) | when *not* to use the ORM |
| 9 | The testing gap ⚠️ | (there are **0** test files) | biggest improvement opportunity |

---

## 1. Frontend architecture — `CommAjax` + `commPaging` 🌟

`common.js` is a hand-built front-end framework on top of jQuery. Worth studying as
*JavaScript patterns*, not just glue code.

```js
// Revealing Module Pattern — returns an object of public methods, hides the rest
function CommAjax() {
    var formObj = {};                 // private state (closure)
    this.setUrl = function (url) { this.url = _ctx + url; };
    this.ajax   = function () { /* builds { body, page } JSON, posts it */ };
}
```

What to learn from it:
- **Revealing module pattern / closures** — private vars (`formObj`, `fnPaging`) hidden inside the function.
- **A request/response protocol** — everything is `{ body: {...}, page: {...} }`, matching the Java `CmmInVO`/`CmmOutVO`. Front and back agreed on one shape.
- **CSRF handling** — reads `<meta name="_csrf">` and sets the header on every POST.
- **Global UX hooks** — `$(document).ajaxStart/ajaxStop` show/hide a loading spinner app-wide.
- **Centralized error handling** — one `errorFunc` maps HTTP 400/401/403/500 to alerts/redirects (mirrors the server's `CmmExceptionHandler`).
- **Prototype extension** — `Date.prototype.addDays`, `String.prototype.replaceAll` (powerful, but a known anti-pattern — note *why* it's risky).

> 🔧 Practice: build a tiny `fetch`-based `ApiClient` that always sends `{body,page}`,
> injects a CSRF header, and routes errors through one handler.

---

## 2. Thymeleaf layout + fragments

267 HTML files in oper, 62 in pfom — they don't repeat the header/menu because of:
- `layout/default.html` + the **layout dialect** (`layout:decorate`, `layout:fragment`).
- Reusable `fragments/` (header, lnb (left nav), pagination, popups) via `th:replace`/`th:insert`.
- Context-path safety: `@{/...}` and a `_ctx` meta so URLs work under `/lunch`, `/adlunch`.

> 🔧 Practice: in `spring-boot-lab`, make a `default.html` layout + a `header` fragment,
> then build two pages that decorate it. (You already have Thymeleaf projects there.)

---

## 3. Excel export with Apache POI — `ExcelView` 🌟

Extends Spring's `AbstractXlsView`. Two non-obvious, production-grade techniques:

- **`SXSSFWorkbook(100)`** — *streaming* workbook: keeps only 100 rows in memory and
  flushes the rest to disk. This is how you export 50,000+ rows without `OutOfMemoryError`.
  (Regular `XSSFWorkbook` holds everything in RAM.)
- **Password-encrypted .xlsx** — `EncryptionInfo(EncryptionMode.agile)` + `Encryptor`
  to produce a password-protected file.
- **Dynamic columns** — headers/keys/widths/aligns passed as `List`s, rows as
  `List<Map<String,Object>>`, so one view exports any query.
- Resource hygiene: `try/finally` + `workbook.dispose()`/`close()`.

> 🔧 Practice: export a `List<Map>` to `.xlsx` with `SXSSFWorkbook`; then add a password.

---

## 4. Cryptography — three different kinds in one project

| Kind | Where | Note |
|------|-------|------|
| **Symmetric AES-256** | `BizplayAes256Util` | `AES/CBC/PKCS5Padding` via `javax.crypto.Cipher`, IV = last 16 bytes of key, Base64 output. Key rotates daily (`+MMDD`). |
| **Property encryption** | `CryptoConfig` + Jasypt | encrypt DB passwords etc. in `application.yml` (`ENC(...)`) |
| **Password hashing** | `CmmPasswordEncoder` / Spring `PasswordEncoder` | one-way hash for login creds |
| **Security headers / XSS / CSRF** | `XssFilter`, `SecurityConfig` | defense in depth |

Key lesson: **encryption ≠ hashing ≠ encoding.** AES is reversible (for data you must
read back), hashing is one-way (for passwords), Base64 is just encoding (not security).

> 🔧 Practice: write an `Aes256Util` (encrypt/decrypt round-trip) — pure Java, no Spring.
> Great fit for `java-core-mastery-lab`. ⚠️ Never hardcode keys in real code.

---

## 5. ID generation strategies — `IdGenService` 🌟 (a hidden gem)

One service, four strategies — a great study of *trade-offs*:

| Strategy | How | Use when |
|----------|-----|----------|
| **sequence** | PostgreSQL `nextval('seq_…')` | ordered numeric PKs (`_sn`) |
| **prefix_random** | `"M" + 9 random digits`, retry on collision | human-ish IDs (`mngr_no`) |
| **UUID** | `UUID.randomUUID()` | global uniqueness, no coordination |
| **ULID** | timestamp + 80-bit random, **monotonic**, Crockford Base32 | sortable-by-time unique IDs |

Deeper Java concepts packed in here:
- **`SecureRandom`** (not `Math.random`) for anything security-adjacent.
- **`ReentrantLock`** to make ULID generation thread-safe + monotonic.
- **Bit manipulation** (`>>>`, `&`, `<<`) to pack timestamp+random into Base32.
- **Direct JDBC** with `try-with-resources` (`Connection`/`PreparedStatement`/`ResultSet`)
  — deliberately *bypassing* MyBatis for a simple `nextval`. Teaches when the ORM is overkill.

> 🔧 Practice: implement `generateUlid()` yourself and sort a list of them — prove they
> come out in creation order. (This single class could be a whole afternoon of learning.)

---

## 6. Interceptor lifecycle & ordering — `WebMvcConfig`

The order interceptors run is deliberate and worth internalizing:

```
SessionHolderInterceptor  → sets ThreadLocal user context   (runs FIRST)
LoggingInterceptor        → logs the request
AuthInterceptor           → session check (ONLY in web mode; api mode uses Spring Security)
```

- Registration is **conditional** (`if mode == "web"`), and static/public URLs are excluded.
- `ApiTrafficLogInterceptor` shows the **full lifecycle**: `preHandle` records the start
  time, `afterCompletion` writes the audit row — wrapped in `try-catch` so logging
  failures never break the real request. (Important production habit.)

> 🔧 Practice: extend your `java-core-mastery-lab` ex05 chain with `afterCompletion`
> running in reverse order, and a timing interceptor (start in pre, elapsed in after).

---

## 7. External API integration (the whole `olv-api` `lnk` package)

A realistic 3rd-party integration (bizplay) — a different world from CRUD screens:
- **Header VOs** — `ApiGetReqHeaderVO`, `ApiPostRspHeaderVO` model the partner's contract.
- **Token auth** — `@Token` annotation + `ApiAccessInterceptor` validate an agency token from a header.
- **Encrypted payloads** — request/response bodies AES-encrypted (see #4).
- **Traffic logging** — every call's request/response persisted to `lk_api_if_log_p`.
- **Enum response codes** — `ApiResponseCode` (already in the OOP/concepts notes).

> 🔧 Practice: build a tiny "partner API" controller that requires a token header,
> decrypts the body, and logs the call — ties together #4, #6, and the annotation exercise.

---

## 8. The testing gap ⚠️ (honest finding)

`find -path '*/src/test/*' -name '*.java'` → **0 files.** The whole project has no
automated tests. That's normal for some enterprise shops, but it's the **single biggest
thing you can practice that the project doesn't show you**:

- Unit tests (JUnit 5 — already on the classpath via `spring-boot-starter-test`).
- `@WebMvcTest` for controllers, `@MybatisTest` for mappers, `@SpringBootTest` for slices.
- The two `spring-boot-lab` exercises I built for you **do** include tests
  (`WebModeSecurityTest`, `BoardMapperTest`) — use them as your templates.

> 🔧 Practice: pick one saas-olv pattern (e.g. a service) and write the test you *wish*
> existed — in your lab, not in saas-olv.

---

## 9. Suggested deeper-dive order

1. **ULID / IdGenService** (#5) — richest pure-Java learning, self-contained. 🥇
2. **AES-256 util** (#4) — security fundamentals, pure Java.
3. **POI streaming Excel** (#3) — a skill you'll reuse constantly in enterprise work.
4. **CommAjax / front-end protocol** (#1) — understand how the UI actually works.
5. **Interceptor lifecycle** (#6) — extend the ex05 chain exercise.
6. **Writing tests** (#8) — the habit the project is missing.

> Legend: 🥇 = highest learning-per-hour. All practiceable in `java-core-mastery-lab`
> or `spring-boot-lab` — never in the company project.
