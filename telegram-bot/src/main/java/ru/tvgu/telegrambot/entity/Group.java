package ru.tvgu.telegrambot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "group")
@ToString(exclude = "subjects")
@EqualsAndHashCode(exclude = "subjects")
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    private Faculty faculty;

    @OneToMany(mappedBy = "group")
    private List<Subject> subjects = new ArrayList<>();
}