package pl.coddlers.core.models.dto;

import lombok.Data;
import pl.coddlers.core.models.entity.Course;
import pl.coddlers.core.models.entity.CourseVersion;

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
    private CourseVersion courseVersion;

    @NotNull
    private Timestamp startDate;

    @NotNull
    private Course course;
}
