package ru.tvgu.telegrambot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "faculty")
@ToString(exclude = "groups")
@EqualsAndHashCode(exclude = "groups")
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "faculty")
    private List<Group> groups = new ArrayList<>();
}