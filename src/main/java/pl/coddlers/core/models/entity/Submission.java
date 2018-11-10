package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Timestamp submissionTime;

    private Integer points;

    private String branchName;

    private String lastCommitHash;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = SubmissionStatusType.class)
    @JoinColumn(name = "submission_status_type_name")
    private SubmissionStatusType submissionStatusType;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = StudentLessonRepository.class)
    @JoinColumn(name = "student_lesson_repository_id")
    private StudentLessonRepository studentLessonRepository;

    @OneToMany(mappedBy = "submission", targetEntity = Comment.class)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseEdition.class)
    @JoinColumn(name = "course_edition_id")
    private CourseEdition courseEdition;

    @ManyToOne(targetEntity = Task.class)
    @JoinColumn(name = "task_id")
    private Task task;

    public SubmissionStatusTypeEnum getSubmissionStatusTypeEnum() {
        return SubmissionStatusTypeEnum.getEnumByStatusName(submissionStatusType.getName());
    }
}
