export class SubmissionStatusType {
  private _name: string;

  constructor(name: string) {
    this._name = name;
  }

  get name(): string {
    return this._name;
  }

  public static fromJSON(jsonObj: any): SubmissionStatusType {
    return new SubmissionStatusType(jsonObj.name);
  }

  public toJSON() {
    return {
      id: this.name
    }
  }
}
