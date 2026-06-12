# Security with Spring Boot — Learning Path (Basic to Advanced)
> Study these projects in order. Each one builds on the previous.
> Folder: `4. Security with Spring Boot`
> Prerequisite: finish `3. Data Access with Spring` (you need JPA users/roles).

---

## Why This Section?

Security is a top interview filter for backend roles. The trap is that it feels like "magic filters." This section removes the magic: you'll start from Spring's default login, move to in-memory users, then database-backed users, and finally a full JWT setup — the pattern almost every real REST API uses.

---

## Learning Order

`#1 spring-boot-3-security` contains FOUR sub-projects — do them in this exact order, they are deliberately staged from easy to real:

```
Step 1 → #1/security-default-config    (What you get for free)
Step 2 → #1/security-inmemory-config   (Define users in code)
Step 3 → #1/security-database-config   (Users + roles from the DB) ← KEY
Step 4 → #1/rest-api                    (Securing a REST API surface)
Step 5 → #2 spring-security-jwt-authorization  (Full stateless JWT) ← THE GOAL
```

---

## Step 1 — `#1/security-default-config`

### What to learn
- Just adding `spring-boot-starter-security` locks down every endpoint
- The auto-generated password in the console, the default `/login` form

### Key concept
```
Add the security starter → everything is protected by default ("secure by default").
Spring builds a default SecurityFilterChain for you.
```

### Questions to answer
1. What happens to your endpoints the moment you add the security starter?
2. Where does the default password come from?
3. What is the `SecurityFilterChain`?

---

## Step 2 — `#1/security-inmemory-config`

### What to learn
- `InMemoryUserDetailsManager` — define users/roles in Java config
- `PasswordEncoder` (BCrypt) — never store plain-text passwords
- Restricting URLs by role with `authorizeHttpRequests`

### Questions to answer
1. Why must passwords go through a `PasswordEncoder`?
2. What is the difference between authentication and authorization?
3. How do you allow `/public/**` but protect `/admin/**`?

### Practice exercise
Add a third user with role `MANAGER` and a `/manager/**` rule that only `MANAGER` and `ADMIN` can reach.

---

## Step 3 — `#1/security-database-config` ← KEY PROJECT

### What to learn
- `UserDetailsService` backed by JPA (`UserDetailService` + `UserRepository`)
- Modeling `User`, `Role`, `UserRole` and loading authorities from the DB
- `AppConfig` wiring the `AuthenticationProvider` + `PasswordEncoder`

### Key concept
```
Real apps don't hardcode users. Spring asks YOUR UserDetailsService:
  "load the user named X" → you return username + hashed password + roles from the DB.
Spring then compares the submitted password against the stored hash.
```

### Questions to answer
1. What method must a `UserDetailsService` implement, and what does it return?
2. How are roles/authorities loaded and attached to the authenticated user?
3. Where does password comparison actually happen?

### Practice exercise
Add a registration endpoint that hashes the password with BCrypt before saving, then log in with the new user.

---

## Step 4 — `#1/rest-api`

### What to learn
- Shape of an auth request/response (`AuthenticationRequest` / `AuthenticationResponse`)
- The difference between securing a server-rendered app vs. a stateless REST API

### Questions to answer
1. Why are server-side sessions awkward for REST APIs and mobile clients?
2. What should an auth endpoint return so the client can make future calls?

---

## Step 5 — `#2 spring-security-jwt-authorization` ← THE REAL-WORLD GOAL

### What to learn
- JWT issue + verify flow (`JwtService` / `JwtServiceImpl`, `JwtConfig`)
- A custom filter chain: `JwtAuthenticationFilter` (login → issue token) and `JwtAuthenticationInternalFilter` (every request → verify token)
- `CustomUserDetail` / `CustomUserDetailService`, `CustomAuthenticationProvider`
- Role-based endpoints: `PublicController`, `UserController`, `AdminController`
- Proper error handling: `CustomAccessDeniedHandler`, custom exceptions, CORS filter

### Key concept
```
Stateless auth:
  POST /login  → verify credentials → return a signed JWT (no server session)
  Every request → send "Authorization: Bearer <jwt>"
  A filter verifies the signature + expiry → sets the SecurityContext
The server stores NOTHING between requests → scales horizontally.
```

### Questions to answer
1. What are the three parts of a JWT, and which part is signed?
2. Why is the token signed but NOT encrypted — what does that imply about its payload?
3. Where in the filter chain is the token verified, and what gets set on success?
4. What is the difference between a 401 (Unauthorized) and a 403 (Forbidden), and which handler produces which?
5. How would you handle token expiry / refresh tokens?

### Practice exercise
Add a refresh-token endpoint: issue a short-lived access token + a longer-lived refresh token, and a `POST /refresh` that returns a new access token without re-sending the password.

---

## Summary Table

| Step | Project | Core Concept | Priority |
|---|---|---|---|
| 1 | #1/security-default-config | Secure by default | ✅ |
| 2 | #1/security-inmemory-config | Users/roles in code, BCrypt | ✅ |
| 3 | #1/security-database-config | UserDetailsService from DB | 🔥 KEY |
| 4 | #1/rest-api | Stateless vs. session auth | ✅ |
| 5 | #2 jwt-authorization | Full JWT filter chain | 🔥 THE GOAL |

---

## After This Section → Move to Messaging

Your API is now authenticated. Next (`5. Messaging with Spring Boot`) you decouple services with Kafka and RabbitMQ so they talk asynchronously.