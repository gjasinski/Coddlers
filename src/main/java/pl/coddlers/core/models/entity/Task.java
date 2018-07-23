package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String title;

    private String description;

    @Column(nullable=false)
    private int maxPoints;

    @JsonIgnore
    @ManyToOne(targetEntity = Lesson.class)
    private Lesson lesson;

    @Column(nullable=false)
    private SubmissionStatusType submissionStatusType;

    public Task(String title, String description, int maxPoints, Lesson lesson, SubmissionStatusType submissionStatusType) {
        this.title = title;
        this.description = description;
        this.maxPoints = maxPoints;
        this.lesson = lesson;
        this.submissionStatusType = submissionStatusType;
    }
}
