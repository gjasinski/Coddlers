package pl.coddlers.core.models.dto;

import lombok.Data;

@Data
public class GitFileContent {

    private String path;

    private String content;
}
