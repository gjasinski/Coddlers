package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable=false)
    private String author;

    @Column(nullable=false)
    private Timestamp submissionTime;

    private Integer points;

    @JsonIgnore
    @ManyToOne(targetEntity = Task.class)
    private Task task;

    @Column(nullable=false)
    private TaskStatus status;

    public Submission(String author, Timestamp submissionTime, int points, Task task, TaskStatus status) {
        this.author = author;
        this.submissionTime = submissionTime;
        this.points = points;
        this.task = task;
        this.status = status;
    }
}
