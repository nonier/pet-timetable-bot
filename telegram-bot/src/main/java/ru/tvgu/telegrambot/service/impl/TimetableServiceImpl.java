package ru.tvgu.telegrambot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tvgu.telegrambot.entity.Class;
import ru.tvgu.telegrambot.entity.StudyGroup;
import ru.tvgu.telegrambot.entity.Period;
import ru.tvgu.telegrambot.repository.ClassRepository;
import ru.tvgu.telegrambot.repository.SubjectRepository;
import ru.tvgu.telegrambot.service.TimetableService;
import ru.tvgu.telegrambot.utils.DateUtils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimetableServiceImpl implements TimetableService {

    public static final String TIMETABLE_FOR_TODAY = "Расписание на сегодня";
    public static final String TIMETABLE_FOR_WEEK = "Расписание на неделю";

    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public TimetableServiceImpl(ClassRepository classRepository, SubjectRepository subjectRepository) {
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
    }


    @Override
    public String getTimeTableForToday(StudyGroup studyGroup) {
        LocalDate now = LocalDate.now();
        Period period = DateUtils.getWeekPeriodByDate(now);
        return subjectRepository.findAllByStudyGroup(studyGroup)
                .stream()
                .flatMap(subject -> subject.getClasses().stream())
                .filter(cls -> Objects.equals(period, cls.getPeriod())
                        || Period.EVERY_WEEK.equals(cls.getPeriod()))
                .filter(cls -> Objects.equals(now.getDayOfWeek(), cls.getDayOfWeek()))
                .distinct()
                .map(this::parseClass)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public String getTimeTableForWeek(StudyGroup studyGroup) {
        LocalDate now = LocalDate.now();
        Period period = DateUtils.getWeekPeriodByDate(now);
        return subjectRepository.findAllByStudyGroup(studyGroup)
                .stream()
                .flatMap(subject -> subject.getClasses().stream())
                .filter(cls -> Objects.equals(period, cls.getPeriod())
                        || Period.EVERY_WEEK.equals(cls.getPeriod()))
                .distinct()
                .map(this::parseClass)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String parseClass(Class cl) {
        return "%s\n%s\n%s".formatted(cl.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")),
                cl.getSubject().getTeacher(), cl.getSubject().getName());
    }
}