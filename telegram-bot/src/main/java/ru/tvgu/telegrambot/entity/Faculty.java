package ru.tvgu.telegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "faculty")
@ToString(exclude = "studyGroups")
@EqualsAndHashCode(exclude = "studyGroups")
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "faculty")
    private List<StudyGroup> studyGroups = new ArrayList<>();
}