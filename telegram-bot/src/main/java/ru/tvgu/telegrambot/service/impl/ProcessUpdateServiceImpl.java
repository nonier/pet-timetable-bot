package ru.tvgu.telegrambot.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.TelegramUser;
import ru.tvgu.telegrambot.service.CommandService;
import ru.tvgu.telegrambot.service.ProcessUpdateService;
import ru.tvgu.telegrambot.service.TelegramUserService;
import ru.tvgu.telegrambot.service.TimetableService;

@Service
@Transactional
public class ProcessUpdateServiceImpl implements ProcessUpdateService {

    private final TelegramUserService telegramUserService;
    private final TimetableService timetableService;
    private final CommandService commandService;

    @Autowired
    public ProcessUpdateServiceImpl(TelegramUserService telegramUserService, TimetableService timetableService, CommandService commandService) {
        this.telegramUserService = telegramUserService;
        this.timetableService = timetableService;
        this.commandService = commandService;
    }

    @Override
    @SneakyThrows
    public void process(Update update) {
        Long userId = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        telegramUserService.findById(userId)
                .ifPresentOrElse(
                        telegramUser -> catchUserUpdate(telegramUser, update),
                        () -> catchUserUpdate(new TelegramUser(), update)
                );
    }

    @SneakyThrows
    private void catchUserUpdate(TelegramUser telegramUser, Update update) {
        if (update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().contains("/")) {
            if (telegramUser.isAdmin()) {
                commandService.processAdminCommand(update.getMessage().getText(), update, telegramUser.getId());
            } else {
                commandService.processUserCommand(update.getMessage().getText(), update, telegramUser.getId());
            }
            return;
        }
        telegramUserService.fillUserInfo(telegramUser, update);
        if (telegramUser.getFaculty() != null && telegramUser.getStudyGroup() != null) {
            timetableService.getTimeTable(telegramUser, update);
        }
    }
}