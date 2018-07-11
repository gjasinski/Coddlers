package pl.coddlers.git.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Hook {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private Long projectId;

	@Column(nullable = false)
	private String branch;

	public Hook(Long projectId, String branch) {
		this.projectId = projectId;
		this.branch = branch;
	}

	public Hook() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Hook hook = (Hook) o;
		return Objects.equals(id, hook.id) &&
				Objects.equals(projectId, hook.projectId) &&
				Objects.equals(branch, hook.branch);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, projectId, branch);
	}

	@Override
	public String toString() {
		return "Hook{" +
				"id=" + id +
				", projectId=" + projectId +
				", branch='" + branch + '\'' +
				'}';
	}
}
