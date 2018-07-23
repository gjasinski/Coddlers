package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String title;

    private String description;

    private Integer weight;

    @Column(nullable=false)
    private Timestamp startDate;

    @Column(nullable=false)
    private Integer timeInDays;

    @OneToMany(mappedBy = "lesson", targetEntity = Task.class)
    private List<Task> taskList = new ArrayList<>();

    // TODO only for prototype purposes
    @JsonIgnore
    private Long gitStudentProjectId;

    @JsonIgnore
    @ManyToOne(targetEntity = CourseVersion.class)
    private CourseVersion courseVersion;

    public Lesson(String title, String description, Integer weight, Timestamp startDate, Timestamp dueDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.weight = weight;
    }
}
