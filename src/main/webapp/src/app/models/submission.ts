import {User} from "./user";
import {Task} from "./task";
import {SubmissionStatusType} from "./submissionStatusType";

export class Submission {
  private _id: number;
  private _task: Task;
  private _author: User;
  private _submissionTime: Date;
  private _submissionStatusType: SubmissionStatusType;
  private _points: number;

  constructor(id: number, task: Task, author: User, submissionTime: Date, submissionStatusType: SubmissionStatusType, points: number) {
    this._id = id;
    this._task = task;
    this._author = author;
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

  get author(): User {
    return this._author;
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
    return new Submission(+jsonObj.id, jsonObj.task, jsonObj.author,
      new Date(jsonObj.submissionTime), jsonObj.submissionStatusType, +jsonObj.points);
  }

  public toJSON() {
    return {
      id: this.id,
      task: this.task,
      author: this.author,
      submissionTime: this.submissionTime,
      points: this.points,
      submissionStatusType: this.submissionStatusType
    }
  }
}
