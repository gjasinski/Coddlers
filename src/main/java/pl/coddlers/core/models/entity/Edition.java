package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="course_edition")
@NoArgsConstructor
public class Edition {
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
    private Timestamp endDate;

    @Column(nullable = false)
    private Long courseId;

    public Edition(String title, Long version, Timestamp startDate, Timestamp endDate, Long courseId) {
        this.title = title;
        this.version = version;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseId = courseId;
    }
}
