package pl.coddlers.core.models.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coddlers.core.models.dto.SubmissionDto;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.repositories.SubmissionRepository;

@Component
public class SubmissionConverter implements BaseConverter<Submission, SubmissionDto> {


	private final SubmissionRepository submissionRepository;

	@Autowired
	public SubmissionConverter(SubmissionRepository submissionRepository) {
		this.submissionRepository = submissionRepository;
	}

	@Override
	public SubmissionDto convertFromEntity(Submission entity) {
		SubmissionDto submissionDto = new SubmissionDto();
		submissionDto.setId(entity.getId());
		submissionDto.setTask(entity.getTask());
		submissionDto.setAuthor(entity.getAuthor());
		submissionDto.setSubmissionTime(entity.getSubmissionTime());
		submissionDto.setPoints(entity.getPoints());
		submissionDto.setStatus(entity.getStatus());

		return submissionDto;
	}

	@Override
	public Submission convertFromDto(SubmissionDto dto) {
		Submission submission = new Submission();

		if (dto.getId() != null && submissionRepository.existsById(dto.getId())) {
			submission.setId(dto.getId());
		}

		submission.setAuthor(dto.getAuthor());
		submission.setSubmissionTime(dto.getSubmissionTime());
		submission.setPoints(dto.getPoints());
		submission.setTask(dto.getTask());
		submission.setStatus(dto.getStatus());

		return submission;
	}
}
