package pl.coddlers.core.models.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SubmissionCommentDto implements Commentable {
    @Size(max = 255)
    @NotNull
    private String comment;
}
