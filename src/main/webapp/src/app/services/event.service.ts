import { Injectable } from '@angular/core';
import {Subject} from "rxjs/internal/Subject";
import {Observable} from "rxjs/internal/Observable";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private _events: Subject<string> = new Subject<string>();

  constructor() {}

  emit(value: string): void {
    this._events.next(value);
  }

  get events(): Observable<string> {
    return this._events.asObservable();
  }
}
