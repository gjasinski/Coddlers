package pl.coddlers.git.models;

public class PushHookResponseDTO {

	private String object_kind;
	private String ref;
	private Long user_id;
	private String user_username;
	private Long project_id;

	public PushHookResponseDTO(String object_kind, String ref, Long user_id, String user_username, Long project_id) {
		this.object_kind = object_kind;
		this.ref = ref;
		this.user_id = user_id;
		this.user_username = user_username;
		this.project_id = project_id;
	}

	public PushHookResponseDTO() {
	}

	public String getObject_kind() {
		return object_kind;
	}

	public void setObject_kind(String object_kind) {
		this.object_kind = object_kind;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_username() {
		return user_username;
	}

	public void setUser_username(String user_username) {
		this.user_username = user_username;
	}

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	@Override
	public String toString() {
		return "PushHookDTO{" +
				"object_kind='" + object_kind + '\'' +
				", ref='" + ref + '\'' +
				", user_id=" + user_id +
				", user_username='" + user_username + '\'' +
				", project_id=" + project_id +
				'}';
	}
}
