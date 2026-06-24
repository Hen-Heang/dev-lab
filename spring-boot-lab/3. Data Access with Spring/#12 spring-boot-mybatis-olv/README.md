# #12 spring-boot-mybatis-olv

saas-olv practice — **MyBatis `@Mapper` interface + XML mapper**, the exact data-access
pattern from saas-olv, runnable on an embedded H2 (PostgreSQL-compat) database.

Mirrors the `smp` sample package:

```
BoardController  ->  BoardService  ->  BoardServiceImpl  ->  BoardMapper(@Mapper)  ->  BoardMapper.xml  ->  H2
   (web)              (interface)         (@Service)           (interface)            (SQL)            (DB)
```

## What it demonstrates
- `@Mapper` interface whose method names match `<id>`s in the XML, and whose
  **FQCN equals the XML `namespace`**.
- `map-underscore-to-camel-case` — `board_title` (DB) → `boardTitle` (Java) automatically.
- **LIMIT/OFFSET paging** driven by `BaseVO.getFirstIndex()` (`#{firstIndex}`).
- Dynamic SQL with `<if>` for keyword search.
- INSERT using a **sequence default** (`nextval('seq_board')`) like the real Postgres DDL.
- `resultMap` declaring the DB-column ↔ Java-field contract.

## Files to read (in order)
| File | Role |
|------|------|
| `board/service/BaseVO.java` | paging/search base (`getFirstIndex()` = OFFSET) |
| `board/service/BoardInVO.java` / `BoardOutVO.java` | input vs output VO |
| `board/mapper/BoardMapper.java` | `@Mapper` interface |
| `resources/mapper/BoardMapper.xml` | the SQL (namespace = interface FQCN) |
| `board/service/impl/BoardServiceImpl.java` | delegates to the mapper |
| `board/web/BoardController.java` | REST endpoints |
| `resources/schema.sql` / `data.sql` | H2 DDL + seed (5 rows, 1 inactive) |
| `resources/application.yml` | datasource + mybatis config |

## Run

```bash
# from this folder
./mvnw spring-boot:run
```

```bash
# list (active rows only, newest first, paged)
curl "localhost:8080/board/list?pageIndex=1&recordCountPerPage=2"

# keyword search
curl "localhost:8080/board/list?searchKeyword=notice"

# detail
curl localhost:8080/board/1

# create
curl -X POST localhost:8080/board \
  -H "Content-Type: application/json" \
  -d '{"boardTitle":"hello","boardCn":"body","useYn":"Y","dataRegId":"me"}'
```

Browse the DB at <http://localhost:8080/h2-console>
(JDBC URL `jdbc:h2:mem:olv`, user `sa`, no password).

## Test
```bash
./mvnw test
```
`BoardMapperTest` proves: inactive rows excluded (count=4), snake→camel mapping,
`LIMIT` paging, keyword filter (count=2), and insert round-trip.

## Why H2 in PostgreSQL mode?
saas-olv targets PostgreSQL. `MODE=PostgreSQL` lets the same SQL —
`nextval('seq_board')`, `TEXT`, `NOW()`, `LIMIT/OFFSET` — run unchanged on the
embedded H2, so the exercise needs no external database.

🔧 **Practice ideas**
- Add `@Transactional` on a service method that does two writes; force a failure
  and confirm rollback.
- Switch the PK strategy to `useGeneratedKeys` and compare with the sequence approach.
- Add a Thymeleaf list page (like `SmpBoardList.html`) instead of REST JSON.
