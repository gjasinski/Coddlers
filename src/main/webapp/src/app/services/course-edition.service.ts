import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {CourseEdition} from "../models/courseEdition";
import {Lesson} from "../models/lesson";
import {Subject} from "rxjs/internal/Subject";

@Injectable()
export class CourseEditionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  private subject: Subject<CourseEdition[]> = new Subject();

  constructor(private http: HttpClient) { }

  getCourseEdition(editionId: number): Observable<CourseEdition> {
    return this.http.get<CourseEdition>(`api/editions/${editionId}`)
      .pipe(
        map(obj => CourseEdition.fromJSON(obj))
      )
  }

  public getEditionsByCourseVersion(courseVersion: number): Observable<CourseEdition[]> {
    let courseVersionStr = (courseVersion === 0 || courseVersion === undefined || courseVersion === null) ? '' : `=${courseVersion}`;

    this.http.get<Lesson[]>(`api/course-versions/${courseVersion}/editions`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => CourseEdition.fromJSON(obj))),
        tap((edition: CourseEdition[]) => {
          this.subject.next(edition);
        })
      ).subscribe();

    return this.subject.asObservable();
  }
}
