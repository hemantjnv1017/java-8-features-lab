package com.interview.java8.api;

import com.interview.java8.common.DemoResult;
import com.interview.java8.module01.LambdaDemoService;
import com.interview.java8.module02.FunctionalInterfacesDemoService;
import com.interview.java8.module03.MethodRefsDemoService;
import com.interview.java8.module04.InterfaceDefaultsDemoService;
import com.interview.java8.module05.StreamsBasicDemoService;
import com.interview.java8.module06.StreamsAdvancedDemoService;
import com.interview.java8.module07.OptionalDemoService;
import com.interview.java8.module08.DateTimeDemoService;
import com.interview.java8.module09.CollectionsEnhancementsDemoService;
import com.interview.java8.module10.CompletableFutureDemoService;
import com.interview.java8.module11.InterviewPatternsDemoService;
import com.interview.java8.module12.StreamQuizDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Prefer Swagger: http://localhost:8081/swagger-ui/index.html
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Java 8 learning modules", description = "~3–4 YOE interview demos")
public class LearningController {

    private final LambdaDemoService lambda;
    private final FunctionalInterfacesDemoService functional;
    private final MethodRefsDemoService methodRefs;
    private final InterfaceDefaultsDemoService interfaces;
    private final StreamsBasicDemoService streamsBasic;
    private final StreamsAdvancedDemoService streamsAdvanced;
    private final OptionalDemoService optional;
    private final DateTimeDemoService dateTime;
    private final CollectionsEnhancementsDemoService collections;
    private final CompletableFutureDemoService completable;
    private final InterviewPatternsDemoService patterns;
    private final StreamQuizDemoService streamQuiz;

    public LearningController(
            LambdaDemoService lambda,
            FunctionalInterfacesDemoService functional,
            MethodRefsDemoService methodRefs,
            InterfaceDefaultsDemoService interfaces,
            StreamsBasicDemoService streamsBasic,
            StreamsAdvancedDemoService streamsAdvanced,
            OptionalDemoService optional,
            DateTimeDemoService dateTime,
            CollectionsEnhancementsDemoService collections,
            CompletableFutureDemoService completable,
            InterviewPatternsDemoService patterns,
            StreamQuizDemoService streamQuiz) {
        this.lambda = lambda;
        this.functional = functional;
        this.methodRefs = methodRefs;
        this.interfaces = interfaces;
        this.streamsBasic = streamsBasic;
        this.streamsAdvanced = streamsAdvanced;
        this.optional = optional;
        this.dateTime = dateTime;
        this.collections = collections;
        this.completable = completable;
        this.patterns = patterns;
        this.streamQuiz = streamQuiz;
    }

    @GetMapping("/modules")
    @Operation(summary = "Catalog — start here")
    public Map<String, Object> catalog() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("title", "Java 8 Features Lab — ~3–4 YOE Interview Path");
        body.put("port", 8081);
        body.put("swagger", "http://localhost:8081/swagger-ui/index.html");
        body.put("questionBank", "INTERVIEW-QUESTIONS-3-4YOE.md");
        body.put("howToRevise", List.of(
                "Modules 01→11 in order (~3–4 YOE depth)",
                "Read interviewTip in 30–60s",
                "Practice coding demos in 11-patterns and 12-stream-quiz",
                "Use INTERVIEW-QUESTIONS-3-4YOE.md as checklist"
        ));
        body.put("modules", List.of(
                mod("01-lambda", "Lambda syntax, effectively final, vs anonymous",
                        List.of("syntax", "effectively-final", "vs-anonymous", "sorting", "all")),
                mod("02-functional", "Predicate/Function/Consumer/Supplier + composition",
                        List.of("core-four", "bi-operators", "composition", "custom-fi", "all")),
                mod("03-method-refs", "Four kinds of method references",
                        List.of("four-kinds", "with-streams", "when-not", "all")),
                mod("04-interfaces", "default/static methods, diamond",
                        List.of("default-static", "diamond", "vs-abstract", "all")),
                mod("05-streams-basic", "Lazy pipeline, map/flatMap, find, stream reuse",
                        List.of("lazy-pipeline", "filter-map", "flatmap", "match-find-reduce",
                                "primitive-streams", "stream-reuse", "all")),
                mod("06-streams-advanced", "groupingBy, toMap, joining, parallel caveats",
                        List.of("grouping", "tomap-joining", "collecting-and-then", "parallel", "infinite", "all")),
                mod("07-optional", "orElse vs orElseGet, map/flatMap, anti-patterns",
                        List.of("creation", "orelse-vs-orelseget", "map-flatmap", "anti-patterns", "with-streams", "all")),
                mod("08-datetime", "LocalDate/Time, Instant, Period/Duration, formatter",
                        List.of("local-types", "zones-instant", "period-duration", "format-parse", "legacy", "all")),
                mod("09-collections", "Map merge/compute, Comparator, frequency",
                        List.of("list-helpers", "map-compute", "comparator", "misc", "frequency", "all")),
                mod("10-completable", "CompletableFuture chaining (Java 8 intro)",
                        List.of("chain", "combine", "errors", "executor", "all")),
                mod("11-patterns", "Coding patterns: topN, group, frequency, checklist",
                        List.of("topn-second", "group-agg", "partition-join", "optional-pipeline",
                                "frequency-duplicates", "checklist", "all")),
                mod("12-stream-quiz", "Coding quiz: second highest, frequency, group, top N",
                        List.of("second-highest", "second-by-dept", "duplicates", "first-non-repeated",
                                "char-frequency", "word-frequency", "first-repeated", "above-average",
                                "group-by-dept", "sort-by-salary", "min-max", "top-n", "all"))
        ));
        return body;
    }

    @GetMapping("/modules/{moduleId}")
    @Operation(summary = "Run all demos in a module")
    public DemoResult runModule(
            @Parameter(example = "05-streams-basic") @PathVariable String moduleId) throws Exception {
        return switch (moduleId) {
            case "01-lambda" -> lambda.all();
            case "02-functional" -> functional.all();
            case "03-method-refs" -> methodRefs.all();
            case "04-interfaces" -> interfaces.all();
            case "05-streams-basic" -> streamsBasic.all();
            case "06-streams-advanced" -> streamsAdvanced.all();
            case "07-optional" -> optional.all();
            case "08-datetime" -> dateTime.all();
            case "09-collections" -> collections.all();
            case "10-completable" -> completable.all();
            case "11-patterns" -> patterns.all();
            case "12-stream-quiz" -> streamQuiz.all();
            default -> DemoResult.of(moduleId, "unknown", "GET /api/modules", Map.of());
        };
    }

    @GetMapping("/modules/{moduleId}/{demo}")
    @Operation(summary = "Run one demo")
    public DemoResult runDemo(
            @Parameter(example = "07-optional") @PathVariable String moduleId,
            @Parameter(example = "orelse-vs-orelseget") @PathVariable String demo) throws Exception {
        return switch (moduleId) {
            case "01-lambda" -> switch (demo) {
                case "syntax" -> lambda.syntaxForms();
                case "effectively-final" -> lambda.effectivelyFinal();
                case "vs-anonymous" -> lambda.vsAnonymousClass();
                case "sorting" -> lambda.sortingWithLambda();
                case "all" -> lambda.all();
                default -> unknown(moduleId, demo);
            };
            case "02-functional" -> switch (demo) {
                case "core-four" -> functional.coreFour();
                case "bi-operators" -> functional.biAndOperators();
                case "composition" -> functional.composition();
                case "custom-fi" -> functional.customFunctionalInterface();
                case "all" -> functional.all();
                default -> unknown(moduleId, demo);
            };
            case "03-method-refs" -> switch (demo) {
                case "four-kinds" -> methodRefs.fourKinds();
                case "with-streams" -> methodRefs.withStreams();
                case "when-not" -> methodRefs.whenNotToUse();
                case "all" -> methodRefs.all();
                default -> unknown(moduleId, demo);
            };
            case "04-interfaces" -> switch (demo) {
                case "default-static" -> interfaces.defaultAndStatic();
                case "diamond" -> interfaces.diamondProblem();
                case "vs-abstract" -> interfaces.vsAbstractClass();
                case "all" -> interfaces.all();
                default -> unknown(moduleId, demo);
            };
            case "05-streams-basic" -> switch (demo) {
                case "lazy-pipeline" -> streamsBasic.pipelineLazy();
                case "filter-map" -> streamsBasic.filterMapDistinctSorted();
                case "flatmap" -> streamsBasic.flatMapDemo();
                case "match-find-reduce" -> streamsBasic.matchFindReduce();
                case "primitive-streams" -> streamsBasic.primitiveStreams();
                case "stream-reuse" -> streamsBasic.streamReuse();
                case "all" -> streamsBasic.all();
                default -> unknown(moduleId, demo);
            };
            case "06-streams-advanced" -> switch (demo) {
                case "grouping" -> streamsAdvanced.groupingPartitioning();
                case "tomap-joining" -> streamsAdvanced.toMapJoining();
                case "collecting-and-then" -> streamsAdvanced.collectingAndThen();
                case "parallel" -> streamsAdvanced.parallelCaveats();
                case "infinite" -> streamsAdvanced.infiniteAndShortCircuit();
                case "all" -> streamsAdvanced.all();
                default -> unknown(moduleId, demo);
            };
            case "07-optional" -> switch (demo) {
                case "creation" -> optional.creationAndBasic();
                case "orelse-vs-orelseget" -> optional.orElseVsOrElseGet();
                case "map-flatmap" -> optional.mapFlatMapFilter();
                case "anti-patterns" -> optional.antiPatterns();
                case "with-streams" -> optional.withStreams();
                case "all" -> optional.all();
                default -> unknown(moduleId, demo);
            };
            case "08-datetime" -> switch (demo) {
                case "local-types" -> dateTime.localTypes();
                case "zones-instant" -> dateTime.zonesAndInstant();
                case "period-duration" -> dateTime.periodDuration();
                case "format-parse" -> dateTime.formattingParsing();
                case "legacy" -> dateTime.legacyInterop();
                case "all" -> dateTime.all();
                default -> unknown(moduleId, demo);
            };
            case "09-collections" -> switch (demo) {
                case "list-helpers" -> collections.listHelpers();
                case "map-compute" -> collections.mapComputeMerge();
                case "comparator" -> collections.comparatorFactory();
                case "misc" -> collections.stringJoinerAndBase64();
                case "frequency" -> collections.frequencyCountPattern();
                case "all" -> collections.all();
                default -> unknown(moduleId, demo);
            };
            case "10-completable" -> switch (demo) {
                case "chain" -> completable.basicChain();
                case "combine" -> completable.combineAllOf();
                case "errors" -> completable.errorHandling();
                case "executor" -> completable.customExecutor();
                case "all" -> completable.all();
                default -> unknown(moduleId, demo);
            };
            case "11-patterns" -> switch (demo) {
                case "topn-second" -> patterns.topNAndSecondHighest();
                case "group-agg" -> patterns.groupAvgMax();
                case "partition-join" -> patterns.partitionAndJoin();
                case "optional-pipeline" -> patterns.optionalPipeline();
                case "frequency-duplicates" -> patterns.frequencyAndDuplicates();
                case "checklist" -> patterns.revisionChecklist();
                case "all" -> patterns.all();
                default -> unknown(moduleId, demo);
            };
            case "12-stream-quiz" -> switch (demo) {
                case "second-highest" -> streamQuiz.secondHighestSalary();
                case "second-by-dept" -> streamQuiz.secondHighestByDept();
                case "duplicates" -> streamQuiz.duplicates();
                case "first-non-repeated" -> streamQuiz.firstNonRepeated();
                case "char-frequency" -> streamQuiz.charFrequency();
                case "word-frequency" -> streamQuiz.wordFrequency();
                case "first-repeated" -> streamQuiz.firstRepeated();
                case "above-average" -> streamQuiz.aboveAverageSalary();
                case "group-by-dept" -> streamQuiz.groupByDepartment();
                case "sort-by-salary" -> streamQuiz.sortBySalary();
                case "min-max" -> streamQuiz.minMaxSalary();
                case "top-n" -> streamQuiz.topNSalary();
                case "all" -> streamQuiz.all();
                default -> unknown(moduleId, demo);
            };
            default -> unknown(moduleId, demo);
        };
    }

    private static Map<String, Object> mod(String id, String summary, List<String> demos) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        m.put("summary", summary);
        m.put("runAll", "/api/modules/" + id);
        m.put("demos", demos.stream().map(d -> "/api/modules/" + id + "/" + d).toList());
        return m;
    }

    private static DemoResult unknown(String moduleId, String demo) {
        return DemoResult.of(moduleId, demo, "Unknown. GET /api/modules", Map.of());
    }
}
