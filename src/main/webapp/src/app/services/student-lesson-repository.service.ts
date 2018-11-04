import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class StudentLessonRepositoryService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) {
  }

  public getLessonRepositoryUrl(courseEditionId: number, lessonId: number) {
    return this.http.get(`/api/repositories/students?courseEditionId=${courseEditionId}&lessonId=${lessonId}`, {responseType: 'text'});
  }


}
