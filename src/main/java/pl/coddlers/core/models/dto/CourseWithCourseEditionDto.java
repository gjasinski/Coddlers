package pl.coddlers.core.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CourseWithCourseEditionDto {
    @NotNull
    private CourseDto course;

    @NotNull
    private CourseEditionDto courseEdition;

    private int submittedTasks;

    private int gradedTasks;

    private int allTasks;

    private int gradedLessons;

    private int submittedLessons;

    private int lessonsSize;
}
