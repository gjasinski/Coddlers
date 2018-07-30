package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class CourseVersionLessonRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private Long gitRepositoryId;

    @Column(nullable=false)
    private String repositoryUrl;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseVersion.class)
    @JoinColumn(name = "course_version_id")
    private CourseVersion courseVersion;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Lesson.class)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
