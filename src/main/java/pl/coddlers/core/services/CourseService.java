package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.CourseNotFoundException;
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

	public Course createCourse(final CourseDto courseDto) throws ExecutionException, InterruptedException {
		Course course = courseRepository.save(courseConverter.convertFromDto(courseDto));
		User currentUser = userDetailsService.getCurrentUserEntity();
        CompletableFuture<ProjectDto> gitGroup = createGitGroup(course, currentUser);
        CourseVersion courseVersion = new CourseVersion(1, course);
		Teacher teacher = new Teacher(course, currentUser, true);
		courseVersionRepository.save(courseVersion);
		teacherRepository.save(teacher);
		gitGroup.join();
		ProjectDto projectDto = gitGroup.get();
		course.setGitGroupId(projectDto.getId());
		course.setGitGroupPath(projectDto.getPath());
        course = courseRepository.save(course);
		return course;
	}

    private CompletableFuture<ProjectDto> createGitGroup(Course course, User currentUser) {
        return gitGroupService.createGroup(createGroupName(course))
                    .thenApplyAsync(gitGroupDto -> {
                        gitGroupService.addUserToGroup(currentUser.getGitUserId(), gitGroupDto.getId()).join();
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
