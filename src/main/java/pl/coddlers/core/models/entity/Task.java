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
    @ManyToOne(targetEntity = Assignment.class)
    private Assignment assignment;

    @Column(nullable=false)
    private TaskStatus taskStatus;

    public Task(String title, String description, int maxPoints, Assignment assignment, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.maxPoints = maxPoints;
        this.assignment = assignment;
        this.taskStatus = taskStatus;
    }
}
