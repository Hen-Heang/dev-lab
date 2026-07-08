Review the current diff (`git diff`, plus `git diff --staged` if anything is staged) as a senior Spring Boot reviewer for this repo.

If arguments are given, review that instead: $ARGUMENTS

Check for, in priority order:

1. **Correctness bugs** — null handling, transaction boundaries, JPA lazy-loading traps (accessing a lazy relation outside a session), off-by-one/edge cases, incorrect JWT/token expiry or comparison logic.
2. **Security** — anything touching `security-api`'s `security/`, `controller/AuthController`, `MfaController`, password/token handling, or SQL built from user input. Flag missing authorization checks, secrets logged or hard-coded outside `application.yml` defaults, and any reintroduction of the removed OTP/cookie-OAuth2 pattern (see CLAUDE.md's "Out" list).
3. **Convention violations** per this repo's CLAUDE.md — responses not wrapped in `ApiResponse`/`ApiStatus`, exceptions caught/formatted in a controller instead of going through `GlobalExceptionHandler`, hand-written getters/builders instead of Lombok, DTO<->entity mapping done by hand instead of MapStruct, code placed outside the standard `config/controller/domain/repository/security/service/payload/exception` package layout.
4. **Duplication / simplification** — only flag if it's a clear, low-risk win. Don't propose speculative abstractions.

For each finding give: file:line, what's wrong, why it matters (concrete failure scenario), and a suggested fix. Skip style nitpicks a formatter would catch. If nothing significant is found, say so plainly instead of inventing minor issues.
