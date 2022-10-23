package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.Faculty;
import ru.tvgu.telegrambot.entity.StudyGroup;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    List<StudyGroup> findAllByFaculty(Faculty faculty);

    Optional<StudyGroup> findByName(String name);

    @Query("""
            select studyGroup
            from StudyGroup studyGroup
            join fetch studyGroup.faculty
            """)
    List<StudyGroup> findAllFetchFaculty();
}