package ru.tvgu.telegrambot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.TelegramUser;

import java.util.Optional;

public interface TelegramUserService {

    void fillUserInfo(TelegramUser telegramUser, Update update);

    Optional<TelegramUser> findById(Long userId);

    void deleteById(Long id);

    void deleteUserInfoById(Long userId);

    void addAdminRole(Long userId);
}