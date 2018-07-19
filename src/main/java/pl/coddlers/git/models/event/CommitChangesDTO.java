package pl.coddlers.git.models.event;

import java.util.Arrays;

public class CommitChangesDTO {
	private String[] added;
	private String[] modified;
	private String[] removed;

	public CommitChangesDTO(String[] added, String[] modified, String[] removed) {
		this.added = added;
		this.modified = modified;
		this.removed = removed;
	}

	public CommitChangesDTO() {
	}

	public String[] getAdded() {
		return added;
	}

	public void setAdded(String[] added) {
		this.added = added;
	}

	public String[] getModified() {
		return modified;
	}

	public void setModified(String[] modified) {
		this.modified = modified;
	}

	public String[] getRemoved() {
		return removed;
	}

	public void setRemoved(String[] removed) {
		this.removed = removed;
	}

	@Override
	public String toString() {
		return "CommitChangesDTO{" +
				"added=" + Arrays.toString(added) +
				", modified=" + Arrays.toString(modified) +
				", removed=" + Arrays.toString(removed) +
				'}';
	}
}
