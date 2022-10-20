package ru.tvgu.telegrambot.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.Faculty;
import ru.tvgu.telegrambot.entity.Group;
import ru.tvgu.telegrambot.entity.TelegramUser;
import ru.tvgu.telegrambot.repository.FacultyRepository;
import ru.tvgu.telegrambot.repository.StudyGroupRepository;
import ru.tvgu.telegrambot.repository.TelegramUserRepository;
import ru.tvgu.telegrambot.service.ProcessUpdateService;
import ru.tvgu.telegrambot.service.SendMessageService;
import ru.tvgu.telegrambot.service.TimetableService;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ProcessUpdateServiceImpl implements ProcessUpdateService {

    private final FacultyRepository facultyRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final SendMessageService sendMessageService;
    private final TimetableService timetableService;

    @Autowired
    public ProcessUpdateServiceImpl(FacultyRepository facultyRepository, TelegramUserRepository telegramUserRepository, StudyGroupRepository studyGroupRepository, SendMessageService sendMessageService, TimetableService timetableService) {
        this.facultyRepository = facultyRepository;
        this.telegramUserRepository = telegramUserRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.sendMessageService = sendMessageService;
        this.timetableService = timetableService;
    }

    @Override
    @SneakyThrows
    public void process(Update update) {
        Long userId = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        telegramUserRepository.findById(userId)
                .ifPresentOrElse(
                        telegramUser -> checkUserInfo(telegramUser, update),
                        () -> checkUserInfo(new TelegramUser(), update)
                );
    }

    @SneakyThrows
    private void checkUserInfo(TelegramUser telegramUser, Update update) {
        Long chatId = update.hasCallbackQuery() ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
        if (telegramUser.getFaculty() == null) {
            if (update.hasCallbackQuery()) {
                Faculty faculty = facultyRepository.findByName(update.getCallbackQuery().getData())
                        .orElseThrow(() -> {
                            sendMessageService.sendMessage(chatId,
                                    "Факультета с именем: %s не найдено".formatted(update.getCallbackQuery().getData()),
                                    Collections.emptyList());
                            throw new EntityNotFoundException("Faculty with name: %s not found"
                                    .formatted(update.getCallbackQuery().getData()));
                        });
                telegramUser.setFaculty(faculty);
                telegramUser.setId(update.getCallbackQuery().getFrom().getId());
                telegramUserRepository.save(telegramUser);
                List<String> groupButtons = studyGroupRepository.findAllByFaculty(faculty)
                        .stream()
                        .map(Group::getName)
                        .toList();
                sendMessageService.sendMessage(chatId, "Выбери группу", groupButtons);
            } else {
                List<String> facultyButtons = facultyRepository.findAll()
                        .stream()
                        .map(Faculty::getName)
                        .toList();
                sendMessageService.sendMessage(chatId, "Выбери факультет", facultyButtons);
            }
            return;
        }
        if (telegramUser.getGroup() == null) {
            if (update.hasCallbackQuery()) {
                Group group = studyGroupRepository.findByName(update.getCallbackQuery().getData())
                        .orElseThrow(
                                () -> {
                                    sendMessageService.sendMessage(chatId,
                                            "Группы с именем: %s не найдено".formatted(update.getCallbackQuery().getData()),
                                            Collections.emptyList());
                                    throw new EntityNotFoundException("Group with name: %s not found"
                                            .formatted(update.getCallbackQuery().getData()));
                                }
                        );
                telegramUser.setGroup(group);
                telegramUserRepository.save(telegramUser);
                sendMessageService.sendMessage(chatId, "Выбирай",
                        List.of("Расписание на сегодня", "Расписание на неделю"));
            } else {
                List<String> groupButtons = studyGroupRepository.findAllByFaculty(telegramUser.getFaculty())
                        .stream()
                        .map(Group::getName)
                        .toList();
                sendMessageService.sendMessage(chatId, "Выбери группу", groupButtons);
            }
            return;
        }
        if (update.hasCallbackQuery()) {
            if ("Расписание на сегодня".equals(update.getCallbackQuery().getData())) {
                sendMessageService.sendMessage(chatId, timetableService.getTimeTableForToday(telegramUser.getGroup()),
                        Collections.emptyList());
            }
            if ("Расписание на неделю".equals(update.getCallbackQuery().getData())) {
                sendMessageService.sendMessage(chatId, timetableService.getTimeTableForWeek(telegramUser.getGroup()),
                        Collections.emptyList());
            }
        } else {
            sendMessageService.sendMessage(chatId, "Выбирай",
                    List.of("Расписание на сегодня", "Расписание на неделю"));
        }
    }
}