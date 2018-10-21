package pl.coddlers.core.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.UserDataRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-dev.properties")
public class LessonServiceTest {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseEditionRepository courseEditionRepository;

    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private LessonRepository lessonRepository;


//    @Test
//    @Transactional
    public void simpleManualForkTest() throws InterruptedException, ExecutionException {
        CourseEdition courseEdition = courseEditionRepository.getOne(20L);
        List<Lesson> courseEditionLessons = lessonRepository.getCourseEditionLessons(courseEdition.getCourseVersion().getId());
        User student = userDataRepository.findFirstByUserMail("grjs@coddlers.pl");


        List<CompletableFuture<StudentLessonRepository>> completableFutures = new LinkedList<>();
        courseEditionLessons.forEach(lesson -> completableFutures.add(lessonService.forkModelLessonForUser(courseEdition, lesson, student)));
        completableFutures.forEach(CompletableFuture::join);

    }
}

