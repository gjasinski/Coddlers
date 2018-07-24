package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private Integer timeInDays;

    @OneToMany(mappedBy = "lesson", targetEntity = Task.class)
    private List<Task> tasks = new ArrayList<>();

    // TODO only for prototype purposes
    @JsonIgnore
    private Long gitStudentProjectId;

    @JsonIgnore
    @ManyToOne(targetEntity = CourseVersion.class)
    private CourseVersion courseVersion;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", targetEntity = CourseEditionLesson.class)
    private Set<CourseEditionLesson> courseEditionLessons;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", targetEntity = Note.class)
    private List<Note> notes;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", targetEntity = StudentLessonRepository.class)
    private List<StudentLessonRepository> studentLessonRepositories;

    @JsonIgnore
    @OneToMany(mappedBy = "lesson", targetEntity = CourseVersionLessonRepository.class)
    private List<CourseVersionLessonRepository> courseVersionLessonRepositories;
}
