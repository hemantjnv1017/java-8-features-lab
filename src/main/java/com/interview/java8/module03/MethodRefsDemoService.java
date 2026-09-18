package com.interview.java8.module03;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * MODULE 03 — Method references
 *
 * Four kinds (memorize):
 * 1. Static:          Type::staticMethod
 * 2. Instance bound:  instance::instanceMethod
 * 3. Instance unbound: Type::instanceMethod   (first arg is the instance)
 * 4. Constructor:     Type::new
 */
@Service
public class MethodRefsDemoService {

    public DemoResult fourKinds() {
        List<String> logs = new ArrayList<>();

        // 1) static
        Function<String, Integer> parse = Integer::parseInt;
        logs.add("static Integer::parseInt → " + parse.apply("42"));

        // 2) bound instance
        String prefix = "Hello ";
        Function<String, String> greet = prefix::concat;
        logs.add("bound prefix::concat → " + greet.apply("Java"));

        // 3) unbound instance — receiver is first argument
        Function<String, String> upper = String::toUpperCase;
        logs.add("unbound String::toUpperCase → " + upper.apply("java8"));

        BiFunction<String, String, Integer> cmp = String::compareToIgnoreCase;
        logs.add("unbound compare → " + cmp.apply("a", "B"));

        // 4) constructor
        Supplier<List<String>> newList = ArrayList::new;
        Function<String, StringBuilder> newSb = StringBuilder::new;
        List<String> list = newList.get();
        list.add(newSb.apply("ok").toString());
        logs.add("constructor refs → " + list);

        return DemoResult.of("03-method-refs", "four-kinds",
                "Method ref = shorter lambda when you only call an existing method. Same four kinds every interview.",
                DemoResult.map("logs", logs));
    }

    public DemoResult withStreams() {
        List<String> names = List.of("amy", "bob", "cara");

        List<String> upper = names.stream()
                .map(String::toUpperCase)
                .sorted(String::compareTo)
                .collect(Collectors.toList());

        List<Person> people = names.stream()
                .map(Person::new) // constructor ref
                .collect(Collectors.toList());

        people.forEach(System.out::println); // bound? actually PrintStream::println unbound-ish

        return DemoResult.of("03-method-refs", "with-streams",
                "map(String::toUpperCase), map(Person::new), forEach(System.out::println) — very common in code reviews.",
                DemoResult.map(
                        "upper", upper,
                        "people", people.stream().map(Person::name).toList()
                ));
    }

    public DemoResult whenNotToUse() {
        List<String> tips = List.of(
                "Use lambda when you need extra logic: s -> s == null ? \"\" : s.trim()",
                "Use method ref when it IS the whole body: String::trim",
                "Ambiguous overloads can fail to compile — then write a lambda"
        );
        Function<String, String> safe = s -> s == null ? "" : s.trim();

        return DemoResult.of("03-method-refs", "when-not",
                "Method refs are sugar. If clarity suffers or logic needed → keep the lambda.",
                DemoResult.map("tips", tips, "safeTrim", safe.apply("  x  ")));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(fourKinds(), withStreams(), whenNotToUse());
        return DemoResult.of("03-method-refs", "all",
                "Next: /api/modules/04-interfaces",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }

    public record Person(String name) {
        @Override
        public String toString() {
            return "Person(" + name + ")";
        }
    }
}
