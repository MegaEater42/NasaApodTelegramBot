package ru.megaeater42.telegram.calendar.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String toString(LocalDate localDate, String dayMonthYearPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dayMonthYearPattern);
        return localDate.format(formatter);
    }

    public static String toString(LocalDate localDate) {
        return toString(localDate, "yyyy-MM-dd");
    }

    public static LocalDate toDate(String dateAsString, String dayMonthYearPattern) {
        return LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern(dayMonthYearPattern));
    }

    public static LocalDate toDate(String dateAsString) {
        return toDate(dateAsString, "yyyy-MM-dd");
    }
}
