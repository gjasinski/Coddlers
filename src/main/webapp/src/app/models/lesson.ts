export class Lesson {
  private _id: number;
  private _title: string;
  private _description: string;
  private _weight: number;
  private _timeInDays: number;
  private _courseId: number;
  private _courseVersionNumber: number;

  constructor(id: number, title: string, description: string, weight: number, timeInDays: number, courseId: number,
              courseVersionNumber: number) {
    this._id = id;
    this._title = title;
    this._description = description;
    this._weight = weight;
    this._timeInDays = timeInDays;
    this._courseId = courseId;
    this._courseVersionNumber = courseVersionNumber;
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

  get weight(): number {
    return this._weight;
  }

  get timeInDays(): number {
    return this._timeInDays;
  }

  get courseId(): number {
    return this._courseId;
  }

  get courseVersionNumber(): number {
    return this._courseVersionNumber;
  }

  public static fromJSON(jsonObj: any): Lesson {
    return new Lesson(+jsonObj.id, jsonObj.title, jsonObj.description, +jsonObj.weight,
      +jsonObj.timeInDays, +jsonObj.courseId, +jsonObj.courseVersionNumber);
  }

  public toJSON() {
    return {
      id: this.id,
      title: this.title,
      description: this.description,
      weight: this.weight,
      timeInDays: this.timeInDays,
      courseId: this.courseId,
      courseVersionNumber: this.courseVersionNumber
    };
  }
}
