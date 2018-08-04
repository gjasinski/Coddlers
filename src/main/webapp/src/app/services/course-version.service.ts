import {Injectable} from '@angular/core';
import {Observable} from "rxjs/internal/Observable";
import {CourseVersion} from "../models/courseVersion";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CourseVersionService {

  constructor(private http: HttpClient) {
  }

  public getCourseVersions(courseId: number): Observable<CourseVersion[]> {
    return this.http.get<CourseVersion[]>(`api/course-versions?courseId=${courseId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => CourseVersion.fromJSON(obj)))
      );
  }

}
