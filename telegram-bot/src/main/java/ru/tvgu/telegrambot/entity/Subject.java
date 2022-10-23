package ru.tvgu.telegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "subject")
@ToString(exclude = "studyClasses")
@EqualsAndHashCode(exclude = "studyClasses")
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "teacher", nullable = false)
    private String teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", referencedColumnName = "id")
    private StudyGroup studyGroup;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    private List<StudyClass> studyClasses = new ArrayList<>();
}