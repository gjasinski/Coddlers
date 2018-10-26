import {CourseEdition} from "./courseEdition";
import {Course} from "./course";

export class CourseWithCourseEdition {
  private _course: Course;
  private _courseEdition: CourseEdition;

  constructor(course: Course, courseEdition: CourseEdition) {
    this._course = course;
    this._courseEdition = courseEdition;
  }

  get course(): Course {
    return this._course;
  }

  get courseEdition(): CourseEdition {
    return this._courseEdition;
  }

  public static fromJSON(jsonObj: any): CourseWithCourseEdition {
    return new CourseWithCourseEdition(jsonObj.course, jsonObj.courseEdition);
  }

  public toJSON() {
    return {
      course: this.course,
      courseEdition: this.courseEdition,
    }
  }
}
