package com.interview.java8.module11;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MODULE 11 — Interview patterns & revision checklist (Java 8)
 *
 * End-to-end mini problems interviewers love:
 * - second highest / top N
 * - group + aggregate
 * - flatten + distinct
 * - Optional-safe lookup pipeline
 * - Comparator multi-field sort
 */
@Service
public class InterviewPatternsDemoService {

    record Product(String name, String category, double price) {}

    private static final List<Product> PRODUCTS = List.of(
            new Product("Phone", "Electronics", 700),
            new Product("TV", "Electronics", 1200),
            new Product("Sofa", "Home", 500),
            new Product("Lamp", "Home", 40),
            new Product("Laptop", "Electronics", 1500),
            new Product("Chair", "Home", 120)
    );

    public DemoResult topNAndSecondHighest() {
        List<Double> top3Prices = PRODUCTS.stream()
                .map(Product::price)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .toList();

        Optional<Double> second = PRODUCTS.stream()
                .map(Product::price)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();

        return DemoResult.of("11-patterns", "topn-second",
                "sorted(reverse).skip(1).findFirst() = second highest. limit(N) = top N.",
                DemoResult.map("top3Prices", top3Prices, "secondHighest", second.orElse(null)));
    }

    public DemoResult groupAvgMax() {
        Map<String, DoubleSummaryStatistics> stats = PRODUCTS.stream()
                .collect(Collectors.groupingBy(Product::category, Collectors.summarizingDouble(Product::price)));

        Map<String, Object> summary = new LinkedHashMap<>();
        stats.forEach((cat, s) -> summary.put(cat, Map.of(
                "count", s.getCount(),
                "avg", s.getAverage(),
                "max", s.getMax(),
                "sum", s.getSum()
        )));

        Map<String, Product> maxByCategory = PRODUCTS.stream()
                .collect(Collectors.toMap(
                        Product::category,
                        Function.identity(),
                        (a, b) -> a.price() >= b.price() ? a : b
                ));

        Map<String, String> maxNames = maxByCategory.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().name()));

        return DemoResult.of("11-patterns", "group-agg",
                "groupingBy + summarizing* / toMap with merge = category aggregates. Very common round-1.",
                DemoResult.map("stats", summary, "maxProductByCategory", maxNames));
    }

    public DemoResult partitionAndJoin() {
        Map<Boolean, List<String>> partitioned = PRODUCTS.stream()
                .collect(Collectors.partitioningBy(
                        p -> p.price() >= 500,
                        Collectors.mapping(Product::name, Collectors.toList())
                ));

        String expensiveCsv = PRODUCTS.stream()
                .filter(p -> p.price() >= 500)
                .map(Product::name)
                .sorted()
                .collect(Collectors.joining(" | "));

        return DemoResult.of("11-patterns", "partition-join",
                "partitioningBy for boolean splits. joining for CSV/display strings.",
                DemoResult.map("expensive", partitioned.get(true), "cheap", partitioned.get(false), "csv", expensiveCsv));
    }

    public DemoResult optionalPipeline() {
        Map<String, Product> index = PRODUCTS.stream()
                .collect(Collectors.toMap(Product::name, Function.identity()));

        String label = Optional.ofNullable(index.get("Laptop"))
                .filter(p -> p.price() > 1000)
                .map(p -> p.category() + ":" + p.name())
                .orElse("N/A");

        String missing = Optional.ofNullable(index.get("Tablet"))
                .map(Product::name)
                .orElseGet(() -> "missing-default");

        return DemoResult.of("11-patterns", "optional-pipeline",
                "ofNullable → filter → map → orElse/orElseGet = null-safe pipeline without nested ifs.",
                DemoResult.map("laptopLabel", label, "tablet", missing));
    }

    public DemoResult frequencyAndDuplicates() {
        List<String> words = List.of("java", "stream", "java", "lambda", "stream", "java");

        Map<String, Long> freq = words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        List<String> duplicates = freq.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        return DemoResult.of("11-patterns", "frequency-duplicates",
                "Word count = groupingBy + counting. Duplicates = freq > 1. Extremely common coding Q.",
                DemoResult.map("frequency", freq, "duplicates", duplicates));
    }

    public DemoResult revisionChecklist() {
        List<String> mustRevise = List.of(
                "Lambda + effectively final + vs anonymous this",
                "Predicate/Function/Consumer/Supplier",
                "Method refs (4 kinds) — high level",
                "default/static + diamond override",
                "Stream lazy/terminal; map vs flatMap; single-use",
                "groupingBy / partitioningBy / toMap(merge) / joining",
                "Optional: orElse vs orElseGet; don't get() blind",
                "LocalDate vs Instant; Period vs Duration",
                "Map.merge / computeIfAbsent; Comparator.comparing",
                "Coding: second highest, frequency, group-by dept"
        );

        return DemoResult.of("11-patterns", "checklist",
                "~3–4 YOE checklist. 30s each with example = ready.",
                DemoResult.map("checklist", mustRevise));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                topNAndSecondHighest(), groupAvgMax(), partitionAndJoin(),
                optionalPipeline(), frequencyAndDuplicates(), revisionChecklist()
        );
        return DemoResult.of("11-patterns", "all",
                "Java 8 lab complete. See INTERVIEW-QUESTIONS-3-4YOE.md",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
