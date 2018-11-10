package pl.coddlers.core.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SubmissionReopenDto implements Commentable {
    @Size(max = 255)
    private String reason;

    @JsonIgnore
    @Override
    public String getComment() {
        return reason;
    }
}
