import {User} from "./user";
import {Task} from "./task";
import {SubmissionStatusType} from "./submissionStatusType";

export class Submission {
  private _id: number;
  private _task: Task;
  private _user: User;
  private _submissionTime: Date;
  private _submissionStatusType: SubmissionStatusType;
  private _points: number;

  constructor(id: number, task: Task, user: User, submissionTime: Date, submissionStatusType: SubmissionStatusType, points: number) {
    this._id = id;
    this._task = task;
    this._user = user;
    this._submissionTime = submissionTime;
    this._submissionStatusType = submissionStatusType;
    this._points = points;
  }

  get id(): number {
    return this._id;
  }

  get task(): Task {
    return this._task;
  }

  get user(): User {
    return this._user;
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
    return new Submission(+jsonObj.id, jsonObj.task, jsonObj.user,
      new Date(jsonObj.submissionTime), jsonObj.submissionStatusType, +jsonObj.points);
  }

  public toJSON() {
    return {
      id: this.id,
      task: this.task,
      user: this.user,
      submissionTime: this.submissionTime,
      points: this.points,
      submissionStatusType: this.submissionStatusType
    }
  }
}
