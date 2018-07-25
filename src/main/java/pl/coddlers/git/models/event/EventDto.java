package pl.coddlers.git.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventDto {

    @JsonProperty("object_kind")
    private String objectKind;
    private String before;
    private String after;
    private String ref;

    @JsonProperty("checkout_sha")
    private String checkoutSha;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_username")
    private String userUsername;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_avatar")
    private String userAvatar;

    @JsonProperty("project_id")
    private Long projectId;

    @JsonProperty("project")
    private ProjectDto projectDto;

    @JsonProperty("repository")
    private RepositoryDto repositoryDto;
    private CommitDto[] commits;

    @JsonProperty("total_commits_count")
    private Integer totalCommitsCount;

}
