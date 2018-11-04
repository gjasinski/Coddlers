import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Submission} from "../models/submission";

@Injectable()
export class SubmissionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  public getSubmissions(taskId: number): Observable<Submission[]> {
    return this.http.get<Submission[]>(`/api/submissions?taskId=${taskId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Submission.fromJSON(obj)))
      );
  }

  public getSubmissionsForLesson(courseEditionId: number, lessonId: number): Observable<Submission[]> {
    return this.http.get<Submission[]>(`/api/submissions?courseEditionId=${courseEditionId}&lessonId=${lessonId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Submission.fromJSON(obj)))
      );
  }
}
