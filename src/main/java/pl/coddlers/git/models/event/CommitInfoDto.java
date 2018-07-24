package pl.coddlers.git.models.event;

import lombok.Data;

@Data
class CommitInfoDto {
    private String id;
    private String message;
    private String timestamp;
    private String url;
    private CommitInfoDto author;

}
