package com.interview.java8.module01;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * MODULE 01 — Lambda expressions
 *
 * Interview must-knows:
 * - Lambda = anonymous function implementing a functional interface
 * - Syntax: (params) -> expression  OR  (params) -> { statements; }
 * - Effectively final local variables
 * - Target typing
 * - Difference from anonymous inner class (this, new scope)
 */
@Service
public class LambdaDemoService {

    /*
     * NOTES FOR COLLECTION-FRAMEWORK
     *
     * Object
     *   |
     *   +-- Iterable
     *   |     |
     *   |     +-- Collection
     *   |           |
     *   |           +-- List                         index, ordered, duplicates ok
     *   |           |     +-- ArrayList              resizable array, fast get
     *   |           |     +-- LinkedList             also a Deque
     *   |           |     +-- Vector                 synchronized, legacy
     *   |           |           +-- Stack            LIFO, legacy — prefer Deque
     *   |           |
     *   |           +-- Queue                        FIFO
     *   |           |     +-- Deque                  double-ended
     *   |           |     |     +-- ArrayDeque
     *   |           |     |     +-- LinkedList
     *   |           |     +-- PriorityQueue          heap, not FIFO
     *   |           |
     *   |           +-- Set                          no duplicates (equals / hashCode)
     *   |                 +-- HashSet
     *   |                 |     +-- LinkedHashSet    insertion order
     *   |                 +-- SortedSet
     *   |                       +-- NavigableSet
     *   |                             +-- TreeSet    red-black tree, Comparable / Comparator
     *   |
     *   +-- Map                                      does NOT extend Collection or Iterable
     *         +-- HashMap                            one null key allowed
     *         |     +-- LinkedHashMap                insertion or access order
     *         +-- SortedMap
     *         |     +-- NavigableMap
     *         |           +-- TreeMap
     *         +-- Hashtable                          synchronized, no null, legacy
     *               +-- Properties
     */

    public DemoResult syntaxForms() {
        List<String> logs = new ArrayList<>();

        // no-arg
        Runnable r = () -> logs.add("no-arg lambda");
        r.run();

        // one arg — parentheses optional
        java.util.function.Consumer<String> c1 = s -> logs.add("one-arg: " + s);
        java.util.function.Consumer<String> c2 = (String s) -> logs.add("typed one-arg: " + s);
        c1.accept("hi");
        c2.accept("hello");

        // multi-arg + block body
        BinaryOperator<Integer> add = (a, b) -> {
            int sum = a + b;
            return sum;
        };
        logs.add("add(2,3)=" + add.apply(2, 3));

        // expression body
        BinaryOperator<Integer> mul = (a, b) -> a * b;
        logs.add("mul(4,5)=" + mul.apply(4, 5));

        return DemoResult.of("01-lambda", "syntax",
                "Lambda needs a functional interface target. Body: expression OR { block with return }.",
                DemoResult.map("logs", logs));
    }

    public DemoResult effectivelyFinal() {
        List<String> logs = new ArrayList<>();
        String prefix = "user-"; // effectively final — not reassigned
        // prefix = "x-"; // would NOT compile if used inside lambda

        java.util.function.Function<String, String> f = name -> prefix + name;
        logs.add(f.apply("hemant"));
        logs.add("Local vars used in lambda must be final or effectively final.");

        return DemoResult.of("01-lambda", "effectively-final",
                "Effectively final = never reassigned after init. Required for capture in lambdas/inner classes.",
                DemoResult.map("logs", logs));
    }

    public DemoResult vsAnonymousClass() {
        List<String> logs = new ArrayList<>();

        Runnable anon = new Runnable() {
            @Override
            public void run() {
                logs.add("anonymous this=" + this.getClass().getName());
            }
        };
        anon.run();

        Runnable lambda = () -> logs.add("lambda has no own 'this' — uses enclosing class this");
        lambda.run();
        logs.add("enclosing this class=" + getClass().getSimpleName());

        return DemoResult.of("01-lambda", "vs-anonymous",
                "Anonymous class: own scope + this. Lambda: lexically scoped, this = enclosing instance. Prefer lambda.",
                DemoResult.map("logs", logs));
    }

    public DemoResult sortingWithLambda() {
        List<String> names = new ArrayList<>(List.of("Zara", "Amy", "John", "Bob"));
        List<String> before = new ArrayList<>(names);

        // pre-Java8: anonymous Comparator
        // Java8: lambda
        names.sort((a, b) -> a.compareToIgnoreCase(b));
        // even shorter: names.sort(String::compareToIgnoreCase);

        return DemoResult.of("01-lambda", "sorting",
                "Collections.sort / List.sort with lambda replaces verbose anonymous Comparator.",
                DemoResult.map("before", before, "after", names));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                syntaxForms(),
                effectivelyFinal(),
                vsAnonymousClass(),
                sortingWithLambda()
        );
        return DemoResult.of("01-lambda", "all",
                "Next: /api/modules/02-functional",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
