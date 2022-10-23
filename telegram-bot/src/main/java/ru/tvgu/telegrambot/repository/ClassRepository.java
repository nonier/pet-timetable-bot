package ru.tvgu.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tvgu.telegrambot.entity.Class;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    @Query("""
            select class
            from Class class
            join fetch class.subject
            """)
    List<Class> findAllFetchSubject();
}