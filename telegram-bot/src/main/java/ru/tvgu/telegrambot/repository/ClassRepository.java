package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.Class;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
}