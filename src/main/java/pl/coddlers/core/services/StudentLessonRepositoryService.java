package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Lesson;
import pl.coddlers.core.models.entity.StudentLessonRepository;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.LessonRepository;
import pl.coddlers.core.repositories.StudentLessonRepositoryRepository;

@Service
public class StudentLessonRepositoryService {
	private final StudentLessonRepositoryRepository studentLessonRepositoryRepository;
	private final UserDetailsServiceImpl userDetailsService;
	private final LessonRepository lessonRepository;
	private final CourseEditionRepository courseEditionRepository;


	@Autowired
	public StudentLessonRepositoryService(StudentLessonRepositoryRepository studentLessonRepositoryRepository, UserDetailsServiceImpl userDetailsService, LessonRepository lessonRepository, CourseEditionRepository courseEditionRepository) {
		this.studentLessonRepositoryRepository = studentLessonRepositoryRepository;
		this.userDetailsService = userDetailsService;
		this.lessonRepository = lessonRepository;
		this.courseEditionRepository = courseEditionRepository;
	}


	public String getRepositoryUrl(Long courseEditionId, Long lessonId){
		User currentUser = userDetailsService.getCurrentUserEntity();
		CourseEdition courseEdition = courseEditionRepository.getOne(courseEditionId);
		Lesson lesson = lessonRepository.getOne(lessonId);
		StudentLessonRepository repository = this.studentLessonRepositoryRepository.findByCourseEditionAndLessonAndUser(courseEdition, lesson, currentUser);
		return repository.getRepositoryUrl();
	}

}
