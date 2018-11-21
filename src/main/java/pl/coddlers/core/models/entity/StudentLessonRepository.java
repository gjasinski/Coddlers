package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"lesson", "courseEdition"})
public class StudentLessonRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @Column(nullable=false)
    private Long gitRepositoryId;

    @Column(nullable=false)
    private String repositoryUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "studentLessonRepository", targetEntity = Submission.class)
    private List<Submission> submissions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Lesson.class)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseEdition.class)
    @JoinColumn(name = "course_edition_id")
    private CourseEdition courseEdition;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
}
