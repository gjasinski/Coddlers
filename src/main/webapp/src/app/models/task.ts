export class Task {
  private _id: number;
  private _lessonId: number;
  private _title: string;
  private _description: string;
  private _maxPoints: number;
  private _isCodeTask: boolean;
  private _branchNamePrefix: string;

  constructor(id: number, lessonId: number, title: string, description: string, maxPoints: number, isCodeTask: boolean, branchNamePrefix: string) {
    this._id = id;
    this._lessonId = lessonId;
    this._title = title;
    this._description = description;
    this._maxPoints = maxPoints;
    this._isCodeTask = isCodeTask;
    this._branchNamePrefix = branchNamePrefix;
  }

  get id(): number {
    return this._id;
  }

  get lessonId(): number {
    return this._lessonId;
  }

  get title(): string {
    return this._title;
  }

  get description(): string {
    return this._description;
  }

  get maxPoints(): number {
    return this._maxPoints;
  }

  get isCodeTask(): boolean {
    return this._isCodeTask;
  }

  get branchNamePrefix(): string {
    return this._branchNamePrefix;
  }

  public static fromJSON(jsonObj: any): Task {
    return new Task(+jsonObj.id, +jsonObj.lessonId, jsonObj.title,
      jsonObj.description, +jsonObj.maxPoints, jsonObj.isCodeTask, jsonObj.branchNamePrefix);
  }

  public toJSON() {
    return {
      id: this.id,
      lessonId: this.lessonId,
      title: this.title,
      description: this.description,
      maxPoints: this.maxPoints,
      isCodeTask: this.isCodeTask,
      branchNamePrefix: this.branchNamePrefix
    }
  }
}
