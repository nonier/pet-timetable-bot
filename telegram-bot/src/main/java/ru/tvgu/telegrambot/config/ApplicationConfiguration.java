package ru.tvgu.telegrambot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tvgu.telegrambot.entity.TelegramUser;
import ru.tvgu.telegrambot.repository.TelegramUserRepository;

@Configuration
public class ApplicationConfiguration {

    private Long botOwnerId;

    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public ApplicationConfiguration(@Value("${application.bot-owner-id}") Long botOwnerId, TelegramUserRepository telegramUserRepository) {
        this.botOwnerId = botOwnerId;
        this.telegramUserRepository = telegramUserRepository;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            if (telegramUserRepository.findById(botOwnerId).isEmpty()) {
                TelegramUser botOwner = new TelegramUser(botOwnerId, null, null, true);
                telegramUserRepository.save(botOwner);
            }
        };
    }
}