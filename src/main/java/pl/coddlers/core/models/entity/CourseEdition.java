package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
public class CourseEdition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Timestamp startDate;

    @ManyToOne(targetEntity = CourseVersion.class)
    @JoinColumn(name = "course_version_id")
    private CourseVersion courseVersion;

    public CourseEdition(String title, CourseVersion courseVersion, Timestamp startDate) {
        this.title = title;
        this.courseVersion = courseVersion;
        this.startDate = startDate;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "courseEdition", targetEntity = CourseEditionLesson.class)
    private List<CourseEditionLesson> courseEditionLesson;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_edition_students",
            joinColumns = {@JoinColumn(name = "course_edition_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "courseEdition", targetEntity = StudentLessonRepository.class)
    private Set<StudentLessonRepository> studentLessonRepositories;
}
