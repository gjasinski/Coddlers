package pl.coddlers.git.models.event;

public class CommitAuthorDto {
	private String name;
	private String email;

	public CommitAuthorDto(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public CommitAuthorDto() {
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
