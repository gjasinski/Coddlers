package pl.coddlers.core.models.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class SubmissionDataDto {

    private String fullName;
    private SubmissionDto submission;
    private Collection<GitFileContentDto> gitFileContents;

}
