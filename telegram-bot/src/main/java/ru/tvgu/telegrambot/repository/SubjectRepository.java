package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}