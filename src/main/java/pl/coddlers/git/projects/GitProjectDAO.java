package pl.coddlers.git.projects;

public class GitProjectDAO {
	private Long id;

	public GitProjectDAO(Long id) {
		this.id = id;
	}

	public GitProjectDAO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
