package pl.coddlers.core.models.dto;

import lombok.Data;
import pl.coddlers.core.models.entity.Submission;

import java.sql.Timestamp;
import java.util.Collection;

@Data
public class SubmissionDataDto {

    private String fullName;
    private SubmissionDto submission;
    private Collection<GitFileContent> gitFileContents;

}
