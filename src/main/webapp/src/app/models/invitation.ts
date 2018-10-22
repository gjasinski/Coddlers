export class Invitation {
  private _invitationLink: string;
  private _studentEmails: string[];

  constructor(invitationLink: string, studentEmails: string[]) {
    this._invitationLink = invitationLink;
    this._studentEmails = studentEmails;
  }

  get invitationLink(): string {
    return this._invitationLink;
  }

  get studentEmails(): string[] {
    return this._studentEmails;
  }

  public static fromJSON(jsonObj: any): Invitation {
    return new Invitation(jsonObj.invitationLink, jsonObj.studentEmails);
  }

  public toJSON() {
    return {
      invitationLink: this.invitationLink,
      studentEmails: this.studentEmails
    }
  }
}
