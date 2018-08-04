import { Injectable } from '@angular/core';
import {Subject} from "rxjs/internal/Subject";
import {Observable} from "rxjs/internal/Observable";
import {Task} from "../models/task";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private _events: Subject<string> = new Subject<string>();
  private _eventsTask: Subject<Task> = new Subject<Task>();

  constructor() {}

  emit(value: string): void {
    this._events.next(value);
  }

  emitTask(task: Task): void {
    this._eventsTask.next(task);
  }

  get events(): Observable<string> {
    return this._events.asObservable();
  }

  get eventsTask(): Observable<Task> {
    return this._eventsTask.asObservable();
  }
}
