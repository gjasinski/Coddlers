package pl.coddlers.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coddlers.core.models.dto.CourseDto;
import pl.coddlers.core.models.dto.CourseEditionDto;
import pl.coddlers.core.models.dto.CourseWithCourseEditionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.services.CourseEditionService;
import pl.coddlers.core.services.CourseService;
import pl.coddlers.core.services.SubmissionService;
import pl.coddlers.core.services.TaskService;
import pl.coddlers.core.services.UserDetailsServiceImpl;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/editions")
public class CourseEditionController {

    private final CourseEditionService courseEditionService;
    private final CourseService courseService;
    private final SubmissionService submissionService;
    private final TaskService taskService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CourseEditionController(CourseEditionService courseEditionService, CourseService courseService, SubmissionService submissionService, TaskService taskService, UserDetailsServiceImpl userDetailsService) {
        this.courseEditionService = courseEditionService;
        this.courseService = courseService;
        this.submissionService = submissionService;
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CourseEditionDto> getCourseEdition(@PathVariable Long id) {
        return ResponseEntity.ok(courseEditionService.getCourseEditionById(id));
    }

    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createEdition(@RequestBody CourseEditionDto courseEditionDto) {
        CourseEdition courseEdition = courseEditionService.createCourseEdition(courseEditionDto);
        courseEditionService.createCourseEditionLessons(courseEdition);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(courseEdition.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping
    public ResponseEntity<List<CourseWithCourseEditionDto>> getCourses(){
        return ResponseEntity.ok(courseEditionService.getCourses().stream()
                .map(mapEditionToCourseWithCourseEditionDto())
                .collect(Collectors.toList()));

    }

    private Function<CourseEditionDto, CourseWithCourseEditionDto> mapEditionToCourseWithCourseEditionDto() {
        return edition -> {
            User currentUser = userDetailsService.getCurrentUserEntity();
            Long courseVersionId = edition.getCourseVersion().getId();
            CourseDto courseDto = courseService.getCourseByCourseVersionId(courseVersionId)
                    .orElseThrow(() -> new IllegalStateException(exceptionMessage(edition)));
            int allTasks = submissionService.countAllTask(currentUser.getId(), edition.getId());
            int gradedTasks = submissionService.countAllGradedTasks(currentUser.getId(), edition.getId());
            int submittedTasks = submissionService.countAllSubmittedTasks(currentUser.getId(), edition.getId());
            return new CourseWithCourseEditionDto(courseDto, edition, submittedTasks, gradedTasks, allTasks);
        };
    }

    private String exceptionMessage(CourseEditionDto edition) {
        return String.format("Inconsistency on database, didn't found Course for CourseEdition: %s", edition.toString());
    }
}
