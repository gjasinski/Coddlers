package pl.coddlers.automation.model

class InvitationLink {
    String invitationLink
    String[] studentEmails

    InvitationLink(String invitationLink, String[] studentEmails) {
        this.invitationLink = invitationLink
        this.studentEmails = studentEmails
    }
}
