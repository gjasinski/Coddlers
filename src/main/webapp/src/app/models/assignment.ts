export class Assignment {
  private _id: number;
  private _courseId: number;
  private _title: string;
  private _description: string;
  private _weight: number;
  private _startDate: Date;
  private _dueDate: Date;

  constructor(id: number, courseId: number, title: string, description: string, weight: number,
              startDate: Date, dueDate: Date) {
    this._id = id;
    this._courseId = courseId;
    this._title = title;
    this._description = description;
    this._weight = weight;
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

  get weight(): number {
    return this._weight;
  }

  get startDate(): Date {
    return this._startDate;
  }

  get dueDate(): Date {
    return this._dueDate;
  }

  public static fromJSON(jsonObj: any): Assignment {
    return new Assignment(+jsonObj.id, +jsonObj.courseId, jsonObj.title, jsonObj.description,
      +jsonObj.weight, jsonObj.startDate, jsonObj.dueDate);
  }

  public toJSON() {
    return {
      id: this.id,
      courseId: this.courseId,
      title: this.title,
      description: this.description,
      weight: this.weight,
      startDate: this.startDate,
      dueDate: this.dueDate
    };
  }
}
