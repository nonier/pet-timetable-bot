package ru.tvgu.telegrambot.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.service.ProcessUpdateService;

@Slf4j
@Component
public class SimpleTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final ProcessUpdateService processUpdateServices;

    @Autowired
    public SimpleTelegramBot(@Value("${application.bot-username}") String botUsername,
                             @Value("${application.bot-token}") String botToken,
                             ProcessUpdateService processUpdateServices) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.processUpdateServices = processUpdateServices;
    }

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
        processUpdateServices.process(update);
    }
}