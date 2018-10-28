package pl.coddlers.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseNotFoundException;
import pl.coddlers.core.exceptions.GitAsynchronousOperationException;
import pl.coddlers.core.exceptions.WrongDateException;
import pl.coddlers.core.models.converters.CourseConverter;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.entity.CourseVersion;
import pl.coddlers.core.models.entity.Teacher;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseRepository;
import pl.coddlers.core.repositories.CourseVersionRepository;
import pl.coddlers.core.repositories.TeacherRepository;
import pl.coddlers.git.models.event.ProjectDto;
import pl.coddlers.git.services.GitGroupService;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;
    private final UserDetailsServiceImpl userDetailsService;
    private final CourseVersionRepository courseVersionRepository;
    private final TeacherRepository teacherRepository;
    private final GitGroupService gitGroupService;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseConverter courseConverter,
                         UserDetailsServiceImpl userDetailsService, CourseVersionRepository courseVersionRepository,
                         TeacherRepository teacherRepository, GitGroupService gitGroupService) {
        this.courseRepository = courseRepository;
        this.courseConverter = courseConverter;
        this.userDetailsService = userDetailsService;
        this.courseVersionRepository = courseVersionRepository;
        this.teacherRepository = teacherRepository;
        this.gitGroupService = gitGroupService;
    }

    public CourseDto getCourseById(Long id) {
        Course course = validateCourse(id);

        return courseConverter.convertFromEntity(course);
    }

    public Collection<CourseDto> getCourses() throws WrongDateException {
        List<Course> courseList = courseRepository.getByUserId(userDetailsService.getCurrentUserEntity().getId());
        return courseConverter.convertFromEntities(courseList);
    }

    public Course createCourse(final CourseDto courseDto) {
        try {
            Course course = courseRepository.save(courseConverter.convertFromDto(courseDto));
            User currentUser = userDetailsService.getCurrentUserEntity();
            CompletableFuture<ProjectDto> gitGroup = createGitGroup(course, currentUser);
            CourseVersion courseVersion = new CourseVersion(1, course);
            Teacher teacher = new Teacher(course, currentUser, true);
            courseVersionRepository.save(courseVersion);
            teacherRepository.save(teacher);
            ProjectDto projectDto = gitGroup.get();
            course.setGitGroupId(projectDto.getId());
            course = courseRepository.save(course);
            return course;
        } catch (InterruptedException | ExecutionException e) {
            log.error(String.format("Cannot create new course for %s", courseDto.toString()), e);
            throw new GitAsynchronousOperationException("Cannot create new course");
        }
    }

    private CompletableFuture<ProjectDto> createGitGroup(Course course, User currentUser) {
        return gitGroupService.createGroup(createGroupName(course))
                .thenApplyAsync(gitGroupDto -> {
                    gitGroupService.addUserToGroupAsMaintainer(currentUser.getGitUserId(), gitGroupDto.getId()).join();
                    return gitGroupDto;
                });
    }

    private String createGroupName(Course course) {
        return course.getId() + "_" + Long.toString(Instant.now().getEpochSecond());
    }

    public void updateCourse(final CourseDto courseDto) {
        Course course = courseConverter.convertFromDto(courseDto);
        course.setId(courseDto.getId());
        courseRepository.save(course);
    }

    private Course validateCourse(Long id) throws CourseNotFoundException {
        return courseRepository.findById(id).orElseThrow(
                () -> new CourseNotFoundException(id)
        );
    }

}
