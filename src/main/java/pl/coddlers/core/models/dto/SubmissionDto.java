package pl.coddlers.core.models.dto;

import lombok.Data;
import pl.coddlers.core.models.entity.SubmissionStatusTypeEnum;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class SubmissionDto {
    private Long id;

    @NotNull
    private Long taskId;

    @NotNull
    private Long userId;

    @NotNull
    private String userFullName;

    @NotNull
    private Long courseEditionId;

    private Timestamp submissionTime;

    @NotNull
    private SubmissionStatusTypeEnum submissionStatusType;

    private Integer points;
}
