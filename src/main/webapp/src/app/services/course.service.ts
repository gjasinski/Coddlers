import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Course} from "../models/course";

@Injectable()
export class CourseService {

  constructor(private http: HttpClient) { }

  public getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>('api/courses?startsAt&number')
      .pipe(
         map((objArray: any[]) => objArray.map(obj => Course.fromJSON(obj)))
      );
  }

}
