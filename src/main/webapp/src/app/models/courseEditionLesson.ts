export class CourseEditionLesson {
  private _id: number;
  private _startDate: Date;
  private _endDate: Date;
  private _courseEditionId: number;
  private _lessonId: number;

  constructor(id: number, startDate: Date, endDate: Date, courseEditionId: number, lessonId: number) {
    this._id = id;
    this._startDate = startDate;
    this._endDate = endDate;
    this._courseEditionId = courseEditionId;
    this._lessonId = lessonId;
  }

  get id(): number {
    return this._id;
  }

  get startDate(): Date {
    return this._startDate;
  }

  get endDate(): Date {
    return this._endDate;
  }

  get courseEditionId(): number {
    return this._courseEditionId;
  }

  get lessonId(): number {
    return this._lessonId;
  }

  public static fromJSON(jsonObj: any): CourseEditionLesson {
    return new CourseEditionLesson(+jsonObj.id, new Date(jsonObj.startDate), new Date(jsonObj.endDate),
      +jsonObj.courseEditionId, +jsonObj.lessonId);
  }

  public toJSON(): any {
    return {
      id: this.id,
      startDate: this.startDate,
      endDate: this.endDate,
      courseEditionId: this.courseEditionId,
      lessonId: this.lessonId
    };
  }
}
