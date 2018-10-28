package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.SubmissionNotFoundException;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.models.entity.SubmissionStatusTypeEnum;
import pl.coddlers.core.models.entity.User;
import pl.coddlers.core.repositories.SubmissionRepository;

import java.util.Collection;

@Service
public class SubmissionService {

	private final SubmissionRepository submissionRepository;
	private final SubmissionConverter submissionConverter;

	@Autowired
	public SubmissionService(SubmissionRepository submissionRepository, SubmissionConverter submissionConverter) {
		this.submissionRepository = submissionRepository;
		this.submissionConverter = submissionConverter;
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
}
