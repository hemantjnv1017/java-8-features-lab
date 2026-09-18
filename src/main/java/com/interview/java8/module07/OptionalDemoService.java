package com.interview.java8.module07;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MODULE 07 — Optional&lt;T&gt;
 *
 * Interview must-knows:
 * - Optional is a VALUE container for "maybe null" return types — not a field/param replacement
 * - of / ofNullable / empty
 * - isPresent / ifPresent / ifPresentOrElse (Java 9+)
 * - orElse / orElseGet / orElseThrow
 * - map / flatMap / filter
 * - NEVER Optional.get() without check; prefer orElseThrow
 */
@Service
public class OptionalDemoService {

    public DemoResult creationAndBasic() {
        Optional<String> a = Optional.of("Java");
        Optional<String> b = Optional.ofNullable(null);
        Optional<String> c = Optional.empty();

        return DemoResult.of("07-optional", "creation",
                "of(null) → NPE. ofNullable(null) → empty. Use Optional mainly as return type.",
                DemoResult.map(
                        "of", a.orElse(null),
                        "ofNullableIsEmpty", b.isEmpty(),
                        "empty", c.isPresent()
                ));
    }

    public DemoResult orElseVsOrElseGet() {
        List<String> logs = new ArrayList<>();

        String x = Optional.of("present")
                .orElse(expensive(logs, "orElse-on-present")); // STILL evaluates expensive!

        String y = Optional.of("present")
                .orElseGet(() -> expensive(logs, "orElseGet-on-present")); // NOT evaluated

        String z = Optional.<String>empty()
                .orElseGet(() -> expensive(logs, "orElseGet-on-empty"));

        return DemoResult.of("07-optional", "orelse-vs-orelseget",
                "orElse(value) always evaluates value. orElseGet(supplier) only if empty. Prefer orElseGet for costly defaults.",
                DemoResult.map("x", x, "y", y, "z", z, "logs", logs));
    }

    public DemoResult mapFlatMapFilter() {
        Optional<String> name = Optional.of("  hemant  ");
        Optional<String> trimmedUpper = name
                .map(String::trim)
                .filter(s -> s.length() > 3)
                .map(String::toUpperCase);

        Optional<Optional<String>> nested = Optional.of("id-1").map(this::findLabelNested);
        Optional<String> flat = Optional.of("id-1").flatMap(this::findLabel); // flattens

        return DemoResult.of("07-optional", "map-flatmap",
                "map: T→U. flatMap: T→Optional&lt;U&gt; (avoids Optional&lt;Optional&lt;U&gt;&gt;). filter keeps/empties.",
                DemoResult.map(
                        "trimmedUpper", trimmedUpper.orElse(null),
                        "nestedClass", nested.getClass().getSimpleName(),
                        "flat", flat.orElse(null)
                ));
    }

    public DemoResult antiPatterns() {
        List<String> tips = List.of(
                "DON'T use Optional as method parameter",
                "DON'T use Optional as field type (serialization/memory)",
                "DON'T call get() without isPresent — use orElseThrow()",
                "DON'T Optional.ofNullable(x).orElse(null) just to unwrap — pointless",
                "DO return Optional from find*/lookup methods"
        );

        String safe = findUser("missing").orElse("guest");
        String thrownMsg;
        try {
            findUser("missing").orElseThrow(() -> new NoSuchElementException("user not found"));
            thrownMsg = "no throw";
        } catch (NoSuchElementException e) {
            thrownMsg = e.getMessage();
        }

        return DemoResult.of("07-optional", "anti-patterns",
                "Optional replaces returning null from lookups — not a replacement for all null checks.",
                DemoResult.map("tips", tips, "safe", safe, "orElseThrow", thrownMsg));
    }

    public DemoResult withStreams() {
        List<Optional<String>> mixed = List.of(
                Optional.of("a"), Optional.empty(), Optional.of("b")
        );

        // Java 8 style flatten
        List<String> java8 = mixed.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // cleaner: flatMap(Optional::stream) is Java 9+
        List<String> present = mixed.stream()
                .flatMap(Optional::stream)
                .toList();

        return DemoResult.of("07-optional", "with-streams",
                "Flatten Optional in streams: Java8 filter+get; Java9+ flatMap(Optional::stream).",
                DemoResult.map("java8Style", java8, "java9Style", present));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                creationAndBasic(), orElseVsOrElseGet(), mapFlatMapFilter(),
                antiPatterns(), withStreams()
        );
        return DemoResult.of("07-optional", "all",
                "Next: /api/modules/08-datetime",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }

    private static String expensive(List<String> logs, String label) {
        logs.add("evaluated: " + label);
        return "DEFAULT";
    }

    private Optional<String> findLabel(String id) {
        return Optional.of("label-for-" + id);
    }

    private Optional<String> findLabelNested(String id) {
        return Optional.of("label-for-" + id);
    }

    private Optional<String> findUser(String id) {
        return Optional.empty();
    }
}
