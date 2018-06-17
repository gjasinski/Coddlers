export class Course {
  private _id: number;
  private _title: string;
  private _description: string;
  private _startDate: Date;
  private _endDate: Date;

  constructor(id: number, title: string, description: string, startDate: Date, endDate: Date) {
    this._id = id;
    this._title = title;
    this._description = description;
    this._startDate = startDate;
    this._endDate = endDate;
  }

  get id(): number {
    return this._id;
  }

  get title(): string {
    return this._title;
  }

  get description(): string {
    return this._description;
  }

  get startDate(): Date {
    return this._startDate;
  }

  get endDate(): Date {
    return this._endDate;
  }

  public static fromJSON(jsonObj: any): Course {
    return new Course(+jsonObj.id, jsonObj.title, jsonObj.description,
      new Date(jsonObj.startDate), new Date(jsonObj.endDate));
  }

  public toJSON() {
    return {
      id: this.id,
      title: this.title,
      description: this.description,
      startDate: this.startDate,
      endDate: this.endDate
    }
  }
}
