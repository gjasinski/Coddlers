export class SubmissionStatusType {
  private _name: string;

  constructor(name: string) {
    this._name = name;
  }

  get nameWithSpaces(): string {
    return this._name.toLowerCase().replace(/[_]/g, " ");
  }

  get nameWithUnderscores(): string {
    return this._name.toLowerCase();
  }

  public static fromJSON(jsonObj: any): SubmissionStatusType {
    return new SubmissionStatusType(jsonObj.name);
  }

  public toJSON() {
    return {
      id: this.nameWithUnderscores
    }
  }
}
