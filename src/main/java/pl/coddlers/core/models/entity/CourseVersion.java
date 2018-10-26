package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CourseVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer versionNumber;

    @JsonIgnore
    @ManyToOne(targetEntity = Course.class)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "courseVersion", targetEntity = CourseEdition.class)
    private List<CourseEdition> courseEditions;

    @JsonIgnore
    @OneToMany(mappedBy = "courseVersion", targetEntity = Lesson.class)
    private List<Lesson> lessons;

    @JsonIgnore
    @OneToMany(mappedBy = "courseVersion", targetEntity = Note.class)
    private List<Note> notes;

    public CourseVersion(Integer versionNumber, Course course) {
        this.versionNumber = versionNumber;
        this.course = course;
    }
}
