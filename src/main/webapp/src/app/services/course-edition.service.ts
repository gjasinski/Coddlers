import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {CourseEdition} from "../models/courseEdition";
import {Lesson} from "../models/lesson";
import {Subject} from "rxjs/internal/Subject";
import {CourseWithCourseEdition} from "../models/courseWithCourseEdition";
import {CourseEditionLesson} from "../models/courseEditionLesson";
import {InvitationLink} from "../models/invitationLink";
import {Invitation} from "../models/invitation";

@Injectable()
export class CourseEditionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  private subject: Subject<CourseEdition[]> = new Subject();

  constructor(private http: HttpClient) {
  }

  public getCourseEdition(editionId: number): Observable<CourseEdition> {
    return this.http.get<CourseEdition>(`api/editions/${editionId}`)
      .pipe(
        map(obj => CourseEdition.fromJSON(obj))
      )
  }

  public getCourseEditionLesson(editionId: number, lessonId: number): Observable<CourseEditionLesson> {
    return this.getCourseEditionLessonList(editionId, lessonId).pipe(
      map((items: CourseEditionLesson[]) => {
        return items[0];
      })
    );
  }

  public getCourseEditionLessonList(editionId: number, lessonId?: number): Observable<CourseEditionLesson[]> {
    let lessonIdQuery = (lessonId === undefined || lessonId === null || lessonId <= 0) ? '' :
      `=${lessonId}`;

    return this.http.get<CourseEditionLesson[]>(`api/editions/${editionId}/course-edition-lessons?lessonId${lessonIdQuery}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => CourseEditionLesson.fromJSON(obj)))
      );
  }

  public updateCourseEditionLesson(id: number, courseEditionLesson: CourseEditionLesson): Observable<any> {
    return this.http.put(`api/editions/course-edition-lessons/${id}`, courseEditionLesson.toJSON(), this.httpOptions);
  }

  public getEditionsByCourseVersion(courseVersion: number): Observable<CourseEdition[]> {
    this.http.get<Lesson[]>(`api/course-versions/${courseVersion}/editions`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => CourseEdition.fromJSON(obj))),
        tap((edition: CourseEdition[]) => {
          this.subject.next(edition);
        })
      ).subscribe();

    return this.subject.asObservable();
  }

  public createCourseEdition(courseEdition: CourseEdition): Observable<any> {
    return this.http.post('api/editions', courseEdition.toJSON(), this.httpOptions);
  }

  public addToCourseEdition(invitationToken: string): Observable<any> {
    return this.http.post('api/editions/invitations', invitationToken, this.httpOptions);
  }

  public getCourses(): Observable<CourseWithCourseEdition[]> {
    return this.http.get<CourseWithCourseEdition[]>('api/editions')
      .pipe(
        map((objArray: any[]) => {
          console.error(objArray);
          return objArray.map(obj => CourseWithCourseEdition.fromJSON(obj))
        })
      );
  }

  public getInvitationLink(courseEditionId: number): Observable<InvitationLink> {
    return this.http.get<InvitationLink>(`api/editions/invitations?courseEditionId=${courseEditionId}`)
      .pipe(
        map(obj => InvitationLink.fromJSON(obj))
      )
  }

  public sendInvitation(invitation: Invitation): Observable<any> {
    return this.http.post(`api/editions/invitations/emails`, invitation.toJSON(), this.httpOptions);
  }
}
