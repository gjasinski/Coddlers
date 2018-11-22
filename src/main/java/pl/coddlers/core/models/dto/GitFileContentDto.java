package pl.coddlers.core.models.dto;

import lombok.Data;

@Data
public class GitFileContentDto {

    private String path;

    private String content;
}
