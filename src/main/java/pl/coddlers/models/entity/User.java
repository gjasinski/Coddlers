package pl.coddlers.models.entity;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserAccountType",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "accountTypeName", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<AccountType> authorities = new HashSet<>();
}
