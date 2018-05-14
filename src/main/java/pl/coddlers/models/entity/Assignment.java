package pl.coddlers.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String title;

    private String description;

    @Column(nullable=false)
    private Timestamp startDate;

    @Column(nullable=false)
    private Timestamp dueDate;

    @OneToMany(mappedBy = "assignment", targetEntity = Task.class)
    private List<Task> taskList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(targetEntity = Course.class)
    private Course course;

    public Assignment(String title, String description, Timestamp startDate, Timestamp dueDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public Assignment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
