package pl.coddlers.git.models.event;

public class CommitDto {
	private CommitInfoDto commitInfoDTO;
	private CommitAuthorDto commitAuthorDTO;

	public CommitDto(CommitInfoDto commitInfoDTO, CommitAuthorDto commitAuthorDTO) {
		this.commitInfoDTO = commitInfoDTO;
		this.commitAuthorDTO = commitAuthorDTO;
	}

	public CommitDto() {
	}

	public CommitInfoDto getCommitInfoDTO() {
		return commitInfoDTO;
	}

	public void setCommitInfoDTO(CommitInfoDto commitInfoDTO) {
		this.commitInfoDTO = commitInfoDTO;
	}

	public CommitAuthorDto getCommitAuthorDTO() {
		return commitAuthorDTO;
	}

	public void setCommitAuthorDTO(CommitAuthorDto commitAuthorDTO) {
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
