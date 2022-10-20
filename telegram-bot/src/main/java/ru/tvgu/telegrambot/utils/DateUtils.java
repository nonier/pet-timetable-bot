package ru.tvgu.telegrambot.utils;

import lombok.experimental.UtilityClass;
import ru.tvgu.telegrambot.entity.Period;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@UtilityClass
public final class DateUtils {

    public static Period getWeekPeriodByDate(LocalDate localDate) {
        LocalDate firstSeptember = LocalDate.of(localDate.getYear(), 9, 1);
        return ChronoUnit.WEEKS.between(firstSeptember, localDate) % 2 == 0 ? Period.PLUS
                : Period.MINUS;
    }
}