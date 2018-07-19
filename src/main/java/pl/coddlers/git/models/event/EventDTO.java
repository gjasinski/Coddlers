package pl.coddlers.git.models.event;

import java.util.Arrays;

public class EventDTO {
	private String object_kind;
	private String before;
	private String after;
	private String ref;
	private String checkout_sha;
	private Long user_id;
	private String user_name;
	private String user_username;
	private String user_email;
	private String user_avatar;
	private Long project_id;
	private ProjectDTO projectDTO;
	private RepositoryDTO repositoryDTO;
	private CommitDTO[] commits;
	private Integer total_commits_count;

	public EventDTO(String object_kind, String before, String after, String ref, String checkout_sha, Long user_id, String user_name, String user_username, String user_email, String user_avatar, Long project_id, ProjectDTO projectDTO, RepositoryDTO repositoryDTO, CommitDTO[] commits, Integer total_commits_count) {
		this.object_kind = object_kind;
		this.before = before;
		this.after = after;
		this.ref = ref;
		this.checkout_sha = checkout_sha;
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_username = user_username;
		this.user_email = user_email;
		this.user_avatar = user_avatar;
		this.project_id = project_id;
		this.projectDTO = projectDTO;
		this.repositoryDTO = repositoryDTO;
		this.commits = commits;
		this.total_commits_count = total_commits_count;
	}


	public EventDTO() {
	}

	public String getObject_kind() {
		return object_kind;
	}

	public void setObject_kind(String object_kind) {
		this.object_kind = object_kind;
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

	public String getCheckout_sha() {
		return checkout_sha;
	}

	public void setCheckout_sha(String checkout_sha) {
		this.checkout_sha = checkout_sha;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_username() {
		return user_username;
	}

	public void setUser_username(String user_username) {
		this.user_username = user_username;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public ProjectDTO getProjectDTO() {
		return projectDTO;
	}

	public void setProjectDTO(ProjectDTO projectDTO) {
		this.projectDTO = projectDTO;
	}

	public RepositoryDTO getRepositoryDTO() {
		return repositoryDTO;
	}

	public void setRepositoryDTO(RepositoryDTO repositoryDTO) {
		this.repositoryDTO = repositoryDTO;
	}

	public CommitDTO[] getCommits() {
		return commits;
	}

	public void setCommits(CommitDTO[] commits) {
		this.commits = commits;
	}

	public Integer getTotal_commits_count() {
		return total_commits_count;
	}

	public void setTotal_commits_count(Integer total_commits_count) {
		this.total_commits_count = total_commits_count;
	}

	@Override
	public String toString() {
		return "PushEventDTO{" +
				"object_kind='" + object_kind + '\'' +
				", before='" + before + '\'' +
				", after='" + after + '\'' +
				", ref='" + ref + '\'' +
				", checkout_sha='" + checkout_sha + '\'' +
				", user_id=" + user_id +
				", user_name='" + user_name + '\'' +
				", user_username='" + user_username + '\'' +
				", user_email='" + user_email + '\'' +
				", user_avatar='" + user_avatar + '\'' +
				", project_id=" + project_id +
				", projectDTO=" + projectDTO +
				", repositoryDTO=" + repositoryDTO +
				", commits=" + Arrays.toString(commits) +
				", total_commits_count=" + total_commits_count +
				'}';
	}
}
