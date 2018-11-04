package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.SubmissionNotFoundException;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.*;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.SubmissionRepository;
import pl.coddlers.core.repositories.TaskRepository;

import java.util.Collection;

@Service
public class SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final SubmissionConverter submissionConverter;
	private final CourseEditionRepository courseEditionRepository;
	private final UserDetailsServiceImpl userDetailsService;

	@Autowired
	public SubmissionService(SubmissionRepository submissionRepository, SubmissionConverter submissionConverter, CourseEditionRepository courseEditionRepository, UserDetailsServiceImpl userDetailsService) {
		this.submissionRepository = submissionRepository;
		this.submissionConverter = submissionConverter;
		this.courseEditionRepository = courseEditionRepository;
		this.userDetailsService = userDetailsService;
	}

	public Collection<SubmissionDto> getAllTaskSubmissions(long taskId) {
		return submissionConverter.convertFromEntities(submissionRepository.findByTaskId(taskId));
	}

	public SubmissionDto getSubmissionByBranchNameAndRepoName(String branchName, String repoName) {
		return submissionConverter.convertFromEntity(
				submissionRepository.findByBranchNameAndStudentLessonRepository_RepositoryUrl(branchName, repoName)
				.orElseThrow(() -> new SubmissionNotFoundException(branchName, repoName))
		);
	}

	public void updateSubmission(SubmissionDto submissionDto) {
		if (submissionDto.getId() == null || !submissionRepository.existsById(submissionDto.getId())) {
			throw new SubmissionNotFoundException(submissionDto.getId());
		}
		submissionRepository.save(submissionConverter.convertFromDto(submissionDto));
	}

	public Submission createSubmission(SubmissionDto submissionDto) {
		return submissionRepository.save(submissionConverter.convertFromDto(submissionDto));
	}

	public int countAllSubmittedTasks(User user, CourseEdition courseEdition) {
		return submissionRepository.countAllByUserAndCourseEditionAndSubmissionStatusTypeName(user,
				courseEdition,
				SubmissionStatusTypeEnum.WAITING_FOR_REVIEW.getStatus()) +
				countAllGradedTasks(user, courseEdition);
	}

	public int countAllGradedTasks(User user, CourseEdition courseEdition) {
		return submissionRepository.countAllByUserAndCourseEditionAndSubmissionStatusTypeName(user,
						courseEdition,
						SubmissionStatusTypeEnum.GRADED.getStatus());
	}

	public int countAllTask(User user, CourseEdition courseEdition){
		return submissionRepository.countAllByUserAndCourseEdition(user, courseEdition);
	}

	public void createSubmission(CourseEdition courseEdition, Task task, User user, StudentLessonRepository studentLessonRepository) {
		Submission submission = new Submission();
		SubmissionStatusType submissionStatusType = new SubmissionStatusType();
		submissionStatusType.setName(SubmissionStatusTypeEnum.NOT_SUBMITTED.getStatus());
		submission.setSubmissionStatusType(submissionStatusType);
		submission.setUser(user);
		submission.setCourseEdition(courseEdition);
		submission.setStudentLessonRepository(studentLessonRepository);
		submission.setTask(task);
		submission.setBranchName(task.getBranchNamePrefix());
		submissionRepository.saveAndFlush(submission);
	}

	public Collection<SubmissionDto> getTaskSubmission(Long lessonId, Long courseEditionId) {
		User currentUser = userDetailsService.getCurrentUserEntity();
		CourseEdition courseEdition = courseEditionRepository.getOne(courseEditionId);
		return submissionConverter.convertFromEntities(submissionRepository.findSubmissionForTaskAndUser(lessonId, currentUser.getId(), courseEdition));
	}
}
