package pl.coddlers.git.models.event;

public class CommitInfoDTO {
	private String id;
	private String message;
	private String timestamp;
	private String url;
	private CommitInfoDTO author;

	public CommitInfoDTO(String id, String message, String timestamp, String url, CommitInfoDTO author) {
		this.id = id;
		this.message = message;
		this.timestamp = timestamp;
		this.url = url;
		this.author = author;
	}

	public CommitInfoDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CommitInfoDTO getAuthor() {
		return author;
	}

	public void setAuthor(CommitInfoDTO author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "CommitInfoDTO{" +
				"id='" + id + '\'' +
				", message='" + message + '\'' +
				", timestamp='" + timestamp + '\'' +
				", url='" + url + '\'' +
				", author=" + author +
				'}';
	}
}
