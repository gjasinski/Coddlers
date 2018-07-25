package pl.coddlers.core.models.entity;

@Entity
@Data
@NoArgsConstructor
class SubmissionStatusType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
}