package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "groups")
    private Set<User> users = new HashSet<>();
}
