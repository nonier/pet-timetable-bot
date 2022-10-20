package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.Faculty;
import ru.tvgu.telegrambot.entity.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByFaculty(Faculty faculty);

    Optional<Group> findByName(String name);
}