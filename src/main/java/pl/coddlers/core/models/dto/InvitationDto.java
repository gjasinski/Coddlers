package pl.coddlers.core.models.dto;

import lombok.Data;

@Data
public class InvitationDto {
    private String invitationLink;

    private String[] studentEmails;
}
