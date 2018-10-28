package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude={"courseEdition", "lesson"})
public class CourseEditionLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Timestamp startDate;
    private Timestamp endDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseEdition.class)
    @JoinColumn(name = "course_edition_id")
    private CourseEdition courseEdition;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Lesson.class)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
