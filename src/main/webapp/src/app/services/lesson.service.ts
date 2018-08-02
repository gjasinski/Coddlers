import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {Lesson} from "../models/lesson";
import {Subject} from "rxjs/internal/Subject";

@Injectable()
export class LessonService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  private subject: Subject<Lesson[]> = new Subject();

  constructor(private http: HttpClient) {}

  public getLessons(courseId: number): Observable<Lesson[]> {
        return this.getLessonsByCourseVersion(courseId);
  }

  public getLessonsByCourseVersion(courseId: number, courseVersion?: number): Observable<Lesson[]> {
    let courseVersionStr = (courseVersion === 0 || courseVersion === undefined || courseVersion === null) ? '' : `=${courseVersion}`;

    this.http.get<Lesson[]>(`api/lessons?courseId=${courseId}&courseVersion${courseVersionStr}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Lesson.fromJSON(obj))),
        tap((lesson: Lesson[]) => {
          this.subject.next(lesson);
        })
      ).subscribe();

    return this.subject.asObservable();
  }

  public createLesson(assigment: Lesson): Observable<any> {
    return this.http.post('api/lessons', assigment.toJSON(), this.httpOptions);
  }

  public updateLesson(id: number, assigment: Lesson): Observable<any> {
    return this.http.put(`api/lessons/${id}`, assigment.toJSON(), this.httpOptions)
      .pipe(
        map((obj: any) => {
          return Lesson.fromJSON(obj);
        })
      );
  }

  public getLesson(lessonId: number): Observable<Lesson> {
    return this.http.get<Lesson>(`api/lessons/${lessonId}`)
      .pipe(
        map((obj: any) => {
          return Lesson.fromJSON(obj);
        })
      );
  }

}
