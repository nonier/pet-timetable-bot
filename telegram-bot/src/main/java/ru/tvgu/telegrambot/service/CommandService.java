package ru.tvgu.telegrambot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandService {

    void processAdminCommand(String command, Update update, Long userId);

    void processUserCommand(String command, Update update, Long userId);
}
