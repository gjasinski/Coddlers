package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private Timestamp submissionTime;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = SubmissionStatusType.class)
    @JoinColumn(name = "submission_status_type_id")
    private SubmissionStatusType submissionStatusType;

    
}
