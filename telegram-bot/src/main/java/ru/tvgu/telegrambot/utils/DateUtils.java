package ru.tvgu.telegrambot.utils;

import lombok.experimental.UtilityClass;
import ru.tvgu.telegrambot.entity.WeekType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@UtilityClass
public final class DateUtils {

    public static WeekType getWeekPeriodByDate(LocalDate localDate) {
        LocalDate firstSeptember = LocalDate.of(localDate.getYear(), 9, 1);
        return ChronoUnit.WEEKS.between(firstSeptember, localDate) % 2 == 0 ? WeekType.PLUS
                : WeekType.MINUS;
    }
}