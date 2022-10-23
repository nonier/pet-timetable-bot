package ru.tvgu.telegrambot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tvgu.telegrambot.entity.Faculty;
import ru.tvgu.telegrambot.entity.StudyClass;
import ru.tvgu.telegrambot.entity.StudyGroup;
import ru.tvgu.telegrambot.entity.Subject;
import ru.tvgu.telegrambot.repository.ClassRepository;
import ru.tvgu.telegrambot.repository.FacultyRepository;
import ru.tvgu.telegrambot.repository.StudyGroupRepository;
import ru.tvgu.telegrambot.repository.SubjectRepository;
import ru.tvgu.telegrambot.service.CommandService;
import ru.tvgu.telegrambot.service.SendMessageService;
import ru.tvgu.telegrambot.service.TelegramUserService;

@Service
@Transactional
public class CommandServiceImpl implements CommandService {

    private final TelegramUserService telegramUserService;
    private final SendMessageService sendMessageService;
    private final FacultyRepository facultyRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommandServiceImpl(TelegramUserService telegramUserService, SendMessageService sendMessageService, FacultyRepository facultyRepository, StudyGroupRepository studyGroupRepository, SubjectRepository subjectRepository, ClassRepository classRepository, ObjectMapper objectMapper) {
        this.telegramUserService = telegramUserService;
        this.sendMessageService = sendMessageService;
        this.facultyRepository = facultyRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processAdminCommand(String command, Update update, Long userId) {
        try {
            String text = StringUtils.substringBetween(update.getMessage().getText(),"/"," ") != null ?
                    StringUtils.substringBetween(update.getMessage().getText(),"/"," ")
                    : StringUtils.substringAfter(update.getMessage().getText(),"/");
            switch (text) {
                case "start" -> sendStart(update);
                case "help" -> sendAdminHelp(update);
                case "getGroups" -> getGroups(update);
                case "getFaculties" -> getFaculties(update);
                case "getSubjects" -> getSubjects(update);
                case "getClasses" -> getClasses(update);
                case "createFaculty" -> createFaculty(command);
                case "createGroup" -> createGroup(command);
                case "createSubject" -> createSubject(command);
                case "createClass" -> createClass(command);
                case "grantAdminRole" -> grantAdminRole(command);
                case "forget" -> telegramUserService.deleteUserInfoById(userId);
                default -> sendMessageService.sendMessage(update.getMessage().getChatId(), "Неподдерживаемая команда");
            }
        } catch (Exception exception) {
            String errorMessage = "При обработке команды что-то пошло не так: %s".formatted(exception.getMessage());
            sendMessageService.sendMessage(update.getMessage().getChatId(), errorMessage);
        }
    }

    @Override
    public void processUserCommand(String command, Update update, Long userId) {
        String text = StringUtils.substringAfter(command, "/");
        switch (text) {
            case "start" -> sendStart(update);
            case "help" -> sendUserHelp(update);
            case "forget" -> telegramUserService.deleteUserInfoById(userId);
            default -> sendMessageService.sendMessage(update.getMessage().getChatId(), "Неподдерживаемая команда");
        }
    }

    private void sendStart(Update update) {
        String start = """
                привет!
                бот может показывать тебе твоё расписание, если оно конечно в нём есть.
                """;
        sendMessageService.sendMessage(update.getMessage().getChatId(), start);
    }

    private void sendAdminHelp(Update update) {
        String help = """
                Бот покажет расписание для твоей группы, если оно конечно в нём есть.
                Всё просто, выбирай из предложенного в списке.
                Доступные команды:
                /forget - забывает выбранный факультет и группу, можно заполнить заного
                /grantAdminRole {id} - добавляет юзеру роль админ по id
                /getFaculties - возвращает список факультетов в формате json
                /getGroups - возвращает список групп в формате json
                /getSubjects - возвращает список предметов в формате json
                /getClasses - возвращает список пар в формате json
                /createFaculty {faculty json} - создает факультет
                /createGroup {studyGroup json} - создает группу
                /createSubject {subject json} - создает предмет
                /createClass {class json} - создает пару
                """;
        sendMessageService.sendMessage(update.getMessage().getChatId(), help);
    }

    private void sendUserHelp(Update update) {
        String help = """
                Бот покажет расписание для твоей группы, если оно конечно в нём есть.
                Всё просто, выбирай из предложенного в списке.
                Доступные команды:
                /forget - забывает выбранный факультет и группу, можно заполнить заного
                """;
        sendMessageService.sendMessage(update.getMessage().getChatId(), help);
    }

    @SneakyThrows
    private void getClasses(Update update) {
        String classesJson = objectMapper.writeValueAsString(classRepository.findAllFetchSubject());
        sendMessageService.sendMessage(update.getMessage().getChatId(), classesJson);
    }

    @SneakyThrows
    private void getSubjects(Update update) {
        String subjectsJson = objectMapper.writeValueAsString(subjectRepository.findAllFetchStudyGroup());
        sendMessageService.sendMessage(update.getMessage().getChatId(), subjectsJson);
    }

    @SneakyThrows
    private void getGroups(Update update) {
        String studyGroupsJson = objectMapper.writeValueAsString(studyGroupRepository.findAllFetchFaculty());
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
        StudyClass clazz = objectMapper.readValue(json, StudyClass.class);
        classRepository.save(clazz);
    }

    @SneakyThrows
    private void createSubject(String text) {
        String json = StringUtils.substringAfter(text, " ");
        Subject subject = objectMapper.readValue(json, Subject.class);
        subjectRepository.save(subject);
    }

    @SneakyThrows
    private void createGroup(String text) {
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