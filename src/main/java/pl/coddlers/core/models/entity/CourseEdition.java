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

    @ManyToOne(targetEntity = CourseVersion.class)
    @JoinColumn(name = "course_version_id")
    private CourseVersion courseVersion;

    @Column(nullable = false)
    private Timestamp startDate;

    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseEdition(String title, CourseVersion courseVersion, Timestamp startDate, Course course) {
        this.title = title;
        this.courseVersion = courseVersion;
        this.startDate = startDate;
        this.course = course;
    }
}
