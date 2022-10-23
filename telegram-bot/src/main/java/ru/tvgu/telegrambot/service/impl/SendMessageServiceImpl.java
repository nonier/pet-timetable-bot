package ru.tvgu.telegrambot.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tvgu.telegrambot.bot.SimpleTelegramBot;
import ru.tvgu.telegrambot.service.SendMessageService;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SendMessageServiceImpl implements SendMessageService {

    private final SimpleTelegramBot telegramBot;

    @Autowired
    public SendMessageServiceImpl(@Lazy SimpleTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    @SneakyThrows
    public void sendMessage(Long chatId, String text, List<String> buttonsText) {
        List<InlineKeyboardButton> inlineKeyboardButtons = buttonsText.stream()
                .map(o -> InlineKeyboardButton.builder()
                        .text(o)
                        .callbackData(o)
                        .build())
                .toList();
        telegramBot.execute(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .replyMarkup(new InlineKeyboardMarkup(List.of(inlineKeyboardButtons)))
                        .build()
        );
    }

    @Override
    @SneakyThrows
    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, Collections.emptyList());
    }
}