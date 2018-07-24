package pl.coddlers.core.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class NoteAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String fileName;

    @ManyToOne(targetEntity = Note.class)
    @JoinColumn(name = "note_id")
    private Note note;
}
