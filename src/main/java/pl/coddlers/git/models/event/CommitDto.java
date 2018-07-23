package pl.coddlers.git.models.event;

import lombok.Data;

@Data
class CommitDto {
    private CommitInfoDto commitInfoDTO;
    private CommitAuthorDto commitAuthorDTO;

}
