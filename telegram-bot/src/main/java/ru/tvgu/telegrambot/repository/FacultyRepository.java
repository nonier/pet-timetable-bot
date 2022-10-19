package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}