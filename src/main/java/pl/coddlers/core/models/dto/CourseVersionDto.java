package pl.coddlers.core.models.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseVersionDto {
    private Long id;

    @NotNull
    private Integer versionNumber;
}
