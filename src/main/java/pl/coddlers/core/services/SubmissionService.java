package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.models.converters.SubmissionConverter;
import pl.coddlers.core.models.dto.SubmissionDto;
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
}
