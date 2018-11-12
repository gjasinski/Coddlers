import {SubmissionStatusType} from "./submissionStatusType";

export class Submission {
  private _id: number;
  private _taskId: number;
  private _userId: number;
  private _courseEditionId: number;
  private _submissionTime: Date;
  private _submissionStatusType: SubmissionStatusType;
  private _points: number;

  constructor(id: number, taskId: number, userId: number, courseEditionId: number, submissionTime: Date, submissionStatusType: SubmissionStatusType, points: number) {
    this._id = id;
    this._taskId = taskId;
    this._userId = userId;
    this._courseEditionId = courseEditionId;
    this._submissionTime = submissionTime;
    this._submissionStatusType = submissionStatusType;
    this._points = points;
  }

  get id(): number {
    return this._id;
  }

  get taskId(): number {
    return this._taskId;
  }

  get userId(): number {
    return this._userId;
  }

  get courseEditionId(): number {
    return this._courseEditionId;
  }

  get submissionTime(): Date {
    return this._submissionTime;
  }

  get points(): number {
    return this._points;
  }

  get submissionStatusType(): SubmissionStatusType {
    return this._submissionStatusType;
  }

  public static fromJSON(jsonObj: any): Submission {
    return new Submission(+jsonObj.id, +jsonObj.taskId, +jsonObj.userId, +jsonObj.courseEditionId,
      new Date(jsonObj.submissionTime), jsonObj.submissionStatusType, +jsonObj.points);
  }

  public toJSON() {
    return {
      id: this.id,
      taskId: this.taskId,
      userId: this.userId,
      courseEditionId: this.courseEditionId,
      submissionTime: this.submissionTime,
      points: this.points,
      submissionStatusType: this.submissionStatusType
    }
  }
}
