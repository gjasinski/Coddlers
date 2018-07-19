package pl.coddlers.git.models.event;

public class CommitAuthorDTO {
	private String name;
	private String email;

	public CommitAuthorDTO(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public CommitAuthorDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "CommitAuthorDTO{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
