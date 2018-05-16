import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/index";

@Injectable()
export class CourseService {

  constructor(private http: HttpClient) { }

  // public getCourses(): Observable<Course> {
  //
  // }

}
