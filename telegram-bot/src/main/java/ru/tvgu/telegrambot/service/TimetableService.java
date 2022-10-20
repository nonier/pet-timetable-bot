package ru.tvgu.telegrambot.service;

import ru.tvgu.telegrambot.entity.StudyGroup;

public interface TimetableService {

    String getTimeTableForToday(StudyGroup studyGroup);

    String getTimeTableForWeek(StudyGroup studyGroup);
}