package com.interview.java8.module05;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

/**
 * MODULE 05 — Streams basics (~3–4 YOE)
 *
 * Must-know: lazy intermediate vs terminal, map vs flatMap, filter/distinct/sorted,
 * findFirst vs findAny, reduce, stream is single-use
 */
@Service
public class StreamsBasicDemoService {

    private static final List<String> NAMES =
            List.of("Amy", "Bob", "Cara", "Dan", "Amy", "Eve", "Frank");

    public DemoResult pipelineLazy() {
        List<String> peekLog = new ArrayList<>();

        long count = NAMES.stream()
                .filter(n -> {
                    peekLog.add("filter:" + n);
                    return n.length() == 3;
                })
                .map(n -> {
                    peekLog.add("map:" + n);
                    return n.toUpperCase();
                })
                .limit(2) // short-circuit — not all elements processed
                .count();

        return DemoResult.of("05-streams-basic", "lazy-pipeline",
                "Intermediate ops are lazy. Nothing runs until a terminal op. limit/anyMatch short-circuit.",
                DemoResult.map("count", count, "operationsSeen", peekLog));
    }

    public DemoResult filterMapDistinctSorted() {
        List<String> result = NAMES.stream()
                .filter(n -> n.length() <= 3)
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .toList();

        return DemoResult.of("05-streams-basic", "filter-map",
                "filter keeps/removes. map 1→1 transform. distinct uses equals/hashCode. sorted needs Comparable/Comparator.",
                DemoResult.map("result", result));
    }

    public DemoResult flatMapDemo() {
        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5));
        List<Integer> flat = nested.stream()
                .flatMap(Collection::stream) // Stream&lt;List&gt; → Stream&lt;Integer&gt;
                .toList();

        List<String> words = List.of("java 8", "stream api");
        List<String> letters = words.stream()
                .flatMap(w -> Arrays.stream(w.split(" ")))
                .toList();

        return DemoResult.of("05-streams-basic", "flatmap",
                "flatMap: each element → stream, then flatten. Classic: nested lists, Optional, map values.",
                DemoResult.map("flatNumbers", flat, "words", letters));
    }

    public DemoResult matchFindReduce() {
        boolean anyA = NAMES.stream().anyMatch(n -> n.startsWith("A"));
        boolean allShort = NAMES.stream().allMatch(n -> n.length() < 10);
        boolean noneZ = NAMES.stream().noneMatch(n -> n.startsWith("Z"));

        Optional<String> first = NAMES.stream().filter(n -> n.startsWith("A")).findFirst();
        Optional<String> any = NAMES.stream().filter(n -> n.startsWith("A")).findAny();

        int sumLen = NAMES.stream().mapToInt(String::length).sum();
        Optional<String> longest = NAMES.stream()
                .reduce((a, b) -> a.length() >= b.length() ? a : b);

        return DemoResult.of("05-streams-basic", "match-find-reduce",
                "findFirst respects order; findAny may differ in parallel. reduce folds stream to one value.",
                DemoResult.map(
                        "anyA", anyA, "allShort", allShort, "noneZ", noneZ,
                        "findFirst", first.orElse(null),
                        "findAny", any.orElse(null),
                        "sumLen", sumLen,
                        "longest", longest.orElse(null)
                ));
    }

    public DemoResult primitiveStreams() {
        IntSummaryStatistics stats = IntStream.rangeClosed(1, 100)
                .filter(i -> i % 2 == 0)
                .summaryStatistics();

        return DemoResult.of("05-streams-basic", "primitive-streams",
                "mapToInt / IntStream — boxing avoid. Interview: sum/average on numbers.",
                DemoResult.map("evenCount", stats.getCount(), "evenSum", stats.getSum(), "evenAvg", stats.getAverage()));
    }

    /** High-frequency trap: stream cannot be reused after a terminal op. */
    public DemoResult streamReuse() {
        Stream<String> stream = NAMES.stream().filter(n -> n.length() <= 3);
        List<String> firstUse = stream.toList(); // terminal — stream consumed

        String error = null;
        try {
            stream.count(); // reuse → IllegalStateException
        } catch (IllegalStateException e) {
            error = e.getClass().getSimpleName() + ": " + e.getMessage();
        }

        return DemoResult.of("05-streams-basic", "stream-reuse",
                "Stream single-use. Terminal ke baad dubara use = IllegalStateException. Naya stream() banao.",
                DemoResult.map("firstUse", firstUse, "reuseError", error));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                pipelineLazy(), filterMapDistinctSorted(), flatMapDemo(),
                matchFindReduce(), primitiveStreams(), streamReuse()
        );
        return DemoResult.of("05-streams-basic", "all",
                "Next: /api/modules/06-streams-advanced",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
