package pl.coddlers.git.models;

public class ResponseWithIdDTO {

	private Long id;

	public ResponseWithIdDTO(Long id) {
		this.id = id;
	}

	public ResponseWithIdDTO() {
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
