package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.TelegramUser;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    @Query("""
            update TelegramUser user
            set user.studyGroup = null, user.faculty = null 
            where user.id = :userId
            """)
    @Modifying
    void deleteUserInfoById(Long userId);
}