package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Submission.class)
    @JoinColumn(name = "submission_id")
    private Submission submission;
}
