package ru.tvgu.telegrambot.bot;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
@Slf4j
@Component
@NoArgsConstructor
public class SimpleTelegramBot extends TelegramLongPollingBot {

    @Value("${application.bot-username}")
    private String botUsername;
    @Value("${application.bot-token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        execute(SendMessage.builder()
                .text(update.getMessage().getText())
                .chatId(update.getMessage().getChatId())
                .build());
    }
}