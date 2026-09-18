package com.interview.java8.module02;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * MODULE 02 — Built-in functional interfaces (java.util.function)
 *
 * Interview must-knows — memorize these 4 + variants:
 * - Predicate&lt;T&gt;     → boolean test(T)
 * - Function&lt;T,R&gt;    → R apply(T)
 * - Consumer&lt;T&gt;      → void accept(T)
 * - Supplier&lt;T&gt;      → T get()
 * Plus: UnaryOperator, BinaryOperator, BiFunction, BiPredicate, BiConsumer
 * Composition: andThen, compose, and, or, negate
 */
@Service
public class FunctionalInterfacesDemoService {

    @FunctionalInterface
    interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
        // @FunctionalInterface allows exactly ONE abstract method; default/static OK
    }

    public DemoResult coreFour() {
        Predicate<String> notBlank = s -> s != null && !s.isBlank();
        Function<String, Integer> length = String::length;
        Consumer<String> printer = s -> { /* side-effect */ };
        Supplier<Double> random = Math::random;

        String input = "Java8";
        List<String> logs = new ArrayList<>();
        logs.add("Predicate.test → " + notBlank.test(input));
        logs.add("Function.apply → " + length.apply(input));
        printer.accept("Consumer ran");
        logs.add("Supplier.get → " + String.format("%.3f", random.get()));

        return DemoResult.of("02-functional", "core-four",
                "Predicate/Function/Consumer/Supplier — draw the table in interviews from memory.",
                DemoResult.map("logs", logs));
    }

    public DemoResult biAndOperators() {
        BiFunction<Integer, Integer, Integer> sum = Integer::sum;
        BinaryOperator<Integer> max = Integer::max; // BiFunction&lt;T,T,T&gt;
        UnaryOperator<String> shout = s -> s.toUpperCase() + "!";
        BiPredicate<String, Integer> longEnough = (s, n) -> s.length() >= n;
        BiConsumer<String, Integer> logPair = (s, n) -> { };

        return DemoResult.of("02-functional", "bi-operators",
                "UnaryOperator&lt;T&gt; = Function&lt;T,T&gt;. BinaryOperator&lt;T&gt; = BiFunction&lt;T,T,T&gt;.",
                DemoResult.map(
                        "sum(3,4)", sum.apply(3, 4),
                        "max(9,2)", max.apply(9, 2),
                        "shout", shout.apply("ok"),
                        "longEnough", longEnough.test("hello", 4)
                ));
    }

    public DemoResult composition() {
        Predicate<String> startsWithJ = s -> s.startsWith("J");
        Predicate<String> longWord = s -> s.length() > 3;
        Predicate<String> combined = startsWithJ.and(longWord).or(s -> s.equals("Go"));

        Function<Integer, Integer> times2 = x -> x * 2;
        Function<Integer, Integer> plus10 = x -> x + 10;
        // compose: plus10 first, then times2 → 2*(x+10)
        // andThen: times2 first, then plus10 → (2*x)+10
        int compose = times2.compose(plus10).apply(5);  // 2*15=30
        int andThen = times2.andThen(plus10).apply(5);  // 10+10=20

        Function<String, Integer> len = String::length;
        Function<Integer, String> stars = n -> "*".repeat(n);
        String pipeline = len.andThen(stars).apply("Java");

        return DemoResult.of("02-functional", "composition",
                "Predicate: and/or/negate. Function: compose (before) vs andThen (after).",
                DemoResult.map(
                        "Java8 passes", combined.test("Java8"),
                        "Go passes", combined.test("Go"),
                        "compose(5)", compose,
                        "andThen(5)", andThen,
                        "pipeline", pipeline
                ));
    }

    public DemoResult customFunctionalInterface() {
        TriFunction<Integer, Integer, Integer, Integer> volume = (l, w, h) -> l * w * h;
        int v = volume.apply(2, 3, 4);

        return DemoResult.of("02-functional", "custom-fi",
                "@FunctionalInterface documents intent. SAM = Single Abstract Method.",
                DemoResult.map("volume", v, "rule", "Exactly one abstract method"));
    }

    public DemoResult primitiveSpecializations() {
        // Avoid boxing overhead in hot paths
        IntPredicate even = i -> i % 2 == 0;
        ToIntFunction<String> toLen = String::length;
        IntFunction<String> box = i -> "n=" + i;
        ObjIntConsumer<List<Integer>> addInt = List::add;

        List<Integer> nums = new ArrayList<>();
        addInt.accept(nums, 10);
        addInt.accept(nums, 11);

        return DemoResult.of("02-functional", "primitive-fi",
                "IntPredicate, ToIntFunction, LongSupplier... avoid autoboxing in streams of primitives.",
                DemoResult.map(
                        "even(4)", even.test(4),
                        "toLen(Java)", toLen.applyAsInt("Java"),
                        "box(7)", box.apply(7),
                        "nums", nums
                ));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                coreFour(), biAndOperators(), composition(),
                customFunctionalInterface(), primitiveSpecializations()
        );
        return DemoResult.of("02-functional", "all",
                "Next: /api/modules/03-method-refs",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
