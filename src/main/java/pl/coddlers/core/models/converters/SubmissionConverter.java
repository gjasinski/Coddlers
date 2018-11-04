package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.coddlers.core.exceptions.CourseEditionNotFoundException;
import pl.coddlers.core.exceptions.TaskNotFoundException;
import pl.coddlers.core.exceptions.UserNotFoundException;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.models.entity.SubmissionStatusType;
import pl.coddlers.core.models.entity.SubmissionStatusTypeEnum;
import pl.coddlers.core.repositories.CourseEditionRepository;
import pl.coddlers.core.repositories.SubmissionRepository;
import pl.coddlers.core.repositories.TaskRepository;
import pl.coddlers.core.repositories.UserDataRepository;

@Component
@Transactional
public class SubmissionConverter implements BaseConverter<Submission, SubmissionDto> {


	private final SubmissionRepository submissionRepository;
	private final UserDataRepository userDataRepository;
	private final TaskRepository taskRepository;
	private final CourseEditionRepository courseEditionRepository;

	@Autowired
	public SubmissionConverter(SubmissionRepository submissionRepository, UserDataRepository userDataRepository,
							   TaskRepository taskRepository, CourseEditionRepository courseEditionRepository) {
		this.submissionRepository = submissionRepository;
		this.userDataRepository = userDataRepository;
		this.taskRepository = taskRepository;
		this.courseEditionRepository = courseEditionRepository;
	}

	@Override
	public SubmissionDto convertFromEntity(Submission entity) {
		SubmissionDto submissionDto = new SubmissionDto();
		submissionDto.setId(entity.getId());
		submissionDto.setTaskId(entity.getTask().getId());
		submissionDto.setUserId(entity.getUser().getId());
		submissionDto.setCourseEditionId(entity.getCourseEdition().getId());
		submissionDto.setSubmissionTime(entity.getSubmissionTime());
		submissionDto.setSubmissionStatusType(
				SubmissionStatusTypeEnum.getEnumByStatusName(
						entity.getSubmissionStatusType().getName()
				));
		submissionDto.setPoints(entity.getPoints());

		return submissionDto;
	}

	@Override
	public Submission convertFromDto(SubmissionDto dto) {
		Submission submission = new Submission();

		if (dto.getId() != null && submissionRepository.existsById(dto.getId())) {
			submission = submissionRepository.getOne(dto.getId());
		}

		submission.setTask(taskRepository.findById(dto.getTaskId())
				.orElseThrow(() -> new TaskNotFoundException(dto.getTaskId())));
		submission.setUser(userDataRepository.findById(dto.getUserId())
				.orElseThrow(() -> new UserNotFoundException(dto.getId())));
		submission.setCourseEdition(courseEditionRepository.findById(dto.getCourseEditionId())
				.orElseThrow(() -> new CourseEditionNotFoundException(dto.getCourseEditionId())));
		submission.setSubmissionTime(dto.getSubmissionTime());
		SubmissionStatusType submissionStatusType = new SubmissionStatusType();
		submissionStatusType.setName(dto.getSubmissionStatusType().getStatus());
		submission.setSubmissionStatusType(submissionStatusType);
		submission.setPoints(dto.getPoints());

		return submission;
	}
}
