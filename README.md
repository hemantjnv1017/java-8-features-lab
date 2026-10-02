# Java 8 Features Lab

Hands-on Java 8 revision for **~3–4 years experience** interviews — same approach as `multithreading-lab`.

Port: **8081**

> Git: manage manually jab chaaho.

---

## Run

```bash
cd ~/Projects/java8-features-lab
mvn spring-boot:run
```

**Swagger UI (easiest):**  
http://localhost:8081/swagger-ui/index.html  
Shortcuts: http://localhost:8081/ or http://localhost:8081/docs

**Question bank:** [`INTERVIEW-QUESTIONS-3-4YOE.md`](INTERVIEW-QUESTIONS-3-4YOE.md)

```bash
curl http://localhost:8081/api/modules
curl http://localhost:8081/api/modules/07-optional/orelse-vs-orelseget
```

---

## Modules (~3–4 YOE focus)

| # | Id | Topics |
|---|-----|--------|
| 01 | `01-lambda` | Lambda, effectively final, vs anonymous |
| 02 | `02-functional` | Predicate/Function/Consumer/Supplier |
| 03 | `03-method-refs` | 4 kinds of method references |
| 04 | `04-interfaces` | default/static, diamond |
| 05 | `05-streams-basic` | lazy/terminal, flatMap, **stream reuse** |
| 06 | `06-streams-advanced` | groupingBy, toMap, parallel caveats |
| 07 | `07-optional` | **orElse vs orElseGet**, map/flatMap |
| 08 | `08-datetime` | LocalDate, Instant, Period/Duration |
| 09 | `09-collections` | Map.merge, Comparator, frequency |
| 10 | `10-completable` | CF chaining intro |
| 11 | `11-patterns` | topN, group, **frequency/duplicates**, checklist |
| 12 | `12-stream-quiz` | coding quiz: second highest, frequency, group by dept, top N |

---

## Revision plan

- Day 1: 01–04  
- Day 2: 05–07 (+ coding from 11 and 12)  
- Day 3: 08–10  
- Day 4: `INTERVIEW-QUESTIONS-3-4YOE.md` full pass  

Runtime is **Java 21** (Spring Boot 3); concepts are **Java 8 APIs** still asked in interviews.
