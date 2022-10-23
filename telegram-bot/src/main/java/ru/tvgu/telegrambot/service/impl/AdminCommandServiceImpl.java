package ru.tvgu.telegrambot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.Class;
import ru.tvgu.telegrambot.entity.Faculty;
import ru.tvgu.telegrambot.entity.StudyGroup;
import ru.tvgu.telegrambot.entity.Subject;
import ru.tvgu.telegrambot.repository.ClassRepository;
import ru.tvgu.telegrambot.repository.FacultyRepository;
import ru.tvgu.telegrambot.repository.StudyGroupRepository;
import ru.tvgu.telegrambot.repository.SubjectRepository;
import ru.tvgu.telegrambot.service.AdminCommandService;
import ru.tvgu.telegrambot.service.SendMessageService;
import ru.tvgu.telegrambot.service.TelegramUserService;

@Service
public class AdminCommandServiceImpl implements AdminCommandService {

    private final TelegramUserService telegramUserService;
    private final SendMessageService sendMessageService;
    private final FacultyRepository facultyRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public AdminCommandServiceImpl(TelegramUserService telegramUserService, SendMessageService sendMessageService, FacultyRepository facultyRepository, StudyGroupRepository studyGroupRepository, SubjectRepository subjectRepository, ClassRepository classRepository, ObjectMapper objectMapper) {
        this.telegramUserService = telegramUserService;
        this.sendMessageService = sendMessageService;
        this.facultyRepository = facultyRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processAdminCommand(String command, Update update) {
        try {
            String text = StringUtils.substringBetween(update.getMessage().getText(),"/"," ") != null ?
                    StringUtils.substringBetween(update.getMessage().getText(),"/"," ")
                    : StringUtils.substringAfter(update.getMessage().getText(),"/");
            switch (text) {
                case "getStudyGroups" -> getStudyGroups(update);
                case "getFaculties" -> getFaculties(update);
                case "getSubjects" -> getSubjects(update);
                case "getClasses" -> getClasses(update);
                case "createFaculty" -> createFaculty(command);
                case "createStudyGroup" -> createStudyGroup(command);
                case "createSubject" -> createSubject(command);
                case "grantAdminRole" -> grantAdminRole(command);
                case "createClass" -> createClass(command);
                default -> sendMessageService.sendMessage(update.getMessage().getChatId(), "Неподдерживаемая команда");
            }
        } catch (Exception exception) {
            String errorMessage = "При обработке команды что-то пошло не так: %s".formatted(exception.getMessage());
            sendMessageService.sendMessage(update.getMessage().getChatId(), errorMessage);
        }
    }

    @SneakyThrows
    private void getClasses(Update update) {
        String classesJson = objectMapper.writeValueAsString(classRepository.findAll());
        sendMessageService.sendMessage(update.getMessage().getChatId(), classesJson);
    }

    @SneakyThrows
    private void getSubjects(Update update) {
        String subjectsJson = objectMapper.writeValueAsString(subjectRepository.findAll());
        sendMessageService.sendMessage(update.getMessage().getChatId(), subjectsJson);
    }

    @SneakyThrows
    private void getStudyGroups(Update update) {
        String studyGroupsJson = objectMapper.writeValueAsString(studyGroupRepository.findAll());
        sendMessageService.sendMessage(update.getMessage().getChatId(), studyGroupsJson);
    }

    @SneakyThrows
    private void getFaculties(Update update) {
        String facultiesJson = objectMapper.writeValueAsString(facultyRepository.findAll());
        sendMessageService.sendMessage(update.getMessage().getChatId(), facultiesJson);
    }

    @SneakyThrows
    private void createClass(String text) {
        String json = StringUtils.substringAfter(text, " ");
        Class clazz = objectMapper.readValue(json, Class.class);
        classRepository.save(clazz);
    }

    @SneakyThrows
    private void createSubject(String text) {
        String json = StringUtils.substringAfter(text, " ");
        Subject subject = objectMapper.readValue(json, Subject.class);
        subjectRepository.save(subject);
    }

    @SneakyThrows
    private void createStudyGroup(String text) {
        String json = StringUtils.substringAfter(text, " ");
        StudyGroup studyGroup = objectMapper.readValue(json, StudyGroup.class);
        studyGroupRepository.save(studyGroup);
    }

    @SneakyThrows
    private void createFaculty(String text) {
        String json = StringUtils.substringAfter(text, " ");
        Faculty faculty = objectMapper.readValue(json, Faculty.class);
        facultyRepository.save(faculty);
    }

    private void grantAdminRole(String text) {
        Long userId = Long.valueOf(StringUtils.substringAfter(text, " "));
        telegramUserService.addAdminRole(userId);
    }
}