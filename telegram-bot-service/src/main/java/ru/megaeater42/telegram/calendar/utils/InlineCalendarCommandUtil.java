package ru.megaeater42.telegram.calendar.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static ru.megaeater42.telegram.calendar.InlineCalendar.*;

public class InlineCalendarCommandUtil {
    public static boolean checkCallbackQueryData(Update update) {
        return update.hasCallbackQuery() && (update.getCallbackQuery().getData() != null);
    }

    public static boolean isClicked(Update update) {
        return checkCallbackQueryData(update) && update.getCallbackQuery().getData().startsWith(INLINE_CALENDAR_PREFIX);
    }

    public static boolean isIgnoreButtonClicked(Update update) {
        return checkCallbackQueryData(update) && update.getCallbackQuery().getData().equals(IGNORE_COMMAND);
    }

    public static boolean isInfoButtonClicked(Update update) {
        return checkCallbackQueryData(update) && update.getCallbackQuery().getData().startsWith(BASE_INFO_COMMAND);
    }

    public static LocalDate extractNavigationDate(Update update, String dayMonthYearPattern) {
        if (!isInfoButtonClicked(update)) {
            return null;
        }

        try {
            return DateUtil.toDate(update.getCallbackQuery().getData().replaceFirst(BASE_INFO_COMMAND, "").substring(0, dayMonthYearPattern.length()), dayMonthYearPattern);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static boolean isDateSelected(Update update, String dayMonthYearPattern) {
        if (checkCallbackQueryData(update)) {
            try {
                DateUtil.toDate(update.getCallbackQuery().getData().replaceFirst(DATE_COMMAND, ""), dayMonthYearPattern);
                return true;
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    public static LocalDate extractDate(Update update, String dayMonthYearPattern) {
        return DateUtil.toDate(update.getCallbackQuery().getData().replaceFirst(DATE_COMMAND, ""), dayMonthYearPattern);
    }

    public static boolean wasRanged(Update update, String dayMonthYearPattern) {
        try {
            return update.getCallbackQuery().getData().replace(BASE_INFO_COMMAND, "").substring(dayMonthYearPattern.length() + 1).startsWith(PIVOT_PREFIX);
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static LocalDate extractInfoPivotDate(Update update, String dayMonthYearPattern) {
        try {
            String mightBePivotDateString = update.getCallbackQuery().getData().replace(BASE_INFO_COMMAND, "").substring(dayMonthYearPattern.length() + 1).replaceFirst(PIVOT_PREFIX, "");
            return DateUtil.toDate(mightBePivotDateString.substring(0, dayMonthYearPattern.length()), dayMonthYearPattern);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static boolean isRangeClicked(Update update, String dayMonthYearPattern) {
        if (checkCallbackQueryData(update)) {
            try {
                String mightBeRangeStartString = update.getCallbackQuery().getData().replaceFirst(RANGE_COMMAND, "");
                DateUtil.toDate(mightBeRangeStartString.substring(0, dayMonthYearPattern.length()), dayMonthYearPattern);
                return mightBeRangeStartString.substring(dayMonthYearPattern.length()).startsWith(":");
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    public static LocalDate extractPivotDate(Update update, String dayMonthYearPattern) {
        try {
            return DateUtil.toDate(update.getCallbackQuery().getData().replace(RANGE_COMMAND, "").substring(0, dayMonthYearPattern.length()), dayMonthYearPattern);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static boolean isRangeSelected(Update update, String dayMonthYearPattern) {
        if (isRangeClicked(update, dayMonthYearPattern)) {
            try {
                String mightBeLastRangeDateString = update.getCallbackQuery().getData().substring(RANGE_COMMAND.length() + dayMonthYearPattern.length() + 1);
                DateUtil.toDate(mightBeLastRangeDateString, dayMonthYearPattern);
                return true;
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    // Really looking forward for Java-core implementation of the Pair<F, S>, until that... parse yourself
    public static String extractRange(Update update, String dayMonthYearPattern) {
        return update.getCallbackQuery().getData().replace(RANGE_COMMAND, "").substring(0, (2 * dayMonthYearPattern.length()) + 1);
    }

    public static LocalDate extractNavigationDate(Update update) {
        return extractNavigationDate(update, "yyyy-MM-dd");
    }

    public static boolean isDateSelected(Update update) {
        return isDateSelected(update, "yyyy-MM-dd");
    }

    public static LocalDate extractDate(Update update) {
        return extractDate(update, "yyyy-MM-dd");
    }

    public static boolean wasRanged(Update update) {
        return wasRanged(update, "yyyy-MM-dd");
    }

    public static LocalDate extractInfoPivotDate(Update update) {
        return extractInfoPivotDate(update, "yyyy-MM-dd");
    }

    public static boolean isRangeClicked(Update update) {
        return isRangeClicked(update, "yyyy-MM-dd");
    }

    public static LocalDate extractPivotDate(Update update) {
        return extractPivotDate(update, "yyyy-MM-dd");
    }

    public static boolean isRangeSelected(Update update) {
        return isRangeSelected(update, "yyyy-MM-dd");
    }

    public static String extractRange(Update update) {
        return extractRange(update, "yyyy-MM-dd");
    }
}