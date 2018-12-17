import {CourseEdition} from "./courseEdition";
import {Course} from "./course";

export class CourseWithCourseEdition {
  private _course: Course;
  private _courseEdition: CourseEdition;
  private _submittedTasks: Number;
  private _gradedTasks: Number;
  private _allTasks: Number;
  private _submittedLessons: Number;
  private _gradedLessons: Number;
  private _lessonsSize: Number;

  constructor(course: Course, courseEdition: CourseEdition, submittedTasks: Number, gradedTasks: Number,
              allTasks: Number, submittedLessons: Number, gradedLessons: Number, lessonsSize: Number) {
    this._course = course;
    this._courseEdition = courseEdition;
    this._submittedTasks = submittedTasks;
    this._gradedTasks = gradedTasks;
    this._allTasks = allTasks;
    this._submittedLessons = submittedLessons;
    this._gradedLessons = gradedLessons;
    this._lessonsSize = lessonsSize;
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

  get submittedLessons(): Number {
    return this._submittedLessons;
  }

  get gradedLessons(): Number {
    return this._gradedLessons;
  }

  get lessonsSize(): Number {
    return this._lessonsSize;
  }

  public static fromJSON(jsonObj: any): CourseWithCourseEdition {
    console.log(jsonObj);

    return new CourseWithCourseEdition(jsonObj.course, jsonObj.courseEdition, jsonObj.submittedTasks, jsonObj.gradedTasks, jsonObj.allTasks,
      jsonObj.submittedLessons, jsonObj.gradedLessons, jsonObj.lessonsSize);
  }

  public toJSON() {
    return {
      course: this.course,
      courseEdition: this.courseEdition,
      submittedTasks: this.submittedTasks,
      gradedTasks: this.gradedTasks,
      allTasks: this.allTasks,
      submittedLessons: this.submittedLessons,
      gradedLessons: this.gradedLessons,
      lessonsSize: this.lessonsSize
    }
  }
}
