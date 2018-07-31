package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CourseVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private Integer versionNumber;

    @JsonIgnore
    @ManyToOne(targetEntity = Course.class)
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "courseVersion", targetEntity = CourseEdition.class)
    private List<CourseEdition> courseEditions;
}
