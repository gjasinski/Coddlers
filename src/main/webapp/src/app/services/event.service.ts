import { Injectable } from '@angular/core';
import {Subject} from "rxjs/internal/Subject";
import {Observable} from "rxjs/internal/Observable";
import {Event} from "../models/event";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private _events: Subject<Event> = new Subject<Event>();

  constructor() {}

  emit(value: Event): void {
    this._events.next(value);
  }

  get events(): Observable<Event> {
    return this._events.asObservable();
  }
}
