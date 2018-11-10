export class InvitationLink {
  private _link: string;

  constructor(link: string) {
    this._link = link;
  }

  get link(): string {
    return this._link;
  }

  public static fromJSON(jsonObj: any): InvitationLink {
    return new InvitationLink(jsonObj.link);
  }

  public toJSON() {
    return {
      link: this._link,
    }
  }
}
