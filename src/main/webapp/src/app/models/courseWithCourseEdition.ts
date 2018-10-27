import {CourseEdition} from "./courseEdition";
import {Course} from "./course";

export class CourseWithCourseEdition {
  private _course: Course;
  private _courseEdition: CourseEdition;
  private _submittedTasks: Number;
  private _gradedTasks: Number;
  private _allTasks: Number;


  constructor(course: Course, courseEdition: CourseEdition, submittedTasks: Number, gradedTasks: Number, allTasks: Number) {
    this._course = course;
    this._courseEdition = courseEdition;
    this._submittedTasks = submittedTasks;
    this._gradedTasks = gradedTasks;
    this._allTasks = allTasks;
  }

  get course(): Course {
    return this._course;
  }

  get courseEdition(): CourseEdition {
    return this._courseEdition;
  }


  get submittedTasks(): Number {
    return this._submittedTasks;
  }

  get gradedTasks(): Number {
    return this._gradedTasks;
  }

  get allTasks(): Number {
    return this._allTasks;
  }

  public static fromJSON(jsonObj: any): CourseWithCourseEdition {
    return new CourseWithCourseEdition(jsonObj.course, jsonObj.courseEdition, jsonObj.submittedTasks, jsonObj.gradedTasks, jsonObj.allTasks);
  }

  public toJSON() {
    return {
      course: this.course,
      courseEdition: this.courseEdition,
      submittedTasks: this.submittedTasks,
      gradedTasks: this.gradedTasks,
      allTasks: this.allTasks
    }
  }
}
