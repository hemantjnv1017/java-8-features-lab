package com.interview.java8.module12;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MODULE 12 — Stream coding quiz (~3–4 YOE)
 *
 * Same questions as a coding round: second highest, duplicates, frequency,
 * group by department, top N. Each demo is one question.
 */
@Service
public class StreamQuizDemoService {

    public static class Employee {
        private final String name;
        private final String department;
        private final double salary;

        public Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public Employee(String name, double salary) {
            this(name, null, salary);
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public double getSalary() {
            return salary;
        }

        public String label() {
            return department == null
                    ? name + ":" + salary
                    : name + ":" + department + ":" + salary;
        }
    }

    private static final List<Employee> EMPLOYEES = List.of(
            new Employee("Amit", 50000),
            new Employee("Rahul", 75000),
            new Employee("Vikram", 75000),
            new Employee("Sneha", 60000),
            new Employee("Pooja", 45000),
            new Employee("Alice", "IT", 90000),
            new Employee("Bob", "IT", 110000),
            new Employee("Charlie", "HR", 70000),
            new Employee("David", "HR", 85000),
            new Employee("Eva", "FINANCE", 95000),
            new Employee("Alice", 90000),
            new Employee("Bob", 110000),
            new Employee("Charlie", 70000),
            new Employee("David", 85000),
            new Employee("Eva", 50000)
    );

    /** Q1 — Second highest distinct salary. */
    public DemoResult secondHighestSalary() {
        Optional<Double> second = EMPLOYEES.stream()
                .map(Employee::getSalary)                         // Employee → salary; distinct() then compares numbers
                .distinct()                                       // 75000 twice must count as one salary
                .sorted(Comparator.reverseOrder())                // highest salary first
                .skip(1)                                          // drop #1, what remains starts at #2
                .findFirst();                                     // terminal: second value, or empty if only one distinct salary

        return DemoResult.of("12-stream-quiz", "second-highest",
                "distinct + sorted(reverse) + skip(1) + findFirst. Without distinct, two people on the top salary still look like #1.",
                DemoResult.map(
                        "employees", EMPLOYEES.stream().map(Employee::label).toList(),
                        "secondHighest", second.orElse(null)
                ));
    }

    /** Q2 — Second highest salary inside each department. */
    public DemoResult secondHighestByDept() {
        Map<String, String> byDept = EMPLOYEES.stream()
                .filter(e -> e.getDepartment() != null)           // groupingBy throws if the key is null
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,                  // key = department
                        LinkedHashMap::new,                       // keep departments in input order (HashMap would not)
                        Collectors.collectingAndThen(             // finish the group list, then run secondInGroup on it
                                Collectors.toList(),
                                StreamQuizDemoService::secondInGroup)
                ));

        return DemoResult.of("12-stream-quiz", "second-by-dept",
                "groupingBy + collectingAndThen. Same distinct/skip rule, applied per department. One person in a dept → not enough.",
                DemoResult.map("secondByDept", byDept));
    }

    /** Q3 — Duplicate values, in the order they repeat. */
    public DemoResult duplicates() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 4, 5, 5, 6);
        Set<Integer> seen = new HashSet<>();
        List<Integer> dups = numbers.stream()
                .filter(n -> !seen.add(n))                        // add() is false when n was already seen → keep the repeat
                .toList();                                        // terminal: collect. Do not parallelize — seen is shared state

        return DemoResult.of("12-stream-quiz", "duplicates",
                "seen.add returns false when the value was already there. Stateful filter — do not use on parallelStream.",
                DemoResult.map("input", numbers, "duplicates", dups));
    }

    /** Q4 — First character that appears once, in encounter order. */
    public DemoResult firstNonRepeated() {
        String name = "ababababashc";
        Character found = name.chars()                            // String → IntStream of char codes
                .mapToObj(c -> (char) c)                          // code → Character, so groupingBy has an object key
                .collect(Collectors.groupingBy(
                        c -> c,
                        LinkedHashMap::new,                       // insertion order, otherwise findFirst is random
                        Collectors.counting()))                   // frequency of each character
                .entrySet().stream()                              // Map itself has no stream()
                .filter(e -> e.getValue() == 1)                   // keep chars seen once
                .map(Map.Entry::getKey)
                .findFirst()                                      // first of those, because the map kept order
                .orElse(null);                                    // every char repeated → null, not NoSuchElementException

        return DemoResult.of("12-stream-quiz", "first-non-repeated",
                "LinkedHashMap keeps insertion order. HashMap would make findFirst meaningless.",
                DemoResult.map("input", name, "firstNonRepeated", found));
    }

    /** Q5 — Frequency of each character. */
    public DemoResult charFrequency() {
        String text = "sdjvbvhbeivbe";
        Map<Character, Long> freq = text.chars()                  // IntStream of codes, not Stream<Character>
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        c -> c,
                        LinkedHashMap::new,                       // stable order for the demo output
                        Collectors.counting()));                  // downstream: count per key, result is Long

        return DemoResult.of("12-stream-quiz", "char-frequency",
                "chars() is an IntStream of code points. mapToObj to Character, then groupingBy + counting.",
                DemoResult.map("input", text, "frequency", freq));
    }

    /** Q6 — Frequency of each word in a sentence. */
    public DemoResult wordFrequency() {
        String sentence = "Java is fun and learning Java is always fun.";
        Map<String, Long> freq = Arrays.stream(sentence.split("\\s+")) // split gives String[], stream turns it into Stream<String>
                .map(word -> word.replaceAll("[^a-zA-Z]", ""))    // drop punctuation so "fun." and "fun" match
                .map(String::toLowerCase)                         // "Java" and "java" are the same word
                .filter(word -> !word.isEmpty())                  // a token that was only punctuation
                .collect(Collectors.groupingBy(
                        w -> w,
                        LinkedHashMap::new,
                        Collectors.counting()));

        return DemoResult.of("12-stream-quiz", "word-frequency",
                "Strip punctuation, lower-case, then groupingBy + counting. Same pattern as character frequency.",
                DemoResult.map("input", sentence, "frequency", freq));
    }

    /** Q7 — First character that shows up a second time. */
    public DemoResult firstRepeated() {
        String text = "adwivbvbwivbcbcaa";
        Set<Character> seen = new HashSet<>();
        Character found = text.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> !seen.add(c))                        // same seen-set as Q3: false means this char already appeared
                .findFirst()                                      // stop at the first repeat; does not scan the rest
                .orElse(null);

        return DemoResult.of("12-stream-quiz", "first-repeated",
                "Same seen-set as duplicates, but findFirst stops at the first repeat. Short-circuit.",
                DemoResult.map("input", text, "firstRepeated", found));
    }

    /** Q8 — Employees whose salary is greater than the average. */
    public DemoResult aboveAverageSalary() {
        double avg = EMPLOYEES.stream()
                .mapToDouble(Employee::getSalary)
                .average()                                        // terminal on DoubleStream: OptionalDouble
                .orElse(0);                                       // empty list → 0, so the next filter still runs
        List<String> above = EMPLOYEES.stream()                       // new stream: the one above is already consumed
                .filter(e -> e.getSalary() > avg)
                .map(Employee::label)                             // show name:salary, not the whole object
                .toList();

        return DemoResult.of("12-stream-quiz", "above-average",
                "average() is terminal, so the filter needs a second stream. A stream cannot be reused.",
                DemoResult.map("average", avg, "aboveAverage", above));
    }

    /** Q9 — Group employees by department. */
    public DemoResult groupByDepartment() {
        Map<String, List<String>> grouped = EMPLOYEES.stream()
                .filter(e -> e.getDepartment() != null)           // groupingBy throws if the key is null
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        LinkedHashMap::new,
                        Collectors.mapping(Employee::label, Collectors.toList()) // value = labels, not List<Employee>
                ));

        return DemoResult.of("12-stream-quiz", "group-by-dept",
                "groupingBy(key, mapFactory, downstream). mapping(...) keeps only the label, not the whole object.",
                DemoResult.map("byDepartment", grouped));
    }

    /** Q10 — Sort employees by salary (lowest first). */
    public DemoResult sortBySalary() {
        List<String> ascending = EMPLOYEES.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary)) // Employee has no Comparable; salary is a double
                .map(Employee::label)
                .toList();                                        // source list stays unsorted; this is a new list

        return DemoResult.of("12-stream-quiz", "sort-by-salary",
                "sorted(Comparator.comparingDouble). reversed() for highest-first. Stream sorted does not change the source list.",
                DemoResult.map("ascending", ascending));
    }

    /** Q11 — Employee with maximum salary and employee with minimum salary. */
    public DemoResult minMaxSalary() {
        Employee max = EMPLOYEES.stream()
                .max(Comparator.comparingDouble(Employee::getSalary)) // one pass, no full sort
                .orElseThrow();                                  // empty stream: fail here, do not call Optional.get()
        Employee min = EMPLOYEES.stream()
                .min(Comparator.comparingDouble(Employee::getSalary))
                .orElseThrow();

        return DemoResult.of("12-stream-quiz", "min-max",
                "max/min take a Comparator and return Optional. Empty list → orElseThrow / orElse, never get() blind.",
                DemoResult.map("highest", max.label(), "lowest", min.label()));
    }

    /** Q12 — Top 3 salaries overall, and top 3 inside each department. */
    public DemoResult topNSalary() {
        List<String> top3 = EMPLOYEES.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed()) // highest first; ascending + limit is the bottom 3
                .limit(3)                                         // stop after 3; short-circuit
                .map(Employee::label)
                .toList();

        Map<String, List<String>> top3ByDept = EMPLOYEES.stream()
                .filter(e -> e.getDepartment() != null)           // groupingBy throws if the key is null
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(             // same top-3 pipeline, once per department
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                                        .limit(3)
                                        .map(Employee::label)
                                        .toList()
                        )
                ));

        return DemoResult.of("12-stream-quiz", "top-n",
                "Highest-first needs reversed() before limit. Ascending + limit(3) returns the lowest 3.",
                DemoResult.map("top3", top3, "top3ByDept", top3ByDept));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                secondHighestSalary(), secondHighestByDept(), duplicates(),
                firstNonRepeated(), charFrequency(), wordFrequency(), firstRepeated(),
                aboveAverageSalary(), groupByDepartment(), sortBySalary(), minMaxSalary(), topNSalary()
        );
        return DemoResult.of("12-stream-quiz", "all",
                "Coding-round set. Next: INTERVIEW-QUESTIONS-3-4YOE.md",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }

    /** Q2 helper — same distinct/skip rule as Q1, on one department. */
    private static String secondInGroup(List<Employee> group) {
        return group.stream()
                .map(Employee::getSalary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .flatMap(salary -> group.stream()                 // salary found → pick one employee who has it
                        .filter(e -> Double.compare(e.getSalary(), salary) == 0)
                        .findFirst())
                .map(Employee::label)
                .orElse("not enough");                            // only one distinct salary in this department
    }
}
