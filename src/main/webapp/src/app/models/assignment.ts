export class Assignment {
  private _id: number;
  private _courseId: number;
  private _title: string;
  private _description: string;
  private _startDate: Date;
  private _dueDate: Date;

  constructor(id: number, courseId: number, title: string, description: string, startDate: Date, dueDate: Date) {
    this._id = id;
    this._courseId = courseId;
    this._title = title;
    this._description = description;
    this._startDate = startDate;
    this._dueDate = dueDate;
  }

  get id(): number {
    return this._id;
  }

  get courseId(): number {
    return this._courseId;
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

  get dueDate(): Date {
    return this._dueDate;
  }

  public static fromJSON(jsonObj: any): Assignment {
    return new Assignment(+jsonObj.id, +jsonObj.courseId, jsonObj.title, jsonObj.description,
      jsonObj.startDate, jsonObj.dueDate);
  }
}
