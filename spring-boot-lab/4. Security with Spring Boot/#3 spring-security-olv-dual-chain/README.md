# #3 spring-security-olv-dual-chain

saas-olv practice — **Spring Security 6: two `SecurityFilterChain` beans selected by config.**

Mirrors `egovframework.com.cmm.config.SecurityConfig`. One application, but the
active security policy is chosen at runtime by `globals.security.mode`:

| mode | policy | unauthenticated `/secure` or `/api` request |
|------|--------|---------------------------------------------|
| `web` (default) | form login + session | 302 redirect to `/login` |
| `api` | stateless, CSRF off, CORS on | 401 JSON |

The selection is done with `@ConditionalOnProperty` on each `@Bean` — exactly the
saas-olv technique. `globals.*` is bound type-safely via `@ConfigurationProperties`
(`GlobalsProperties`).

## Users (in-memory)
| username | password | role |
|----------|----------|------|
| `user`  | `user123`  | USER |
| `admin` | `admin123` | ADMIN |

## Run

```bash
# from this folder
./mvnw spring-boot:run                 # WEB mode (default)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--globals.security.mode=api   # API mode
```

### Try it — WEB mode
```bash
curl -i localhost:8080/public/ping          # 200 (open)
curl -i localhost:8080/secure/me            # 302 -> /login
# log in via browser at http://localhost:8080/login (user / user123), then /secure/me works
```

### Try it — API mode
```bash
curl -i localhost:8080/api/public/ping                       # 200 (open)
curl -i localhost:8080/api/me                                # 401 JSON
curl -i -u user:user123 localhost:8080/api/me               # 200 (HTTP Basic)
```

> Real saas-olv API mode uses a JWT/API-key filter added via
> `http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)`.
> Here we use HTTP Basic to keep the chain runnable; the *structure* is identical.

## Test
```bash
./mvnw test    # WebModeSecurityTest: public open, secure redirects, authed OK
```

## What to learn
- `@ConditionalOnProperty` to register different beans per environment/config.
- The Spring Security 6 lambda DSL (`authorizeHttpRequests`, `sessionManagement`, …).
- Session vs **stateless** auth; custom `authenticationEntryPoint` / `accessDeniedHandler`.
- `@ConfigurationProperties` for type-safe config (`GlobalsProperties`).

🔧 **Practice ideas**
- Add a real JWT filter to the API chain (issue token at `/api/auth/login`).
- Add method security (`@EnableMethodSecurity` + `@PreAuthorize("hasRole('ADMIN')")`).
- Make the web chain AJAX-aware: return 401 JSON for `X-Requested-With` requests
  instead of redirecting (saas-olv does this).
