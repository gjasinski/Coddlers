import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Submission} from "../models/submission";

@Injectable()
export class SubmissionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) {
  }

  public getSubmissions(taskId: number, courseEditionId: number): Observable<Submission[]> {
    return this.http.get<Submission[]>(`/api/submissions?taskId=${taskId}&courseEditionId=${courseEditionId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Submission.fromJSON(obj)))
      );
  }

  public createComment(submissionId: number, comment: string): Observable<any> {
    return this.http.post(`api/submissions/${submissionId}/comments`,
      {comment: comment},
      this.httpOptions);
  }

  public gradeSubmission(submissionId: number, comment: string, points: number): Observable<any> {
    return this.http.post(`api/submissions/${submissionId}/grade`,
      {comment: comment, points: points},
      this.httpOptions);
  }

  public reopenSubmission(submissionId: number, reason: string): Observable<any> {
    return this.http.post(`api/submissions/${submissionId}/reopen`,
      {reason: reason},
      this.httpOptions);
  }

  public requestChangesForSubmission(submissionId: number, reason: string): Observable<any> {
    return this.http.post(`api/submissions/${submissionId}/request-changes`,
      {reason: reason},
      this.httpOptions);
  }

  public getSubmissionsForLesson(courseEditionId: number, lessonId: number): Observable<Submission[]> {
    return this.http.get<Submission[]>(`/api/submissions?courseEditionId=${courseEditionId}&lessonId=${lessonId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Submission.fromJSON(obj)))
      );
  }
}
