package pl.coddlers.git.models;

public class ResponseWithIdDto {

	private Long id;

	public ResponseWithIdDto(Long id) {
		this.id = id;
	}

	public ResponseWithIdDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CreateUserResponseDTO{" +
				"id=" + id +
				'}';
	}
}
