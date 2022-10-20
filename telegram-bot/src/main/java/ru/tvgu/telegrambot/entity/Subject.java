package ru.tvgu.telegrambot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "subject")
@ToString(exclude = "classes")
@EqualsAndHashCode(exclude = "classes")
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "teacher", nullable = false)
    private String teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", referencedColumnName = "id")
    private Group group;

    @OneToMany(mappedBy = "subject")
    private List<Class> classes = new ArrayList<>();
}