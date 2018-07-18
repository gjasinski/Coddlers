package pl.coddlers.core.models.dto;

import lombok.Data;
import pl.coddlers.core.models.entity.TaskStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TaskDto {
    private Long id;

    @NotNull
    private Long lessonId;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    private Integer maxPoints;

    @NotNull
    private TaskStatus taskStatus;
}
