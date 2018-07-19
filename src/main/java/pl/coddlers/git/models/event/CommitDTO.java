package pl.coddlers.git.models.event;

public class CommitDTO {
	private CommitInfoDTO commitInfoDTO;
	private CommitAuthorDTO commitAuthorDTO;

	public CommitDTO(CommitInfoDTO commitInfoDTO, CommitAuthorDTO commitAuthorDTO) {
		this.commitInfoDTO = commitInfoDTO;
		this.commitAuthorDTO = commitAuthorDTO;
	}

	public CommitDTO() {
	}

	public CommitInfoDTO getCommitInfoDTO() {
		return commitInfoDTO;
	}

	public void setCommitInfoDTO(CommitInfoDTO commitInfoDTO) {
		this.commitInfoDTO = commitInfoDTO;
	}

	public CommitAuthorDTO getCommitAuthorDTO() {
		return commitAuthorDTO;
	}

	public void setCommitAuthorDTO(CommitAuthorDTO commitAuthorDTO) {
		this.commitAuthorDTO = commitAuthorDTO;
	}

	@Override
	public String toString() {
		return "CommitDTO{" +
				"commitInfoDTO=" + commitInfoDTO +
				", commitAuthorDTO=" + commitAuthorDTO +
				'}';
	}
}
