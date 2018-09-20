package pl.coddlers.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String profilePictureName;

    @Column(nullable=false, length = 50, unique = true)
    private String userMail;

    @Column(nullable=false, length = 100)
    private String password;

    @JsonIgnore
    private Long gitUserId;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_account_type",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "account_type_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<AccountType> accountTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<CourseEdition> courseEditions = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", targetEntity = Teacher.class)
    private Set<Teacher> teacherInCourse = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_groups_relation",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")})
    private Set<UsersGroup> userGroups = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", targetEntity = StudentLessonRepository.class)
    private Set<StudentLessonRepository> studentLessonRepositories = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", targetEntity = Submission.class)
    private Set<Submission> submissions = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", targetEntity = CourseGrade.class)
    private Set<CourseGrade> courseGrades = new HashSet<>();
}
