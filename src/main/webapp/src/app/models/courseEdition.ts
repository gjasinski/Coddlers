export class CourseEdition {
  private _id: number;
  private _title: string;
  private _version: number;
  private _startDate: Date;
  private _endDate: Date;

  constructor(id: number, title: string, version: number, startDate: Date, endDate: Date) {
    this._id = id;
    this._title = title;
    this._version = version;
    this._startDate = startDate;
    this._endDate = endDate;
  }

  get id(): number {
    return this._id;
  }

  get title(): string {
    return this._title;
  }

  get version(): number {
    return this._version;
  }

  get startDate(): Date {
    return this._startDate;
  }

  get endDate(): Date {
    return this._endDate;
  }

  public static fromJSON(jsonObj: any): CourseEdition {
    return new CourseEdition(+jsonObj.id, jsonObj.title, +jsonObj.version,
      new Date(jsonObj.startDate), new Date(jsonObj.endDate));
  }

  public toJSON() {
    return {
      id: this.id,
      title: this.title,
      description: this.version,
      startDate: this.startDate,
      endDate: this.endDate
    }
  }
}
