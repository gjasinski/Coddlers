import {CourseVersion} from "./courseVersion";

export class CourseEdition {
  private _id: number;
  private _title: string;
  private _courseVersion: CourseVersion;
  private _startDate: Date;

  constructor(id: number, title: string, courseVersion: CourseVersion, startDate: Date) {
    this._id = id;
    this._title = title;
    this._courseVersion = courseVersion;
    this._startDate = startDate;
  }

  get id(): number {
    return this._id;
  }

  get title(): string {
    return this._title;
  }

  get courseVersion(): CourseVersion {
    return this._courseVersion;
  }

  get startDate(): Date {
    return this._startDate;
  }

  public static fromJSON(jsonObj: any): CourseEdition {
    return new CourseEdition(+jsonObj.id, jsonObj.title, jsonObj.courseVersion, new Date(jsonObj.startDate));
  }

  public toJSON() {
    return {
      id: this.id,
      title: this.title,
      courseVersion: this.courseVersion.toJSON(),
      startDate: new Date().getTime(),
    }
  }
}
