package ru.megaeater42.telegram.calendar.translation;

import java.time.Month;
import java.util.Map;

import static java.util.Map.entry;

public class InlineCalendarTranslationEN {
    public static final String[] WEEK_DAYS = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public static final Map<Month, String> MONTHS = Map.ofEntries(
            entry(Month.JANUARY, "January"),
            entry(Month.FEBRUARY, "February"),
            entry(Month.MARCH, "March"),
            entry(Month.APRIL, "April"),
            entry(Month.MAY, "May"),
            entry(Month.JUNE, "June"),
            entry(Month.JULY, "July"),
            entry(Month.AUGUST, "August"),
            entry(Month.SEPTEMBER, "September"),
            entry(Month.OCTOBER, "October"),
            entry(Month.NOVEMBER, "November"),
            entry(Month.DECEMBER, "December")
    );
}
