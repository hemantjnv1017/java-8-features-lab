package com.interview.java8.module09;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MODULE 09 — Collection & Map enhancements in Java 8
 *
 * Interview must-knows:
 * - forEach, removeIf, replaceAll, sort on List
 * - Map: getOrDefault, putIfAbsent, compute, computeIfAbsent, computeIfPresent, merge
 * - Comparator: comparing, thenComparing, reverseOrder, nullsFirst
 * - StringJoiner / String.join
 * - Arrays.parallelSort, Base64
 */
@Service
public class CollectionsEnhancementsDemoService {

    public DemoResult listHelpers() {
        List<String> list = new ArrayList<>(List.of("a", "bb", "ccc", "dd", ""));
        list.removeIf(String::isBlank);
        list.replaceAll(String::toUpperCase);
        list.sort(Comparator.comparingInt(String::length).reversed());

        List<String> visited = new ArrayList<>();
        list.forEach(visited::add);

        return DemoResult.of("09-collections", "list-helpers",
                "removeIf / replaceAll / sort / forEach — default methods on Collection/List from Java 8.",
                DemoResult.map("list", list, "visited", visited));
    }

    public DemoResult mapComputeMerge() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("amy", 10);

        int amy = scores.getOrDefault("bob", 0);
        scores.putIfAbsent("bob", 5);

        scores.compute("amy", (k, v) -> v == null ? 1 : v + 1);
        scores.computeIfAbsent("cara", k -> k.length()); // 4
        scores.computeIfPresent("bob", (k, v) -> v + 10);
        scores.merge("amy", 5, Integer::sum); // amy += 5
        scores.merge("dan", 7, Integer::sum); // insert 7

        return DemoResult.of("09-collections", "map-compute",
                "computeIfAbsent = lazy cache pattern. merge = upsert with remapping function. Extremely common in interviews.",
                DemoResult.map("bobDefaultWas", amy, "scores", scores));
    }

    public DemoResult comparatorFactory() {
        record Emp(String name, String dept, Integer salary) {}
        List<Emp> emps = new ArrayList<>(List.of(
                new Emp("Amy", "IT", 90),
                new Emp("Bob", "IT", null),
                new Emp("Cara", "HR", 90),
                new Emp("Dan", "HR", 80)
        ));

        emps.sort(Comparator
                .comparing(Emp::dept)
                .thenComparing(Emp::salary, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Emp::name));

        List<String> order = emps.stream()
                .map(e -> e.name() + "/" + e.dept() + "/" + e.salary())
                .toList();

        return DemoResult.of("09-collections", "comparator",
                "Comparator.comparing + thenComparing + nullsFirst/Last — stop writing manual compare chains.",
                DemoResult.map("sorted", order));
    }

    public DemoResult stringJoinerAndBase64() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        joiner.add("Java").add("8").add("Rocks");
        String join = String.join("-", "a", "b", "c");

        String encoded = Base64.getEncoder().encodeToString("secret".getBytes());
        String decoded = new String(Base64.getDecoder().decode(encoded));

        int[] arr = {5, 1, 4, 2, 3};
        Arrays.parallelSort(arr);

        return DemoResult.of("09-collections", "misc",
                "StringJoiner/String.join, Base64 (finally in JDK), Arrays.parallelSort — small but asked.",
                DemoResult.map(
                        "stringJoiner", joiner.toString(),
                        "stringJoin", join,
                        "base64", encoded,
                        "decoded", decoded,
                        "parallelSorted", Arrays.toString(arr)
                ));
    }

    public DemoResult frequencyCountPattern() {
        List<String> words = List.of("java", "stream", "java", "lambda", "stream", "java");
        Map<String, Long> freq = words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        Map<String, Integer> freqMerge = new HashMap<>();
        words.forEach(w -> freqMerge.merge(w, 1, Integer::sum));

        return DemoResult.of("09-collections", "frequency",
                "Word-count with merge or groupingBy(counting) — classic coding-round + Java8 combo question.",
                DemoResult.map("groupingBy", freq, "merge", freqMerge));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                listHelpers(), mapComputeMerge(), comparatorFactory(),
                stringJoinerAndBase64(), frequencyCountPattern()
        );
        return DemoResult.of("09-collections", "all",
                "Next: /api/modules/10-completable",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
