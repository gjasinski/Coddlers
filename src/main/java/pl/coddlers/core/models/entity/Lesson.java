package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude={"tasks", "courseVersion", "courseEditionLessons", "notes", "studentLessonRepositories"})
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private Integer weight;

    @Column(nullable = false)
    private Integer timeInDays;

    @JsonIgnore
    private Long gitProjectId;

    private String repositoryName;

    @OneToMany(mappedBy = "lesson", targetEntity = Task.class)
    private List<Task> tasks = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = CourseVersion.class)
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
}
