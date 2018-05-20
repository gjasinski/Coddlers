import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Assignment} from "../models/assignment";

@Injectable()
export class AssignmentService {

  constructor(private http: HttpClient) {}

  public getAssignments(courseId: number): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`api/assignments?courseId=${courseId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Assignment.fromJSON(obj)))
      );
  }

}
