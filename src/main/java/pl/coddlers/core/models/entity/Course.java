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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "course", targetEntity = Lesson.class)
    private List<Lesson> courseVersion = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", targetEntity = CourseGrade.class)
    private List<CourseGrade> courseGrades = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", targetEntity = Teacher.class)
    private List<Teacher> teachers = new ArrayList<>();

    public Course(String title, String description, Timestamp startDate, Timestamp endDate) {
        this.title = title;
        this.description = description;
    }
}
