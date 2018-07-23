package pl.coddlers.git.models.event;

public class CommitInfoDto {
	private String id;
	private String message;
	private String timestamp;
	private String url;
	private CommitInfoDto author;

	public CommitInfoDto(String id, String message, String timestamp, String url, CommitInfoDto author) {
		this.id = id;
		this.message = message;
		this.timestamp = timestamp;
		this.url = url;
		this.author = author;
	}

	public CommitInfoDto() {
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

	public CommitInfoDto getAuthor() {
		return author;
	}

	public void setAuthor(CommitInfoDto author) {
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
