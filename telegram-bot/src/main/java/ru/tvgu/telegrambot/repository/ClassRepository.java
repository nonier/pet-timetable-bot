package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.StudyClass;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<StudyClass, Long> {

    @Query("""
            select studyClass
            from StudyClass studyClass
            join fetch studyClass.subject subject
            join fetch subject.studyGroup studyGroup
            join fetch studyGroup.faculty
            """)
    List<StudyClass> findAllFetchSubject();
}