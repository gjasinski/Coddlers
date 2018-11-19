package pl.coddlers.core.services;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.converters.CourseConverter;
import pl.coddlers.core.models.dto.CourseWithCourseEditionDto;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.dto.ResultDto;
import pl.coddlers.core.models.entity.Teacher;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseRepository;

@Service
public class ResultsService {

  private final CourseService courseService;
  private final CourseEditionService courseEditionService;
  private final SubmissionService submissionService;
  private final UserDetailsServiceImpl userDetailsService;
  private final CourseRepository courseRepository;
  private final CourseConverter courseConverter;

  @Autowired
  public ResultsService(CourseService courseService,
      CourseEditionService courseEditionService,
      SubmissionService submissionService,
      UserDetailsServiceImpl userDetailsService,
      CourseRepository courseRepository,
      CourseConverter courseConverter) {
    this.courseService = courseService;
    this.courseEditionService = courseEditionService;
    this.submissionService = submissionService;
    this.userDetailsService = userDetailsService;
    this.courseRepository = courseRepository;
    this.courseConverter = courseConverter;
  }

  public Collection<ResultDto> getResults() {
    User user = userDetailsService.getCurrentUserEntity();
    List<ResultDto> results = new LinkedList<>();

    List<CourseWithCourseEditionDto> courseWithCourseEditionDtos = courseEditionService
        .getAllEditionsWithEnrolledStudent(user);

    courseWithCourseEditionDtos.forEach(dtos -> {
      Optional<Course> course = courseRepository
          .getByCourseEditionId(dtos.getCourseEdition().getId());

      List<String> teachers =
          course.isPresent() ? courseConverter.convertFromEntity(course.get()).getTeachers() : Collections.emptyList();

      ResultDto result = new ResultDto(dtos.getCourse().getTitle(), teachers);
      results.add(result);
    });

    return results;
  }
}
