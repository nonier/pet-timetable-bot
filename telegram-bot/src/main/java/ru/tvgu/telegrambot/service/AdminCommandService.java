package ru.tvgu.telegrambot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdminCommandService {

    void processAdminCommand(String command, Update update);
}
