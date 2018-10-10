package pl.coddlers.core.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.exceptions.UserNotFoundException;
import pl.coddlers.core.models.converters.CourseEditionConverter;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.CourseEditionLesson;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionLessonRepository;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.UserDataRepository;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseEditionService {


    private static final String key = "Bar12345Bar12345"; // 128 bit key
    private static final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

    private final CourseEditionRepository courseEditionRepository;
    private final CourseEditionConverter courseEditionConverter;
    private final LessonRepository lessonRepository;
    private final CourseEditionLessonRepository courseEditionLessonRepository;
    private final UserDataRepository userDataRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseEditionService(UserDetailsServiceImpl userDetailsService, UserDataRepository userDataRepository, CourseEditionRepository courseEditionRepository, CourseEditionConverter courseEditionConverter, LessonRepository lessonRepository, CourseEditionLessonRepository courseEditionLessonRepository) {
        this.userDetailsService = userDetailsService;
        this.userDataRepository = userDataRepository;
        this.courseEditionRepository = courseEditionRepository;
        this.courseEditionConverter = courseEditionConverter;
        this.lessonRepository = lessonRepository;
        this.courseEditionLessonRepository = courseEditionLessonRepository;
    }

    public CourseEditionDto getCourseEditionById(Long id) {
        CourseEdition courseEdition = validateCourseEdition(id);
        return courseEditionConverter.convertFromEntity(courseEdition);
    }

    private CourseEdition validateCourseEdition(Long id) throws CourseEditionNotFoundException {
        return courseEditionRepository.findById(id).orElseThrow(() -> new CourseEditionNotFoundException(id));
    }

    public List<CourseEditionDto> getCourseEditionsByCourseVersionId(Long courseVersionId) {
        return courseEditionRepository.findAllByCourseVersionId(courseVersionId).stream()
                .map(courseEditionConverter::convertFromEntity)
                .collect(Collectors.toList());
    }

    public CourseEdition createCourseEdition(CourseEditionDto courseEditionDto) {
        return courseEditionRepository.save(courseEditionConverter.convertFromDto(courseEditionDto));

    }

    public List<CourseEditionLesson> cloneLessons(CourseEdition courseEdition) {
        List<Lesson> lessons = lessonRepository.findByCourseVersionId(courseEdition.getCourseVersion().getId());
        return lessons.stream()
                .map(lesson -> {
                    CourseEditionLesson courseEditionLesson = createCourseEditionLesson(courseEdition, lesson);
                    return courseEditionLessonRepository.save(courseEditionLesson);
                })
                .collect(Collectors.toList());
    }

    private CourseEditionLesson createCourseEditionLesson(CourseEdition courseEdition, Lesson lesson) {
        CourseEditionLesson courseEditionLesson = new CourseEditionLesson();
        courseEditionLesson.setCourseEdition(courseEdition);
        courseEditionLesson.setLesson(lesson);
        courseEditionLesson.setStartDate(courseEdition.getStartDate());
        courseEditionLesson.setEndDate(getEndDate(courseEdition, lesson));
        return courseEditionLesson;
    }

    private Timestamp getEndDate(CourseEdition courseEdition, Lesson lesson) {
        LocalDateTime startDate = courseEdition.getStartDate().toLocalDateTime();
        LocalDateTime endTime = startDate.plusDays(lesson.getTimeInDays());
        return Timestamp.valueOf(endTime);
    }

    @Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
    public void addStudentToCourseEdition(Long courseId) {
        CourseEdition courseEdition = courseEditionRepository.findById(courseId).orElseThrow(() -> new CourseEditionNotFoundException(courseId));

//        User currentUser = userDetailsService.getCurrentUserEntity();
        User currentUser = userDataRepository.findById(2L).get();

        String title = courseEdition.getTitle();
        System.out.println("courseTitle = " + title);



        try
        {
            String encryptedTitle = encryptCourse(title);
            System.out.println("second try: encrypted = " + encryptedTitle);

            String decodedTitle = decryptCourse(encryptedTitle);
            System.out.println(decodedTitle);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        courseEdition.getUsers().add(currentUser);
//        courseEditionRepository.saveAndFlush(courseEdition);
    }




    private String encryptCourse(String courseName) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(courseName.getBytes());
        return new String(Base64.getEncoder().encode(encrypted));
    }

    private String decryptCourse(String encryptedString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] encrypted = Base64.getDecoder().decode(encryptedString);
        return new String(cipher.doFinal(encrypted));
    }

    public void addStudentToCourseEdition(String course) {
        System.out.println("Encrypted = " + course);
        course = course.replace(" ","+");
        System.out.println("Encrypted = " + course);

        try {
            String courseTitle = decryptCourse(course);
            System.out.println(courseTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
