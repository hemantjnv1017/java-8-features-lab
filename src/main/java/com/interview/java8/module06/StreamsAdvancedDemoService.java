package com.interview.java8.module06;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MODULE 06 — Collectors (~3–4 YOE goldmine)
 *
 * Must-know: groupingBy, partitioningBy, toMap(+merge), joining, parallelStream caveats
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
                "groupingBy(dept). partitioningBy(boolean). Counting/summing downstream — coding round favorite.",
                DemoResult.map(
                        "countByDept", countByDept,
                        "salarySum", salarySum,
                        "highPaidCount", highPaid.get(true).size(),
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
                "Nice-to-know: collectingAndThen. More common: max/min + orElse after filter.",
                DemoResult.map("unmodifiableNames", unmodifiable, "topIt", topIt));
    }

    public DemoResult parallelCaveats() {
        int sum = EMPS.parallelStream().mapToInt(Emp::salary).sum();
        List<String> tips = List.of(
                "Uses commonPool — shared JVM-wide",
                "OK: large CPU-bound",
                "Avoid: small lists, blocking I/O, shared mutable state"
        );
        return DemoResult.of("06-streams-advanced", "parallel",
                "parallelStream ≠ automatic faster. 3–4 YOE: know when NOT to use.",
                DemoResult.map("salarySumParallel", sum, "tips", tips));
    }

    public DemoResult infiniteAndShortCircuit() {
        List<Integer> first10Even = java.util.stream.Stream.iterate(0, i -> i + 1)
                .filter(i -> i % 2 == 0)
                .limit(10)
                .toList();

        return DemoResult.of("06-streams-advanced", "infinite",
                "iterate/generate infinite → must limit/findAny. Awareness-level Q.",
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
}
