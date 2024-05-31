package ru.megaeater42.telegram.calendar;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.megaeater42.telegram.calendar.translation.InlineCalendarTranslationRU;
import ru.megaeater42.telegram.calendar.utils.DateUtil;
import ru.megaeater42.telegram.calendar.utils.InlineCalendarCommandUtil;


import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineCalendar {
    public static final String INLINE_CALENDAR_PREFIX = "IC:";

    public static final String IGNORE_SUFFIX = "IGNORE";

    public static final String INFO_PREFIX = "INFO:";
    public static final String NAVIGATION_PREFIX = "NAVIGATE:";

    public static final String DATE_PREFIX = "DATE:";

    public static final String RANGE_PREFIX = "RANGE:";
    public static final String PIVOT_PREFIX = "PIVOT:";

    public static final String IGNORE_COMMAND = INLINE_CALENDAR_PREFIX + IGNORE_SUFFIX;
    public static final String BASE_INFO_COMMAND = INLINE_CALENDAR_PREFIX + INFO_PREFIX + NAVIGATION_PREFIX;
    public static final String DATE_COMMAND = INLINE_CALENDAR_PREFIX + DATE_PREFIX;
    public static final String RANGE_COMMAND = INLINE_CALENDAR_PREFIX + RANGE_PREFIX;

    public static InlineCalendarBuilder builder() {
        return new InlineCalendarBuilder();
    }

    public static class InlineCalendarBuilder {
        private String[] weekDays = InlineCalendarTranslationRU.WEEK_DAYS;
        private Map<Month, String> months = InlineCalendarTranslationRU.MONTHS;
        private String dayMonthYearPattern = "yyyy-MM-dd";

        private LocalDate leftBound = LocalDate.MIN;
        private LocalDate rightBound = LocalDate.MAX;

        private boolean ranged = false;
        private LocalDate pivotDate = null;

        public InlineCalendarBuilder weekDays(String[] weekDays) {
            this.weekDays = weekDays;
            return this;
        }

        public InlineCalendarBuilder months(Map<Month, String> months) {
            this.months = months;
            return this;
        }

        public InlineCalendarBuilder dayMonthYearPattern(String dayMonthYearPattern) {
            this.dayMonthYearPattern = dayMonthYearPattern;
            return this;
        }

        public InlineCalendarBuilder leftBound(LocalDate leftBound) {
            this.leftBound = leftBound;
            return this;
        }

        public InlineCalendarBuilder rightBound(LocalDate rightBound) {
            this.rightBound = rightBound;
            return this;
        }

        public InlineCalendarBuilder ranged(boolean ranged) {
            this.ranged = ranged;
            return this;
        }

        public InlineCalendarBuilder pivotDate(LocalDate pivotDate) {
            this.pivotDate = pivotDate;
            return this;
        }

        public synchronized InlineKeyboardMarkup build(Update update) {
            LocalDate dateForCalendar = InlineCalendarCommandUtil.extractNavigationDate(update);

            if (dateForCalendar == null) {
                dateForCalendar = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
            }

            String rangeStartString = RANGE_COMMAND;
            String infoString = ";";
            if (ranged) {
                infoString += PIVOT_PREFIX;
                if (pivotDate != null) {
                    rangeStartString += pivotDate + ":";
                    infoString += pivotDate;
                }
            }
            String rangeSplitter = ranged && (pivotDate == null)? ":" : "";

            Period leftConstraints = Period.between(leftBound, dateForCalendar);
            Period rightConstraints = Period.between(dateForCalendar, rightBound);
            boolean shouldDisableLeftYearButton = leftConstraints.getYears() < 1;
            boolean shouldDisableRightYearButton = rightConstraints.getYears() < 1;
            boolean shouldDisableLeftMonthButton = shouldDisableLeftYearButton && (leftConstraints.getMonths() < 1);
            boolean shouldDisableRightMonthButton = shouldDisableRightYearButton && (rightConstraints.getMonths() < 1);

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

            // Week days
            for (String weekDay : this.weekDays) {
                inlineKeyboardButtons.add(
                        InlineKeyboardButton.builder()
                                .text(weekDay)
                                .callbackData(IGNORE_COMMAND)
                                .build()
                );
            }

            rows.add(inlineKeyboardButtons);

            // Optional empty buttons
            inlineKeyboardButtons = new ArrayList<>();
            int weekDaysCounter = LocalDate.of(dateForCalendar.getYear(), dateForCalendar.getMonth(), 1).getDayOfWeek().getValue() - 1;
            for (int i = 0; i < weekDaysCounter; i++) {
                inlineKeyboardButtons.add(
                        InlineKeyboardButton.builder()
                                .text(" ")
                                .callbackData(IGNORE_COMMAND)
                                .build()
                );
            }

            // Month days
            int daysOfCurrentMonth = YearMonth.of(dateForCalendar.getYear(), dateForCalendar.getMonth()).lengthOfMonth();
            int remainingEmptyDays = 0;
            for (int i = 1; i <= daysOfCurrentMonth; i++) {
                boolean shouldDisableDay = (shouldDisableLeftMonthButton && (i <= leftBound.getDayOfMonth())) || (shouldDisableRightMonthButton && (i >= rightBound.getDayOfMonth()));
                inlineKeyboardButtons.add(
                        InlineKeyboardButton.builder()
                                .text(shouldDisableDay? "x" : String.valueOf(i))
                                .callbackData(shouldDisableDay? IGNORE_COMMAND : (ranged? rangeStartString : DATE_COMMAND) + DateUtil.toString(LocalDate.of(dateForCalendar.getYear(), dateForCalendar.getMonth(), i), dayMonthYearPattern) + rangeSplitter)
                                .build()
                );
                weekDaysCounter += 1;

                if (weekDaysCounter == 7) {
                    rows.add(inlineKeyboardButtons);
                    inlineKeyboardButtons = new ArrayList<>();
                    weekDaysCounter = 0;
                } else {
                    if (i == daysOfCurrentMonth) {
                        remainingEmptyDays = 7 - weekDaysCounter;
                    }
                }
            }

            // Optional empty buttons
            for (int i = 0; i < remainingEmptyDays; i++) {
                inlineKeyboardButtons.add(
                        InlineKeyboardButton.builder()
                                .text(" ")
                                .callbackData(IGNORE_COMMAND)
                                .build()
                );
            }

            if (remainingEmptyDays > 0) {
                rows.add(inlineKeyboardButtons);
            }

            // Navigation bar
            // Month
            rows.add(
                    List.of(
                            InlineKeyboardButton.builder()
                                    .text(shouldDisableLeftMonthButton? "x" : "<<")
                                    .callbackData(shouldDisableLeftMonthButton? IGNORE_COMMAND : BASE_INFO_COMMAND + DateUtil.toString(dateForCalendar.minusMonths(1), dayMonthYearPattern) + infoString)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(this.months.get(dateForCalendar.getMonth()))
                                    .callbackData(IGNORE_COMMAND)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(shouldDisableRightMonthButton? "x" : ">>")
                                    .callbackData(shouldDisableRightMonthButton? IGNORE_COMMAND : BASE_INFO_COMMAND + DateUtil.toString(dateForCalendar.plusMonths(1), dayMonthYearPattern) + infoString)
                                    .build()
                    )
            );

            // Year
            rows.add(
                    List.of(
                            InlineKeyboardButton.builder()
                                    .text(shouldDisableLeftYearButton? "x" : "<<")
                                    .callbackData(shouldDisableLeftYearButton? IGNORE_COMMAND : BASE_INFO_COMMAND + DateUtil.toString(dateForCalendar.minusYears(1), dayMonthYearPattern) + infoString)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(String.valueOf(dateForCalendar.getYear()))
                                    .callbackData(IGNORE_COMMAND)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(shouldDisableRightYearButton? "x" : ">>")
                                    .callbackData(shouldDisableRightYearButton? IGNORE_COMMAND : BASE_INFO_COMMAND + DateUtil.toString(dateForCalendar.plusYears(1), dayMonthYearPattern) + infoString)
                                    .build()
                    )
            );

            inlineKeyboardMarkup.setKeyboard(rows);

            return inlineKeyboardMarkup;
        }
    }
}