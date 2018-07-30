export class CourseVersion {
  private _id: number;
  private _versionNumber: number;

  constructor(id: number, versionNumber: number) {
    this._id = id;
    this._versionNumber = versionNumber;
  }

  get id(): number {
    return this._id;
  }

  get versionNumber(): number {
    return this._versionNumber;
  }

  public static fromJSON(jsonObj: any): CourseVersion {
    return new CourseVersion(+jsonObj.id, +jsonObj.versionNumber);
  }

  public toJSON() {
    return {
      id: this.id,
      versionNumber: this.versionNumber
    }
  }
}
