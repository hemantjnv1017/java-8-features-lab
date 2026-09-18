package com.interview.java8.module10;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * MODULE 10 — CompletableFuture (introduced in Java 8)
 *
 * Interview must-knows (Java 8 surface):
 * - supplyAsync / runAsync
 * - thenApply / thenAccept / thenRun / thenCompose
 * - thenCombine / allOf / anyOf
 * - exceptionally / handle
 * - Default executor = ForkJoinPool.commonPool()
 *
 * Deeper CF lives in multithreading lab; here = Java 8 feature angle.
 */
@Service
public class CompletableFutureDemoService {

    public DemoResult basicChain() throws Exception {
        String result = CompletableFuture
                .supplyAsync(() -> "java")
                .thenApply(String::toUpperCase)
                .thenApply(s -> s + "-8")
                .get(2, TimeUnit.SECONDS);

        return DemoResult.of("10-completable", "chain",
                "thenApply = map. thenCompose = flatMap for async. Prefer CF over raw Future for composition.",
                DemoResult.map("result", result));
    }

    public DemoResult combineAllOf() throws Exception {
        CompletableFuture<String> user = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return "Hemant";
        });
        CompletableFuture<Integer> score = CompletableFuture.supplyAsync(() -> {
            sleep(80);
            return 98;
        });

        String combined = user.thenCombine(score, (u, s) -> u + ":" + s).get();

        CompletableFuture<Void> all = CompletableFuture.allOf(user, score);
        all.join();

        Object first = CompletableFuture.anyOf(
                CompletableFuture.supplyAsync(() -> { sleep(100); return "slow"; }),
                CompletableFuture.supplyAsync(() -> { sleep(20); return "fast"; })
        ).get();

        return DemoResult.of("10-completable", "combine",
                "thenCombine(2 values). allOf waits all (Void). anyOf = first completed.",
                DemoResult.map("combined", combined, "anyOf", first));
    }

    public DemoResult errorHandling() throws Exception {
        String recovered = CompletableFuture
                .supplyAsync(() -> {
                    if (true) throw new RuntimeException("boom");
                    return "ok";
                })
                .exceptionally(ex -> "fallback:" + ex.getCause().getMessage())
                .get();

        String handled = CompletableFuture
                .supplyAsync(() -> 10)
                .handle((v, ex) -> ex == null ? "v=" + v : "err")
                .get();

        return DemoResult.of("10-completable", "errors",
                "exceptionally recovers. handle sees both success and failure.",
                DemoResult.map("recovered", recovered, "handled", handled));
    }

    public DemoResult customExecutor() throws Exception {
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            String result = CompletableFuture
                    .supplyAsync(() -> Thread.currentThread().getName(), pool)
                    .thenApplyAsync(name -> "ran-on-" + name, pool)
                    .get(2, TimeUnit.SECONDS);

            return DemoResult.of("10-completable", "executor",
                    "Pass an Executor to supplyAsync/thenApplyAsync — don't overload commonPool in servers.",
                    DemoResult.map("result", result));
        }
    }

    public DemoResult all() throws Exception {
        List<DemoResult> parts = List.of(
                basicChain(), combineAllOf(), errorHandling(), customExecutor()
        );
        return DemoResult.of("10-completable", "all",
                "Next: /api/modules/11-patterns",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
