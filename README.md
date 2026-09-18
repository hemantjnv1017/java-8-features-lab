# Java 8 Features Lab

Interview-focused, hands-on revision of **all major Java 8 features** — same style as `multithreading-lab`.

Port: **8081** (so both labs can run together).

> Git: not managed here — init/commit/push jab mann kare, manually.

---

## Run

```bash
cd ~/Projects/java8-features-lab
mvn spring-boot:run
```

Catalog: http://localhost:8081/api/modules

```bash
curl http://localhost:8081/api/modules/01-lambda
curl http://localhost:8081/api/modules/07-optional/orelse-vs-orelseget
```

---

## Modules (basic → advanced)

| # | Id | Topics |
|---|-----|--------|
| 01 | `01-lambda` | Lambda syntax, effectively final, vs anonymous class |
| 02 | `02-functional` | Predicate / Function / Consumer / Supplier + composition |
| 03 | `03-method-refs` | 4 kinds of method references |
| 04 | `04-interfaces` | default & static methods, diamond problem |
| 05 | `05-streams-basic` | filter/map/flatMap, match/find/reduce, IntStream |
| 06 | `06-streams-advanced` | groupingBy, partitioningBy, toMap, joining, parallel |
| 07 | `07-optional` | of/ofNullable, orElse vs orElseGet, map/flatMap |
| 08 | `08-datetime` | LocalDate/Time, ZonedDateTime, Instant, Period/Duration |
| 09 | `09-collections` | Map compute/merge, Comparator, Base64, frequency count |
| 10 | `10-completable` | CompletableFuture (Java 8 intro) |
| 11 | `11-patterns` | Coding patterns + full revision checklist |

Each JSON response has an `interviewTip` — bol ke explain karo.

---

## Revision plan

- Day 1: 01–04  
- Day 2: 05–07  
- Day 3: 08–10  
- Day 4: 11 + re-run weak modules  

Code: `src/main/java/com/interview/java8/moduleXX/`

---

## Note on JDK

Project runs on **Java 21** (Spring Boot 3), but demos teach **Java 8 APIs** still asked in interviews. Runtime naya hai; concepts Java 8 ke hain.
