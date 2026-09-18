package com.interview.java8.module04;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * MODULE 04 — Interface default & static methods (Java 8)
 *
 * Interview must-knows:
 * - default methods enable interface evolution without breaking implementors
 * - static methods in interfaces (helpers)
 * - Diamond problem resolution rules
 * - Why Abstract class still exists vs interface with defaults
 */
@Service
public class InterfaceDefaultsDemoService {

    interface Vehicle {
        String name();

        default String start() {
            return name() + " starting...";
        }

        default String stop() {
            return name() + " stopped";
        }

        static boolean isValidSpeed(int speed) {
            return speed >= 0 && speed <= 300;
        }
    }

    interface Electric {
        default String start() {
            return "Electric silent start";
        }

        default String charge() {
            return "charging...";
        }
    }

    /** Must override start() — both Vehicle and Electric define it (diamond). */
    static class Tesla implements Vehicle, Electric {
        @Override
        public String name() {
            return "Tesla";
        }

        @Override
        public String start() {
            // explicitly choose / combine
            return Electric.super.start() + " | " + Vehicle.super.start();
        }
    }

    static class Bike implements Vehicle {
        @Override
        public String name() {
            return "Bike";
        }
        // inherits default start/stop
    }

    public DemoResult defaultAndStatic() {
        Bike bike = new Bike();
        List<String> logs = new ArrayList<>();
        logs.add(bike.start());
        logs.add(bike.stop());
        logs.add("Vehicle.isValidSpeed(40)=" + Vehicle.isValidSpeed(40));
        logs.add("Vehicle.isValidSpeed(-1)=" + Vehicle.isValidSpeed(-1));

        return DemoResult.of("04-interfaces", "default-static",
                "default = instance behavior on interface. static = call via InterfaceName.method().",
                DemoResult.map("logs", logs));
    }

    public DemoResult diamondProblem() {
        Tesla t = new Tesla();
        return DemoResult.of("04-interfaces", "diamond",
                "If two interfaces have same default method → implementing class MUST override. Use Interface.super.method().",
                DemoResult.map(
                        "teslaStart", t.start(),
                        "charge", t.charge(),
                        "rule", "class wins over interface; subtype interface wins over super-interface"
                ));
    }

    public DemoResult vsAbstractClass() {
        Map<String, String> comparison = new LinkedHashMap<>();
        comparison.put("fields", "Abstract class: instance state. Interface: mostly behavior (+ defaults)");
        comparison.put("extends", "Class: one parent. Interfaces: many");
        comparison.put("when_abstract", "Shared state + hierarchy");
        comparison.put("when_default", "API evolve without breaking implementors");

        return DemoResult.of("04-interfaces", "vs-abstract",
                "Default methods ≠ abstract class replacement. Short answer enough for 3–4 YOE.",
                DemoResult.map("comparison", comparison));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(defaultAndStatic(), diamondProblem(), vsAbstractClass());
        return DemoResult.of("04-interfaces", "all",
                "Next: /api/modules/05-streams-basic",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
