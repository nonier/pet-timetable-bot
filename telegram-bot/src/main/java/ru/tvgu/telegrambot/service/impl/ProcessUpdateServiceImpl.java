package ru.tvgu.telegrambot.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.TelegramUser;
import ru.tvgu.telegrambot.service.ProcessUpdateService;
import ru.tvgu.telegrambot.service.TelegramUserService;
import ru.tvgu.telegrambot.service.TimetableService;

@Service
@Transactional
public class ProcessUpdateServiceImpl implements ProcessUpdateService {

    private static final String FORGET_ME_COMMAND = "/forget";

    private final TelegramUserService telegramUserService;
    private final TimetableService timetableService;

    @Autowired
    public ProcessUpdateServiceImpl(TelegramUserService telegramUserService, TimetableService timetableService) {
        this.telegramUserService = telegramUserService;
        this.timetableService = timetableService;
    }

    @Override
    @SneakyThrows
    public void process(Update update) {
        Long userId = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        if (update.hasMessage() && FORGET_ME_COMMAND.equals(update.getMessage().getText())) {
            telegramUserService.deleteUserInfoById(userId);
        }
        telegramUserService.findById(userId)
                .ifPresentOrElse(
                        telegramUser -> catchUserUpdate(telegramUser, update),
                        () -> catchUserUpdate(new TelegramUser(), update)
                );
    }

    @SneakyThrows
    private void catchUserUpdate(TelegramUser telegramUser, Update update) {
        telegramUserService.fillUserInfo(telegramUser, update);
        if (telegramUser.getFaculty() != null && telegramUser.getStudyGroup() != null) {
            timetableService.getTimeTable(telegramUser, update);
        }
    }
}