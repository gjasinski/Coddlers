import {Course} from "./course";
import {CourseVersion} from "./courseVersion";

export class CourseEdition {
  private _id: number;
  private _title: string;
  private _courseVersion: CourseVersion;
  private _startDate: Date;
  private _course: Course;

  constructor(id: number, title: string, courseVersion: CourseVersion, startDate: Date, course: Course) {
    this._id = id;
    this._title = title;
    this._courseVersion = courseVersion;
    this._startDate = startDate;
    this._course = course;
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

  get course(): Course {
    return this._course;
  }

  public static fromJSON(jsonObj: any): CourseEdition {
    return new CourseEdition(+jsonObj.id, jsonObj.title, jsonObj.courseVersion, new Date(jsonObj.startDate), jsonObj.course);
  }

  public toJSON() {
    return {
      id: this.id,
      title: this.title,
      description: this.courseVersion,
      startDate: this.startDate,
      course: this.course
    }
  }
}
