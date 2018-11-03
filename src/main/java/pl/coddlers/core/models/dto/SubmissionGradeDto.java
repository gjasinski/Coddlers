package pl.coddlers.core.models.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SubmissionGradeDto implements Commentable {
    @Size(max = 255)
    private String comment;

    @NotNull
    private Integer points;
}
