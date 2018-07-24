export class Submission {
  private _id: number;
  private _lessonId: number;
  private _author: string;
  private _submissionTime: Date;
  private _status: string;
  private _points: number;

  constructor(id: number, lessonId: number, author: string, submissionTime: Date, status: string, points: number) {
    this._id = id;
    this._lessonId = lessonId;
    this._author = author;
    this._submissionTime = submissionTime;
    this._status = status;
    this._points = points;
  }

  get id(): number {
    return this._id;
  }

  get lessonId(): number {
    return this._lessonId;
  }

  get author(): string {
    return this._author;
  }

  get submissionTime(): Date {
    return this._submissionTime;
  }

  get points(): number {
    return this._points;
  }

  get status(): string {
    return this._status;
  }

  public static fromJSON(jsonObj: any): Submission {
    return new Submission(+jsonObj.id, +jsonObj.lessonId, jsonObj.author,
      new Date(jsonObj.submissionTime), jsonObj.status.toLocaleLowerCase(), +jsonObj.points);
  }

  public toJSON() {
    return {
      id: this.id,
      lessonId: this.lessonId,
      author: this.author,
      submissionTime: this.submissionTime,
      points: this.points,
      status: this.status
    }
  }
}
