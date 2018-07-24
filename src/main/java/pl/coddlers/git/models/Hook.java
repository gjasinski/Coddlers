package pl.coddlers.git.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// FIXME: 19.07.18 For now this we need this class, while cloning repository I have to now about hooks which should I create in forked repository.
// todo: 19.07.18 In future we will have model and this may not be necessary.

@Entity
@Data
public class Hook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private String branch;
}
