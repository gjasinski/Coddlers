package pl.coddlers.core.models.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CourseDto {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 255)
    private String description;
}
