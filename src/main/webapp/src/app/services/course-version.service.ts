import {Injectable} from '@angular/core';
import {Observable} from "rxjs/internal/Observable";
import {CourseVersion} from "../models/courseVersion";
import {map} from "rxjs/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Course} from "../models/course";

@Injectable({
  providedIn: 'root'
})
export class CourseVersionService {

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) {
  }

  public getCourseVersions(courseId: number): Observable<CourseVersion[]> {
    return this.http.get<CourseVersion[]>(`api/course-versions?courseId=${courseId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => CourseVersion.fromJSON(obj)))
      );
  }

  public createCourseVersion(course: Course): Observable<any> {
    return this.http.post(`api/course-versions`, course.toJSON(), this.httpOptions);
  }

}
