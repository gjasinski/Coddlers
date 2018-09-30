package pl.coddlers.git.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("git_ssh_url")
    private String gitSshUrl;

    @JsonProperty("git_http_url")
    private String gitHttpUrl;

    @JsonProperty("visibility_level")
    private Integer visibilityLevel;

    @JsonProperty("path_with_namespace")
    private String pathWithNamespace;

    @JsonProperty("default_branch")
    private String defaultBranch;
    private String homepage;
    private String url;

    @JsonProperty("ssh_url")
    private String sshUrl;

    @JsonProperty("http_url")
    private String httpUrl;

}
