import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {CourseEdition} from "../models/courseEdition";
import {Course} from "../models/course";

@Injectable()
export class CourseEditionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  getCourseEdition(editionId: number): Observable<CourseEdition> {
    return this.http.get<CourseEdition>(`api/editions/${editionId}`)
      .pipe(
        map(obj => CourseEdition.fromJSON(obj))
      )
  }

  public getCourses(): Observable<CourseEdition[]> {
    return this.http.get<CourseEdition[]>('api/editions')
      .pipe(
        map((objArray: any[]) => objArray.map(obj => CourseEdition.fromJSON(obj)))
      );
  }
}
