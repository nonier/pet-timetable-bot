package ru.tvgu.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tvgu.telegrambot.entity.Group;
import ru.tvgu.telegrambot.service.TimetableService;

@Service
@Transactional
public class TimetableServiceImpl implements TimetableService {

    @Override
    public String getTimeTableForToday(Group group) {
        return null;
    }

    @Override
    public String getTimeTableForWeek(Group group) {
        return null;
    }
}