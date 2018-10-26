package pl.coddlers.core.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseVersionDto {
    private Long id;

    @NotNull
    private Integer versionNumber;
}
