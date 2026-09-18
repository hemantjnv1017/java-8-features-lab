package com.interview.java8.module08;

import com.interview.java8.common.DemoResult;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * MODULE 08 — java.time (JSR-310) — replaces Date/Calendar
 *
 * Interview must-knows:
 * - LocalDate / LocalTime / LocalDateTime (no zone)
 * - ZonedDateTime / Instant / ZoneId
 * - Period (date-based) vs Duration (time-based)
 * - DateTimeFormatter (immutable, thread-safe — unlike SimpleDateFormat)
 * - Why old Date is bad (mutable, confusing months)
 */
@Service
public class DateTimeDemoService {

    public DemoResult localTypes() {
        LocalDate today = LocalDate.now();
        LocalDate independence = LocalDate.of(1947, Month.AUGUST, 15);
        LocalTime noon = LocalTime.of(12, 0);
        LocalDateTime meeting = LocalDateTime.of(today, noon).plusDays(1).minusHours(2);

        return DemoResult.of("08-datetime", "local-types",
                "Local* = no timezone. Use for birthdays, business dates without zone math.",
                DemoResult.map(
                        "today", today.toString(),
                        "independence", independence.toString(),
                        "dayOfWeek", independence.getDayOfWeek().toString(),
                        "noon", noon.toString(),
                        "meeting", meeting.toString()
                ));
    }

    public DemoResult zonesAndInstant() {
        ZoneId india = ZoneId.of("Asia/Kolkata");
        ZoneId nyc = ZoneId.of("America/New_York");

        ZonedDateTime inIndia = ZonedDateTime.now(india);
        ZonedDateTime inNyc = inIndia.withZoneSameInstant(nyc);

        Instant now = Instant.now(); // machine timeline UTC
        Instant plus = now.plus(30, ChronoUnit.MINUTES);

        return DemoResult.of("08-datetime", "zones-instant",
                "Instant = UTC timestamp. ZonedDateTime = human local + zone. Convert with withZoneSameInstant.",
                DemoResult.map(
                        "india", inIndia.toString(),
                        "nycSameInstant", inNyc.toString(),
                        "instant", now.toString(),
                        "instantPlus30m", plus.toString()
                ));
    }

    public DemoResult periodDuration() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 15);
        Period period = Period.between(start, end);

        Duration duration = Duration.between(
                LocalTime.of(9, 0),
                LocalTime.of(17, 30)
        );

        return DemoResult.of("08-datetime", "period-duration",
                "Period = years/months/days. Duration = hours/minutes/seconds/nanos. Don't mix them up.",
                DemoResult.map(
                        "period", period.getYears() + "y " + period.getMonths() + "m " + period.getDays() + "d",
                        "workDayHours", duration.toHours(),
                        "workDayMinutes", duration.toMinutes()
                ));
    }

    public DemoResult formattingParsing() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        LocalDateTime ldt = LocalDateTime.of(2026, 9, 15, 23, 59);
        String formatted = ldt.format(fmt);
        LocalDateTime parsed = LocalDateTime.parse(formatted, fmt);

        DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE;

        return DemoResult.of("08-datetime", "format-parse",
                "DateTimeFormatter is immutable & thread-safe. Never use SimpleDateFormat in new code.",
                DemoResult.map(
                        "formatted", formatted,
                        "parsed", parsed.toString(),
                        "isoToday", LocalDate.now().format(iso)
                ));
    }

    public DemoResult legacyInterop() {
        Date legacy = new Date();
        Instant instant = legacy.toInstant();
        Date back = Date.from(instant);

        Calendar cal = Calendar.getInstance();
        ZonedDateTime zdt = cal.toInstant().atZone(ZoneId.systemDefault());

        return DemoResult.of("08-datetime", "legacy",
                "Date→Instant via toInstant(). Instant→Date via Date.from(). Prefer java.time everywhere new.",
                DemoResult.map(
                        "legacyMillis", legacy.getTime(),
                        "instant", instant.toString(),
                        "roundTripOk", legacy.getTime() == back.getTime(),
                        "fromCalendar", zdt.toLocalDate().toString()
                ));
    }

    public DemoResult all() {
        List<DemoResult> parts = List.of(
                localTypes(), zonesAndInstant(), periodDuration(),
                formattingParsing(), legacyInterop()
        );
        return DemoResult.of("08-datetime", "all",
                "Next: /api/modules/09-collections",
                DemoResult.map("demos", parts.stream().map(DemoResult::demo).toList(), "results", parts));
    }
}
