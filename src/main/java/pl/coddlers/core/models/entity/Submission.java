package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Submission {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(nullable = false)
    private Timestamp submissionTime;

    private Integer points;

    @ManyToOne(targetEntity = Task.class)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = SubmissionStatusType.class)
    @JoinColumn(name = "submission_status_type_name")
    private SubmissionStatusType submissionStatusType;
}
