package ru.tvgu.telegrambot.service;

import java.util.List;

public interface SendMessageService {

    void sendMessage(Long chatId, String text, List<String> buttons);

    void sendMessage(Long chatId, String text);
}