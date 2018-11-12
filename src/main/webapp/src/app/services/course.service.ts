import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Course} from "../models/course";

@Injectable()
export class CourseService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  public getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>('api/courses')
      .pipe(
         map((objArray: any[]) => objArray.map(obj => Course.fromJSON(obj)))
      );
  }

  public getCourse(courseId: number): Observable<Course> {
    return this.http.get<Course>(`api/courses/${courseId}`)
      .pipe(
        map(obj => Course.fromJSON(obj))
      )
  }

  public getCourseByCourseEditionId(courseEditionId: number): Observable<Course> {
    return this.http.get<Course>(`api/courses?editionId=${courseEditionId}`)
      .pipe(
        map(obj => Course.fromJSON(obj))
      )
  }

  public createCourse(course: Course): Observable<any> {
    return this.http.post('api/courses', course.toJSON(), this.httpOptions);
  }

  public updateCourse(course: Course): Observable<any> {
    return this.http.put('api/courses', course.toJSON(), this.httpOptions);
  }

}
