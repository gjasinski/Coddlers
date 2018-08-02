export class Course {
  private _id: number;
  private _title: string;
  private _description: string;

  constructor(id: number, title: string, description: string) {
    this._id = id;
    this._title = title;
    this._description = description;
  }

  get id(): number {
    return this._id;
  }

  get title(): string {
    return this._title;
  }

  get description(): string {
    return this._description;
  }

  public static fromJSON(jsonObj: any): Course {
    return new Course(+jsonObj.id, jsonObj.title, jsonObj.description);
  }

  public toJSON() {
    return {
      id: this.id,
      title: this.title,
      description: this.description
    }
  }
}
