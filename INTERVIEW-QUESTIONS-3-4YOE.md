# Java 8 Interview Questions — 3–4 Years Experience

Curated from common mid-level Java interview themes (LinkedIn / coding rounds / backend screens).  
Lab demos map to each question. Answer aloud in **30–60 seconds**.

**Swagger:** http://localhost:8081/swagger-ui/index.html  
**Port:** 8081

---

## A. Lambda & functional interfaces (almost every round)

| # | Question | Lab demo |
|---|----------|----------|
| 1 | What is a lambda? When can you pass it? | `/api/modules/01-lambda/syntax` |
| 2 | What is effectively final? | `/api/modules/01-lambda/effectively-final` |
| 3 | Lambda vs anonymous inner class (`this`)? | `/api/modules/01-lambda/vs-anonymous` |
| 4 | What is a functional interface / SAM? | `/api/modules/02-functional/custom-fi` |
| 5 | Predicate / Function / Consumer / Supplier? | `/api/modules/02-functional/core-four` |
| 6 | `andThen` vs `compose`? | `/api/modules/02-functional/composition` |
| 7 | Four kinds of method references? | `/api/modules/03-method-refs/four-kinds` |

---

## B. Interface defaults (Java 8 language)

| # | Question | Lab demo |
|---|----------|----------|
| 8 | Why default methods on interfaces? | `/api/modules/04-interfaces/default-static` |
| 9 | Diamond problem with two defaults — fix? | `/api/modules/04-interfaces/diamond` |
| 10 | Default method vs abstract class (short)? | `/api/modules/04-interfaces/vs-abstract` |

---

## C. Streams — theory (high frequency)

| # | Question | Lab demo |
|---|----------|----------|
| 11 | Intermediate vs terminal? Lazy evaluation? | `/api/modules/05-streams-basic/lazy-pipeline` |
| 12 | `map` vs `flatMap`? | `/api/modules/05-streams-basic/flatmap` |
| 13 | `findFirst` vs `findAny`? | `/api/modules/05-streams-basic/match-find-reduce` |
| 14 | Can you reuse a Stream? | `/api/modules/05-streams-basic/stream-reuse` |
| 15 | When NOT to use `parallelStream`? | `/api/modules/06-streams-advanced/parallel` |
| 16 | Infinite stream — how terminate? | `/api/modules/06-streams-advanced/infinite` |

---

## D. Streams — Collectors / coding (must practice)

| # | Question | Lab demo |
|---|----------|----------|
| 17 | `groupingBy` / `partitioningBy`? | `/api/modules/06-streams-advanced/grouping` |
| 18 | `toMap` duplicate key — 3-arg merge? | `/api/modules/06-streams-advanced/tomap-joining` |
| 19 | `Collectors.joining`? | same |
| 20 | Second highest / top N? | `/api/modules/11-patterns/topn-second` |
| 21 | Group by category + avg/max? | `/api/modules/11-patterns/group-agg` |
| 22 | Frequency count / find duplicates? | `/api/modules/11-patterns/frequency-duplicates` |
| 23 | Word frequency with `Map.merge`? | `/api/modules/09-collections/frequency` |

---

## E. Optional (traps loved by interviewers)

| # | Question | Lab demo |
|---|----------|----------|
| 24 | `of` vs `ofNullable`? | `/api/modules/07-optional/creation` |
| 25 | **`orElse` vs `orElseGet`?** (eager vs lazy) | `/api/modules/07-optional/orelse-vs-orelseget` |
| 26 | `map` vs `flatMap` on Optional? | `/api/modules/07-optional/map-flatmap` |
| 27 | Optional anti-patterns (field/param/`get()`)? | `/api/modules/07-optional/anti-patterns` |

---

## F. Date/Time API

| # | Question | Lab demo |
|---|----------|----------|
| 28 | Why replace `Date`/`Calendar`? | `/api/modules/08-datetime/legacy` |
| 29 | `LocalDate` vs `Instant` / `ZonedDateTime`? | local-types + zones-instant |
| 30 | `Period` vs `Duration`? | `/api/modules/08-datetime/period-duration` |
| 31 | `DateTimeFormatter` thread-safe? | `/api/modules/08-datetime/format-parse` |

---

## G. Collections / Comparator (Java 8 additions)

| # | Question | Lab demo |
|---|----------|----------|
| 32 | `Map.computeIfAbsent` / `merge`? | `/api/modules/09-collections/map-compute` |
| 33 | `Comparator.comparing` + `thenComparing`? | `/api/modules/09-collections/comparator` |
| 34 | `removeIf` / `replaceAll` / `List.sort`? | `/api/modules/09-collections/list-helpers` |

---

## H. CompletableFuture (Java 8 intro — overlap with MT lab)

| # | Question | Lab demo |
|---|----------|----------|
| 35 | `Future` vs `CompletableFuture`? | module 10 tip |
| 36 | `thenApply` vs `thenCompose`? | `/api/modules/10-completable/chain` |
| 37 | `thenCombine` / `allOf`? | `/api/modules/10-completable/combine` |
| 38 | `exceptionally`? Custom executor why? | errors + executor demos |

---

## Quick verbal cheats

1. **Functional interface** = exactly one abstract method (SAM).  
2. **Effectively final** = local var not reassigned after init.  
3. **Lazy stream** = intermediate ops run only after terminal.  
4. **flatMap** = map to stream + flatten.  
5. **orElse** always evaluates default; **orElseGet** only if empty.  
6. **toMap** duplicates → use 3-arg merge (`Integer::max`).  
7. **Stream reuse** → `IllegalStateException`.  
8. **parallelStream** → commonPool; avoid for tiny/IO work.  
9. **Period** = years/months/days; **Duration** = time-based.  
10. **Second highest** → `sorted(reverse).distinct().skip(1).findFirst()`.

---

## How to use

1. Day plan: A+B → C+D → E+F → G+H + coding from 11-patterns.  
2. Hit Swagger demo → close notes → answer in 1 minute.  
3. Weak topics → re-run endpoint + tweak `*DemoService.java`.

Depth: **3–4 YOE Java backend** — practical APIs + coding patterns, not Metaspace/Nashorn deep dives.
