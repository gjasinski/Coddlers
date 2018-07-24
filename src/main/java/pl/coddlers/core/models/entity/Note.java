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
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "note", targetEntity = NoteAttachment.class)
    private List<NoteAttachment> noteAttachments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseVersion.class)
    @JoinColumn(name = "course_version_id")
    private CourseVersion courseVersion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseVersion.class)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CourseVersion.class)
    @JoinColumn(name = "task_id")
    private Task task;

}
