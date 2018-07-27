package pl.coddlers.core.models.dto;

import lombok.Data;
import pl.coddlers.core.models.entity.Task;
import pl.coddlers.core.models.entity.TaskStatus;
import pl.coddlers.core.models.entity.User;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class SubmissionDto {
    private Long id;

    @NotNull
    private Task task;

    @NotNull
    private User author;

    @NotNull
    private Timestamp submissionTime;

    @NotNull
    private TaskStatus status;

    private Integer points;
}
