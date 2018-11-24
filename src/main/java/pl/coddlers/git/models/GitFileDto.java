package pl.coddlers.git.models;

import lombok.Data;

@Data
public class GitFileDto {

    private String id;
    private String name;
    private String type;
    private String path;
    private String mode;
}
