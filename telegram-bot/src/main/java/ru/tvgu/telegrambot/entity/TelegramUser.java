package ru.tvgu.telegrambot.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "telegram_user")
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", referencedColumnName = "id")
    private StudyGroup studyGroup;

    @Column(name = "is_admin", nullable = false, columnDefinition = "boolean default false")
    private boolean isAdmin;
}