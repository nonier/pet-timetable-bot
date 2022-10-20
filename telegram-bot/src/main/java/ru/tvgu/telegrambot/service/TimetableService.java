package ru.tvgu.telegrambot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.StudyGroup;
import ru.tvgu.telegrambot.entity.TelegramUser;

public interface TimetableService {

    String getTimeTableForToday(StudyGroup studyGroup);

    String getTimeTableForWeek(StudyGroup studyGroup);

    void getTimeTable(TelegramUser telegramUser, Update update);
}