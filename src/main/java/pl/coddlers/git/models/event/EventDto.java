package pl.coddlers.git.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

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
	private ProjectDto projectDTO;

	@JsonProperty("repository")
	private RepositoryDto repositoryDTO;
	private CommitDto[] commits;

	@JsonProperty("total_commits_count")
	private Integer totalCommitsCount;

	public EventDto(String objectKind, String before, String after, String ref, String checkoutSha, Long userId, String userName, String userUsername, String userEmail, String userAvatar, Long projectId, ProjectDto projectDTO, RepositoryDto repositoryDTO, CommitDto[] commits, Integer totalCommitsCount) {
		this.objectKind = objectKind;
		this.before = before;
		this.after = after;
		this.ref = ref;
		this.checkoutSha = checkoutSha;
		this.userId = userId;
		this.userName = userName;
		this.userUsername = userUsername;
		this.userEmail = userEmail;
		this.userAvatar = userAvatar;
		this.projectId = projectId;
		this.projectDTO = projectDTO;
		this.repositoryDTO = repositoryDTO;
		this.commits = commits;
		this.totalCommitsCount = totalCommitsCount;
	}


	public EventDto() {
	}

	public String getObjectKind() {
		return objectKind;
	}

	public void setObjectKind(String objectKind) {
		this.objectKind = objectKind;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getCheckoutSha() {
		return checkoutSha;
	}

	public void setCheckoutSha(String checkoutSha) {
		this.checkoutSha = checkoutSha;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserUsername() {
		return userUsername;
	}

	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public ProjectDto getProjectDTO() {
		return projectDTO;
	}

	public void setProjectDTO(ProjectDto projectDTO) {
		this.projectDTO = projectDTO;
	}

	public RepositoryDto getRepositoryDTO() {
		return repositoryDTO;
	}

	public void setRepositoryDTO(RepositoryDto repositoryDTO) {
		this.repositoryDTO = repositoryDTO;
	}

	public CommitDto[] getCommits() {
		return commits;
	}

	public void setCommits(CommitDto[] commits) {
		this.commits = commits;
	}

	public Integer getTotalCommitsCount() {
		return totalCommitsCount;
	}

	public void setTotalCommitsCount(Integer totalCommitsCount) {
		this.totalCommitsCount = totalCommitsCount;
	}

	@Override
	public String toString() {
		return "PushEventDTO{" +
				"objectKind='" + objectKind + '\'' +
				", before='" + before + '\'' +
				", after='" + after + '\'' +
				", ref='" + ref + '\'' +
				", checkoutSha='" + checkoutSha + '\'' +
				", userId=" + userId +
				", userName='" + userName + '\'' +
				", userUsername='" + userUsername + '\'' +
				", userEmail='" + userEmail + '\'' +
				", userAvatar='" + userAvatar + '\'' +
				", projectId=" + projectId +
				", projectDTO=" + projectDTO +
				", repositoryDTO=" + repositoryDTO +
				", commits=" + Arrays.toString(commits) +
				", totalCommitsCount=" + totalCommitsCount +
				'}';
	}
}
