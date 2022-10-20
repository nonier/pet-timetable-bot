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
import ru.tvgu.telegrambot.repository.GroupRepository;
import ru.tvgu.telegrambot.repository.TelegramUserRepository;
import ru.tvgu.telegrambot.service.ProcessUpdateService;
import ru.tvgu.telegrambot.service.SendMessageService;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ProcessUpdateServiceImpl implements ProcessUpdateService {

    private final FacultyRepository facultyRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final GroupRepository groupRepository;
    private final SendMessageService sendMessageService;

    @Autowired
    public ProcessUpdateServiceImpl(FacultyRepository facultyRepository, TelegramUserRepository telegramUserRepository, GroupRepository groupRepository, SendMessageService sendMessageService) {
        this.facultyRepository = facultyRepository;
        this.telegramUserRepository = telegramUserRepository;
        this.groupRepository = groupRepository;
        this.sendMessageService = sendMessageService;
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
                List<String> groupButtons = groupRepository.findAllByFaculty(faculty)
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
        }
        telegramUserRepository.save(telegramUser);
    }
}