# #4 — Spring Boot Rate Limiting (Bucket4j)

A minimal demo of API rate limiting with the **token bucket** algorithm using
[Bucket4j](https://github.com/bucket4j/bucket4j). The `GET /api/posts` endpoint
allows a limited number of calls per minute; extra calls get `429 Too Many Requests`.

## Concept

A *bucket* holds tokens. Each request consumes one token. Tokens refill on a
fixed interval. When the bucket is empty, requests are rejected until it refills.

| Plan (`user.type`) | Limit          |
|--------------------|----------------|
| `GOLD`             | 5 requests/min |
| anything else      | 2 requests/min |

## Project layout

```
src/main/java/com/learn
├─ SpringBootRatingLimitApplication.java  # entry point + seeds 2 demo posts
├─ controller/PostController.java         # GET /api/posts, applies the bucket
├─ service/PostService.java               # reads posts from the DB
├─ service/RatingLimitService.java        # builds the Bucket4j bucket per plan
├─ model/Post.java                        # JPA entity (table tbl_post)
└─ repository/PostRepository.java         # Spring Data JPA repository
```

## Run it

```bash
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

Then hit the endpoint repeatedly (use `requests.http`, or curl):

```bash
curl -i http://localhost:8080/api/posts
```

- `200 OK` while tokens remain — header `X-Rate-Limit-Remaining` shows how many are left.
- `429 Too Many Requests` once empty — header `X-Rate-Limit-Retry-After-Seconds` shows the wait.

View the seeded data at the H2 console: <http://localhost:8080/h2-console>
(JDBC URL `jdbc:h2:mem:postsdb`, user `sa`, no password).

## Test it

```bash
./mvnw test
```

`PostControllerRateLimitTest` proves the limit: with `GOLD` the first 5 calls
return 200 and the 6th returns 429.

## Practice ideas

1. Change `user.type` to `SILVER` (or remove it) and watch the limit drop to 2/min.
2. Make the limit **per user** instead of global — key the bucket by an `X-User-Id`
   header in a `Map<String, Bucket>` so different callers get separate buckets.
3. Move the rate-limit check into a Spring `HandlerInterceptor` or filter so it
   applies to every endpoint, not just `PostController`.
4. Return a JSON error body on 429 instead of an empty response.
