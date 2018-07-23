package pl.coddlers.git.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	private String namespace;

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

	public ProjectDto(Long id, String name, String description, String webUrl, String avatarUrl, String gitSshUrl, String gitHttpUrl, String namespace, Integer visibilityLevel, String pathWithNamespace, String defaultBranch, String homepage, String url, String sshUrl, String httpUrl) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.webUrl = webUrl;
		this.avatarUrl = avatarUrl;
		this.gitSshUrl = gitSshUrl;
		this.gitHttpUrl = gitHttpUrl;
		this.namespace = namespace;
		this.visibilityLevel = visibilityLevel;
		this.pathWithNamespace = pathWithNamespace;
		this.defaultBranch = defaultBranch;
		this.homepage = homepage;
		this.url = url;
		this.sshUrl = sshUrl;
		this.httpUrl = httpUrl;
	}

	public ProjectDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getGitSshUrl() {
		return gitSshUrl;
	}

	public void setGitSshUrl(String gitSshUrl) {
		this.gitSshUrl = gitSshUrl;
	}

	public String getGitHttpUrl() {
		return gitHttpUrl;
	}

	public void setGitHttpUrl(String gitHttpUrl) {
		this.gitHttpUrl = gitHttpUrl;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Integer getVisibilityLevel() {
		return visibilityLevel;
	}

	public void setVisibilityLevel(Integer visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}

	public String getPathWithNamespace() {
		return pathWithNamespace;
	}

	public void setPathWithNamespace(String pathWithNamespace) {
		this.pathWithNamespace = pathWithNamespace;
	}

	public String getDefaultBranch() {
		return defaultBranch;
	}

	public void setDefaultBranch(String defaultBranch) {
		this.defaultBranch = defaultBranch;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSshUrl() {
		return sshUrl;
	}

	public void setSshUrl(String sshUrl) {
		this.sshUrl = sshUrl;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	@Override
	public String toString() {
		return "ProjectDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", webUrl='" + webUrl + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", gitSshUrl='" + gitSshUrl + '\'' +
				", gitHttpUrl='" + gitHttpUrl + '\'' +
				", namespace='" + namespace + '\'' +
				", visibilityLevel='" + visibilityLevel + '\'' +
				", pathWithNamespace='" + pathWithNamespace + '\'' +
				", defaultBranch='" + defaultBranch + '\'' +
				", homepage='" + homepage + '\'' +
				", url='" + url + '\'' +
				", sshUrl='" + sshUrl + '\'' +
				", httpUrl='" + httpUrl + '\'' +
				'}';
	}
}
