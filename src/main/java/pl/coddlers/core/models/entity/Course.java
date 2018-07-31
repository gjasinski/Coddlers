package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "course", targetEntity = CourseVersion.class)
    private List<CourseVersion> courseVersion = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", targetEntity = CourseGrade.class)
    private List<CourseGrade> courseGrades = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", targetEntity = Teacher.class)
    private List<Teacher> teachers = new ArrayList<>();
}
