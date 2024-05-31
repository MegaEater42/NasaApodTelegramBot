package ru.megaeater42.telegram.calendar.translation;

import java.time.Month;
import java.util.Map;

import static java.util.Map.entry;

public class InlineCalendarTranslationRU {
    public static final String[] WEEK_DAYS = new String[] {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    public static final Map<Month, String> MONTHS = Map.ofEntries(
            entry(Month.JANUARY, "Январь"),
            entry(Month.FEBRUARY, "Февраль"),
            entry(Month.MARCH, "Март"),
            entry(Month.APRIL, "Апрель"),
            entry(Month.MAY, "Май"),
            entry(Month.JUNE, "Июнь"),
            entry(Month.JULY, "Июль"),
            entry(Month.AUGUST, "Август"),
            entry(Month.SEPTEMBER, "Сентябрь"),
            entry(Month.OCTOBER, "Октябрь"),
            entry(Month.NOVEMBER, "Ноябрь"),
            entry(Month.DECEMBER, "Декабрь")
    );
}
