package ru.tvgu.telegrambot.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.bot.SimpleTelegramBot;
import ru.tvgu.telegrambot.service.ProcessUpdateService;

@Component
public class ProcessUpdateServiceImpl implements ProcessUpdateService {

    private final SimpleTelegramBot simpleTelegramBot;

    @Autowired
    public ProcessUpdateServiceImpl(@Lazy SimpleTelegramBot simpleTelegramBot) {
        this.simpleTelegramBot = simpleTelegramBot;
    }

    @Override
    @SneakyThrows
    public void process(Update update) {
        simpleTelegramBot.execute(SendMessage.builder()
                .text(update.getMessage().getText())
                .chatId(update.getMessage().getChatId())
                .build());
    }
}