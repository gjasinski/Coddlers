package pl.coddlers.git.models.event;

import lombok.Data;

@Data
public class CommitChangesDto {
    private String[] added;
    private String[] modified;
    private String[] removed;

}
