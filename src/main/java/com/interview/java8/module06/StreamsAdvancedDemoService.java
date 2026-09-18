package com.interview.java8.module06;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MODULE 06 — Streams advanced + Collectors (interview goldmine)
 *
 * Must-know Collectors:
 * toList/toSet/toMap, joining, counting, summingInt, averagingInt,
 * groupingBy, partitioningBy, mapping, collectingAndThen, summarizingInt
 */
@Service
public class StreamsAdvancedDemoService {

    record Emp(String name, String dept, int salary) {}

    private static final List<Emp> EMPS = List.of(
            new Emp("Amy", "IT", 90),
            new Emp("Bob", "HR", 70),
            new Emp("Cara", "IT", 110),
            new Emp("Dan", "Finance", 95),
            new Emp("Eve", "HR", 80),
            new Emp("Frank", "IT", 100)
    );

    public DemoResult groupingPartitioning() {
        Map<String, List<Emp>> byDept = EMPS.stream()
                .collect(Collectors.groupingBy(Emp::dept));

        Map<String, Long> countByDept = EMPS.stream()
                .collect(Collectors.groupingBy(Emp::dept, Collectors.counting()));

        Map<String, Integer> salarySum = EMPS.stream()
                .collect(Collectors.groupingBy(Emp::dept, Collectors.summingInt(Emp::salary)));

        Map<Boolean, List<Emp>> highPaid = EMPS.stream()
                .collect(Collectors.partitioningBy(e -> e.salary() >= 90));

        Map<String, List<String>> namesByDept = EMPS.stream()
                .collect(Collectors.groupingBy(Emp::dept,
                        Collectors.mapping(Emp::name, Collectors.toList())));

        return DemoResult.of("06-streams-advanced", "grouping",
                "groupingBy = classifier key. partitioningBy = always Map&lt;Boolean, List&gt;. Downstream collectors are powerful.",
                DemoResult.map(
                        "byDeptSizes", byDept.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size())),
                        "countByDept", countByDept,
                        "salarySum", salarySum,
                        "highPaidCount", highPaid.get(true).size(),
                        "lowPaidCount", highPaid.get(false).size(),
                        "namesByDept", namesByDept
                ));
    }

    public DemoResult toMapJoining() {
        Map<String, Integer> nameToSalary = EMPS.stream()
                .collect(Collectors.toMap(Emp::name, Emp::salary));

        // duplicate key handling
        Map<String, Integer> deptMax = EMPS.stream()
                .collect(Collectors.toMap(Emp::dept, Emp::salary, Integer::max));

        String csv = EMPS.stream()
                .map(Emp::name)
                .sorted()
                .collect(Collectors.joining(", ", "[", "]"));

        return DemoResult.of("06-streams-advanced", "tomap-joining",
                "toMap(key, value, mergeFn) — always know the 3-arg form for duplicate keys.",
                DemoResult.map("nameToSalary", nameToSalary, "deptMaxSalary", deptMax, "csv", csv));
    }

    public DemoResult collectingAndThen() {
        List<String> unmodifiable = EMPS.stream()
                .map(Emp::name)
                .sorted()
                .collect(Collectors.collectingAndThen(Collectors.toList(), List::copyOf));

        String topIt = EMPS.stream()
                .filter(e -> e.dept().equals("IT"))
                .max(Comparator.comparingInt(Emp::salary))
                .map(Emp::name)
                .orElse("none");

        return DemoResult.of("06-streams-advanced", "collecting-and-then",
                "collectingAndThen(downstream, finisher) — e.g. collect then wrap unmodifiable.",
                DemoResult.map("unmodifiableNames", unmodifiable, "topIt", topIt));
    }

    public DemoResult parallelCaveats() {
        int sum = EMPS.parallelStream().mapToInt(Emp::salary).sum();

        List<String> tips = List.of(
                "parallelStream uses ForkJoinPool.commonPool()",
                "Good: large CPU-bound, associative ops (sum)",
                "Bad: tiny lists, blocking I/O, ordered side-effects",
                "Avoid shared mutable state in parallel lambdas"
        );

        return DemoResult.of("06-streams-advanced", "parallel",
                "parallelStream ≠ free speedup. Measure. Prefer sequential unless proven benefit.",
                DemoResult.map("salarySumParallel", sum, "tips", tips));
    }

    public DemoResult infiniteAndShortCircuit() {
        List<Integer> first10Even = StreamSupportHelpers.firstEven(10);

        return DemoResult.of("06-streams-advanced", "infinite",
                "iterate/generate create infinite streams — MUST use limit/find*/anyMatch to terminate.",
                DemoResult.map("first10Even", first10Even));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                groupingPartitioning(), toMapJoining(), collectingAndThen(),
                parallelCaveats(), infiniteAndShortCircuit()
        );
        return DemoResult.of("06-streams-advanced", "all",
                "Next: /api/modules/07-optional",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }

    /** Small helper to keep demo readable. */
    static class StreamSupportHelpers {
        static List<Integer> firstEven(int n) {
            return java.util.stream.Stream.iterate(0, i -> i + 1)
                    .filter(i -> i % 2 == 0)
                    .limit(n)
                    .toList();
        }
    }
}
