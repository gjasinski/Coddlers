import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {CourseEdition} from "../models/courseEdition";

@Injectable()
export class CourseEditionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  getEdition(editionId: number): Observable<CourseEdition> {
    return this.http.get<CourseEdition>(`api/editions/${editionId}`)
      .pipe(
        map(obj => CourseEdition.fromJSON(obj))
      )
  }
}
