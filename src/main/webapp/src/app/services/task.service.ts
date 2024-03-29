import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Task} from "../models/task";

@Injectable()
export class TaskService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  public getTasks(lessonId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`/api/tasks?lessonId=${lessonId}`)
      .pipe(
        map((objArray: any[]) => objArray.map(obj => Task.fromJSON(obj)))
      );
  }

  public getTask(taskId: number): Observable<Task> {
    return this.http.get<Task>(`/api/tasks/${taskId}`)
      .pipe(
        map(obj => Task.fromJSON(obj))
      )
  }

  public createTask(task: Task): Observable<any> {
    return this.http.post('/api/tasks', task.toJSON(), this.httpOptions);
  }

  public updateTask(task: Task): Observable<any> {
    return this.http.put(`/api/tasks/${task.id}`, task.toJSON(), this.httpOptions);
  }

}
