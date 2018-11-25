import {SubmissionStatus} from "./submissionStatusEnum";

export class Submission {
  private _id: number;
  private _taskId: number;
  private _userId: number;
  private _courseEditionId: number;
  private _userFullName: string;
  private _submissionTime: Date;
  private _submissionStatusType: SubmissionStatus;
  private _points: number;

  constructor(id: number, taskId: number, userId: number, userFullName: string, courseEditionId: number, submissionTime: Date, submissionStatusType: SubmissionStatus, points: number) {
    this._id = id;
    this._taskId = taskId;
    this._userId = userId;
    this._userFullName = userFullName;
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

  get userFullName(): string {
    return this._userFullName;
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

  set points(points: number) {
    this._points = points;
  }

  get submissionStatusType(): SubmissionStatus {
    return this._submissionStatusType;
  }

  set submissionStatusType(submissionStatusType: SubmissionStatus) {
    this._submissionStatusType = submissionStatusType;
  }

  public static fromJSON(jsonObj: any): Submission {
    return new Submission(+jsonObj.id, +jsonObj.taskId, +jsonObj.userId, jsonObj.userFullName, +jsonObj.courseEditionId,
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
