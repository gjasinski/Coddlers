package pl.coddlers.core.models.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.coddlers.core.models.entity.Teacher;

@Data
public class CourseDto {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 255)
    private String description;

    private List<String> teachers;
}
