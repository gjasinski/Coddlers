export class Event {
  private _eventType: string;
  private _eventData: any;

  constructor(eventType: string, eventData?: any) {
    this._eventType = eventType;
    this._eventData = eventData;
  }

  get eventType(): string {
    return this._eventType;
  }

  get eventData(): any {
    return this._eventData;
  }
}
