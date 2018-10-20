package pl.coddlers.core.models.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class CourseEditionDto {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    private CourseVersionDto courseVersion;

    @NotNull
    private Timestamp startDate;

    private String invitationToken;
}
