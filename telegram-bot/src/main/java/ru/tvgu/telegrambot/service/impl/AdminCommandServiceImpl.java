package ru.tvgu.telegrambot.service.impl;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.service.AdminCommandService;
import ru.tvgu.telegrambot.service.SendMessageService;
import ru.tvgu.telegrambot.service.TelegramUserService;

@Service
public class AdminCommandServiceImpl implements AdminCommandService {

    private final TelegramUserService telegramUserService;
    private final SendMessageService sendMessageService;

    @Autowired
    public AdminCommandServiceImpl(TelegramUserService telegramUserService, SendMessageService sendMessageService) {
        this.telegramUserService = telegramUserService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processAdminCommand(String command, Update update) {
        try {
            String text = StringUtils.substringAfter(command, "/");
            switch (text) {
                case "grantAdminRole" -> grantAdminRole(text);
            }
        } catch (Exception exception) {
            String errorMessage = "При обработке команды что-то пошло не так: %s".formatted(exception.getMessage());
            sendMessageService.sendMessage(update.getMessage().getChatId(), errorMessage);
        }
    }

    private void grantAdminRole(String text) {
        Long userId = Long.valueOf(StringUtils.substringAfterLast(text, " "));
        telegramUserService.addAdminRole(userId);
    }
}
