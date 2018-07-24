package pl.coddlers.core.models.dto;

import lombok.Data;
import pl.coddlers.core.models.entity.TaskStatus;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class SubmissionDto {
    private Long id;

    @NotNull
    private Long taskId;

    @NotNull
    private String author;

    @NotNull
    private Timestamp submissionTime;

    @NotNull
    private TaskStatus status;

    private Integer points;
}
