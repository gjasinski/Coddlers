import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {Assignment} from "../models/assignment";
import {Subject} from "rxjs/internal/Subject";

@Injectable()
export class AssignmentService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  private subject: Subject<Assignment[]> = new Subject();

  constructor(private http: HttpClient) {}

  public getAssignments(courseId: number): Observable<Assignment[]> {
    this.http.get<Assignment[]>(`api/lessons?courseId=${courseId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Assignment.fromJSON(obj))),
        tap((assignments: Assignment[]) => {
          this.subject.next(assignments);
        })
      ).subscribe();

    return this.subject.asObservable();
  }

  public createAssignment(assigment: Assignment): Observable<any> {
    return this.http.post('api/lessons', assigment.toJSON(), this.httpOptions);
  }

  public updateAssignment(id: number, assigment: Assignment): Observable<any> {
    return this.http.put(`api/lessons/${id}`, assigment.toJSON(), this.httpOptions)
      .pipe(
        map((obj: any) => {
          return Assignment.fromJSON(obj);
        })
      );
  }

  public getAssignment(assignmentId: number): Observable<Assignment> {
    return this.http.get<Assignment>(`api/lessons/${assignmentId}`)
      .pipe(
        map((obj: any) => {
          return Assignment.fromJSON(obj);
        })
      );
  }

}
