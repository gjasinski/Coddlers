package pl.coddlers.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class AssignmentDto {
	private Long id;

	@NotNull
	private Long courseId;

	@NotNull
	@Size(min = 3, max = 100)
	private String title;

	@Size(max = 255)
	private String description;

	@NotNull
	private Integer weight;

	@NotNull
	private Timestamp startDate;

	@NotNull
	private Timestamp dueDate;

	// TODO only for prototype purposes
	@JsonIgnore
	private Long gitStudentProjectId;
}
