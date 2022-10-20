package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.StudyGroup;
import ru.tvgu.telegrambot.entity.Subject;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("""
            select subject
            from Subject subject
            join fetch subject.classes
            where subject.studyGroup = :studyGroup
            """)
    List<Subject> findAllByStudyGroup(StudyGroup studyGroup);
}