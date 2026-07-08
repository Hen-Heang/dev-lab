# 🧱 OOP in a Real Project — saas-olv (Spring Boot + eGovFramework)

> **Source:** `C:\Users\user\Documents\saas-olv` — a multi-module gov-style enterprise app.
> **Goal:** See how the 4 OOP pillars are *actually* used in production code, not toy examples.
> Every snippet below is copied from the real `smp` (sample) package + `olv-core`.

---

## 📌 TL;DR

Yes, the project is fully OOP. But it's **enterprise OOP** — "anemic" data objects (fields + getters/setters, no behavior) with all the logic living in service classes. That is the *intended* Spring/eGov style, not a flaw.

| Pillar | Where it lives in saas-olv | Key file |
|--------|----------------------------|----------|
| Encapsulation | `private` fields + getters/setters in every VO | `CmmVO`, `SmpBoardInVO` |
| Inheritance | every InVO `extends CmmVO` (shared paging/search/audit) | `SmpBoardInVO` |
| Abstraction | `Service` + `@Mapper` interfaces (contracts, no logic) | `SmpBoardService` |
| Polymorphism | `@Override` impls + interface-typed DI | `SmpBoardServiceImpl` |

---

## 1. Encapsulation — hide the data

Every Value Object keeps fields `private` and exposes them only through accessors.

```java
// SmpBoardInVO.java
public class SmpBoardInVO extends CmmVO {
    private long boardSn;          // PK — hidden
    private String boardTitle;     // hidden

    public long getBoardSn() { return boardSn; }
    public void setBoardSn(long boardSn) { this.boardSn = boardSn; }

    public String getBoardTitle() { return boardTitle; }
    public void setBoardTitle(String boardTitle) { this.boardTitle = boardTitle; }
}
```

**Why it matters here:** MyBatis and Spring MVC both bind data by calling these
getters/setters via reflection. No public fields anywhere = controlled access +
framework compatibility.

**Bonus encapsulation pattern — split input vs output:**
- `XxxInVO` → request data (search, form, PK). Extends `CmmVO`.
- `XxxOutVO` → response data only. Just `implements Serializable`.

This keeps request fields from leaking into responses — a deliberate boundary.

---

## 2. Inheritance — write common fields ONCE

The single biggest practical win in this codebase. `CmmVO` is the base class
every screen's InVO inherits.

```java
// olv-core/.../cmm/CmmVO.java  (the parent)
public class CmmVO implements Serializable {
    // search
    private String searchCondition = "";
    private String searchKeyword = "";
    // paging
    private int pageIndex = 1;
    private int recordCountPerPage = 10;
    private int firstIndex = 1;
    // audit (every table has these)
    private String dataRegId;
    private String dataRegDt;
    private String dataChgId;
    private String dataChgDt;
    // ... + all getters/setters
}
```

```java
// any domain just extends it — gets ~20 fields for free
public class SmpBoardInVO extends CmmVO { ... }
```

**Why:** every screen needs paging + search + audit columns. Without inheritance
you'd copy-paste those ~20 fields into *dozens* of InVOs. Change paging once in
`CmmVO` → every screen benefits. This is DRY via inheritance.

> 💡 Compare to Spring's `@MappedSuperclass BaseEntity` pattern in the java/README.md —
> same idea, different framework.

---

## 3. Abstraction — program to a contract

Business logic and data access are declared as **interfaces** first, with the
implementation kept separate.

```java
// SmpBoardService.java — pure contract, ZERO logic
public interface SmpBoardService {
    List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception;
    int  selectListTotCnt(SmpBoardInVO inVO) throws Exception;
    SmpBoardOutVO selectDetail(SmpBoardInVO inVO) throws Exception;
    void insert(SmpBoardInVO inVO) throws Exception;
    void update(SmpBoardInVO inVO) throws Exception;
    void delete(SmpBoardInVO inVO) throws Exception;
}
```

The `@Mapper` interface is the same idea for data access — MyBatis generates the
implementation at runtime from the SQL XML:

```java
@Mapper
public interface SmpBoardMapper {
    List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception;
    void insert(SmpBoardInVO inVO) throws Exception;
    // ... matches the <select>/<insert> ids in SmpBoard_SQL.xml
}
```

**Why:** the Controller depends on the *interface* (`SmpBoardService`), never the
concrete class. You can swap the implementation (test mock, different data source)
without touching the caller.

---

## 4. Polymorphism — one type, swappable behavior

The implementation overrides the interface methods, and Spring injects it behind
the interface type.

```java
// SmpBoardServiceImpl.java
@Service("smpBoardService")
public class SmpBoardServiceImpl implements SmpBoardService {

    @Autowired
    private SmpBoardMapper smpBoardMapper;

    @Override
    public List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception {
        return smpBoardMapper.selectList(inVO);   // delegates to mapper
    }
    // ... all 6 methods @Override
}
```

In the controller the field is typed as the **interface**, but at runtime it holds
a `SmpBoardServiceImpl` (actually a Spring proxy of it):

```java
@Autowired
private SmpBoardService smpBoardService;   // interface type → impl injected at runtime
```

That's **runtime polymorphism via Dependency Injection**. Also `CmmVO` overrides
`toString()` — classic method-overriding polymorphism.

---

## 🏛️ The Layered Architecture (OOP at the macro level)

```
HTTP request
   │
   ▼
Controller (web)          @Controller   — URL mapping, redirect
   │  depends on interface
   ▼
Service (interface)       contract
   │  implemented by
   ▼
ServiceImpl               @Service      — business logic
   │  depends on interface
   ▼
Mapper (interface)        @Mapper       — data-access contract
   │  generated from
   ▼
Xxx_SQL.xml  ──────────►  PostgreSQL
```

Each arrow is "depend on an abstraction, not a concretion" — the Dependency
Inversion Principle. This is why the whole thing is testable and swappable.

---

## 🤔 Why use OOP here at all?

1. **The framework requires it.** Spring DI + MyBatis only work because you code to
   interfaces (abstraction) and they inject/generate implementations (polymorphism).
2. **Kill duplication.** `CmmVO` inheritance = common fields written once.
3. **Separate concerns.** Controller → Service → Mapper layers stay swappable & testable.
4. **Team consistency.** Every domain follows the identical 8-file pattern
   (InVO→OutVO→SQL→Mapper→Service→Impl→Controller→HTML), so learning one domain
   means you can work on any of them.

> Honest summary: OOP here is about **maintainability at scale** (many screens,
> modules, developers) — not about modeling rich object behavior. Exactly what an
> enterprise eGov project optimizes for.

---

## 🧪 Note for my own learning (vs the java/README.md)

| Concept | java/README.md (modern Spring) | saas-olv (eGov, this project) |
|---------|-------------------------------|-------------------------------|
| Boilerplate | Lombok `@Getter/@Setter` | **No Lombok** — manual getters/setters |
| Data access | `JpaRepository<T, ID>` | MyBatis `@Mapper` + SQL XML |
| Base class | `@MappedSuperclass BaseEntity` | `CmmVO` plain inheritance |
| Exceptions | `@RestControllerAdvice` | `CmmBizException` / `CmmException` + global handler |
| DTO | `record` / `@Data` | `InVO` (extends CmmVO) + `OutVO` (Serializable) |

➡️ Takeaway: the *OOP principles are identical*; only the framework conventions differ.
Learn the principle once, recognize it in any stack.
