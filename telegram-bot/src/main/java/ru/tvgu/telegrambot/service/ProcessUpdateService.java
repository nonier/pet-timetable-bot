package ru.tvgu.telegrambot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProcessUpdateService {

    void process(Update update);
}