package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude={"lesson", "submissions", "notes"})
public class Task {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String title;

    private String description;

    @Column(nullable=false)
    private int maxPoints;

    private Boolean isCodeTask;

    private String branchNamePrefix;

    private Timestamp creationTime;

    @JsonIgnore
    @ManyToOne(targetEntity = Lesson.class)
    private Lesson lesson;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", targetEntity = Submission.class)
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", targetEntity = Note.class)
    private List<Note> notes = new ArrayList<>();
}
