package ru.tvgu.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.Faculty;
import ru.tvgu.telegrambot.entity.StudyGroup;
import ru.tvgu.telegrambot.entity.TelegramUser;
import ru.tvgu.telegrambot.repository.FacultyRepository;
import ru.tvgu.telegrambot.repository.StudyGroupRepository;
import ru.tvgu.telegrambot.repository.TelegramUserRepository;
import ru.tvgu.telegrambot.service.SendMessageService;
import ru.tvgu.telegrambot.service.TelegramUserService;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.tvgu.telegrambot.service.impl.TimetableServiceImpl.TIMETABLE_FOR_TODAY;
import static ru.tvgu.telegrambot.service.impl.TimetableServiceImpl.TIMETABLE_FOR_WEEK;

@Service
@Transactional
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;
    private final FacultyRepository facultyRepository;
    private final SendMessageService sendMessageService;
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository, FacultyRepository facultyRepository, SendMessageService sendMessageService, StudyGroupRepository studyGroupRepository) {
        this.telegramUserRepository = telegramUserRepository;
        this.facultyRepository = facultyRepository;
        this.sendMessageService = sendMessageService;
        this.studyGroupRepository = studyGroupRepository;
    }

    @Override
    public void fillUserInfo(TelegramUser telegramUser, Update update) {
        Long chatId = update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
        if (telegramUser.getFaculty() == null) {
            if (update.hasCallbackQuery()) {
                Faculty faculty = facultyRepository.findByName(update.getCallbackQuery().getData()).orElseThrow(() -> {
                    sendMessageService.sendMessage(chatId, "Факультета с именем: %s не найдено".formatted(update.getCallbackQuery().getData()), Collections.emptyList());
                    throw new EntityNotFoundException("Faculty with name: %s not found".formatted(update.getCallbackQuery().getData()));
                });
                telegramUser.setFaculty(faculty);
                telegramUser.setId(update.getCallbackQuery().getFrom().getId());
                telegramUserRepository.save(telegramUser);
                List<String> groupButtons = studyGroupRepository.findAllByFaculty(faculty).stream().map(StudyGroup::getName).toList();
                sendMessageService.sendMessage(chatId, "Выбери группу", groupButtons);
            } else {
                List<String> facultyButtons = facultyRepository.findAll().stream().map(Faculty::getName).toList();
                sendMessageService.sendMessage(chatId, "Выбери факультет", facultyButtons);
            }
            return;
        }
        if (telegramUser.getStudyGroup() == null) {
            if (update.hasCallbackQuery()) {
                StudyGroup studyGroup = studyGroupRepository.findByName(update.getCallbackQuery().getData()).orElseThrow(() -> {
                    sendMessageService.sendMessage(chatId, "Группы с именем: %s не найдено".formatted(update.getCallbackQuery().getData()), Collections.emptyList());
                    throw new EntityNotFoundException("Group with name: %s not found".formatted(update.getCallbackQuery().getData()));
                });
                telegramUser.setStudyGroup(studyGroup);
                telegramUserRepository.save(telegramUser);
                sendMessageService.sendMessage(chatId, "Выбири расписание", List.of(TIMETABLE_FOR_TODAY, TIMETABLE_FOR_WEEK));
            } else {
                List<String> groupButtons = studyGroupRepository.findAllByFaculty(telegramUser.getFaculty()).stream().map(StudyGroup::getName).toList();
                sendMessageService.sendMessage(chatId, "Выбери группу", groupButtons);
            }
        }
    }

    @Override
    public Optional<TelegramUser> findById(Long userId) {
        return telegramUserRepository.findById(userId);
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            telegramUserRepository.deleteById(id);
        }
    }
}