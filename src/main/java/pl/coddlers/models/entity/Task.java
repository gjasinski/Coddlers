package pl.coddlers.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String title;

    private String description;

    @Column(nullable=false)
    private int maxPoints;

    @Column(nullable=false)
    private double weight;

    @ManyToOne(targetEntity = Assignment.class)
    @JsonIgnore
    private Assignment assignment;

    @Column(nullable=false)
    private TaskStatus taskStatus;

    public Task(String title, String description, int maxPoints, double weight, Assignment assignment, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.maxPoints = maxPoints;
        this.weight = weight;
        this.assignment = assignment;
        this.taskStatus = taskStatus;
    }

    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
