package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class SubmissionStatusType {
    @Id
    @Column(length = 50, nullable = false)
    private String name;
}