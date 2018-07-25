package pl.coddlers.git.models.event;

import lombok.Data;

@Data
class CommitAuthorDto {
    private String name;
    private String email;

}
