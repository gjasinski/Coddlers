package pl.coddlers.core.models.dto;

import lombok.Data;

@Data
public class InvitationDto {

    private InvitationLinkDto invitationLink;

    private String[] studentEmails;
}
