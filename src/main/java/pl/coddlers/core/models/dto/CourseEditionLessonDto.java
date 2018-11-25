package pl.coddlers.core.models.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CourseEditionLessonDto {
    private Long id;

    private Timestamp startDate;

    private Timestamp endDate;

    private Long courseEditionId;

    private Long lessonId;
}
