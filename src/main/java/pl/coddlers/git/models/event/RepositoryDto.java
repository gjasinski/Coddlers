package pl.coddlers.git.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryDto {
	private String name;
	private String url;
	private String description;
	private String homepage;

	@JsonProperty("git_http_url")
	private String gitHttpUrl;

	@JsonProperty("git_ssh_url")
	private String gitSshUrl;

	@JsonProperty("visibility_level")
	private Integer visibilityLevel;

	public RepositoryDto(String name, String url, String description, String homepage, String gitHttpUrl, String gitSshUrl, Integer visibilityLevel) {
		this.name = name;
		this.url = url;
		this.description = description;
		this.homepage = homepage;
		this.gitHttpUrl = gitHttpUrl;
		this.gitSshUrl = gitSshUrl;
		this.visibilityLevel = visibilityLevel;
	}

	public RepositoryDto() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getGitHttpUrl() {
		return gitHttpUrl;
	}

	public void setGitHttpUrl(String gitHttpUrl) {
		this.gitHttpUrl = gitHttpUrl;
	}

	public String getGitSshUrl() {
		return gitSshUrl;
	}

	public void setGitSshUrl(String gitSshUrl) {
		this.gitSshUrl = gitSshUrl;
	}

	public Integer getVisibilityLevel() {
		return visibilityLevel;
	}

	public void setVisibilityLevel(Integer visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}

	@Override
	public String toString() {
		return "RepositoryDTO{" +
				"name='" + name + '\'' +
				", url='" + url + '\'' +
				", description='" + description + '\'' +
				", homepage='" + homepage + '\'' +
				", gitHttpUrl='" + gitHttpUrl + '\'' +
				", gitSshUrl='" + gitSshUrl + '\'' +
				", visibilityLevel='" + visibilityLevel + '\'' +
				'}';
	}
}
