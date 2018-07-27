package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class CourseEdition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long version;

    @Column(nullable = false)
    private Timestamp startDate;

    @Column(nullable = false)
    private Long courseId;

    public CourseEdition(String title, Long version, Timestamp startDate, Long courseId) {
        this.title = title;
        this.version = version;
        this.startDate = startDate;
        this.courseId = courseId;
    }
}
