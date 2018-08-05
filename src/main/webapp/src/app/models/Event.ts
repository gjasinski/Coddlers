export class Event {
  private _eventType: string;
  private _eventData: number;

  constructor(eventType: string, eventData: number) {
    this._eventType = eventType;
    this._eventData = eventData;
  }

  get eventType(): string {
    return this._eventType;
  }

  get eventData(): number {
    return this._eventData;
  }
}
