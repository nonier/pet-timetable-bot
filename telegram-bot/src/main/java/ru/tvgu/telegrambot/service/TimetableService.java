package ru.tvgu.telegrambot.service;

import ru.tvgu.telegrambot.entity.Group;

public interface TimetableService {

    String getTimeTableForToday(Group group);

    String getTimeTableForWeek(Group group);
}