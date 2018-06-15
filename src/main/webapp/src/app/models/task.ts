export class Task {
  private _id: number;
  private _assignmentId: number;
  private _title: string;
  private _description: string;
  private _weight: number;
  private _maxPoints: number;
  private _taskStatus: string;

  constructor(id: number, assignmentId: number, title: string, description: string, weight: number, maxPoints: number, taskStatus: string) {
    this._id = id;
    this._assignmentId = assignmentId;
    this._title = title;
    this._description = description;
    this._weight = weight;
    this._maxPoints = maxPoints;
    this._taskStatus = taskStatus;
  }

  get id(): number {
    return this._id;
  }

  get assignmentId(): number {
    return this._assignmentId;
  }

  get title(): string {
    return this._title;
  }

  get description(): string {
    return this._description;
  }

  get weight(): number {
    return this._weight;
  }

  get maxPoints(): number {
    return this._maxPoints;
  }

  get taskStatus(): string {
    return this._taskStatus;
  }

  public static fromJSON(jsonObj: any): Task {
    return new Task(+jsonObj.id, +jsonObj.assignmentId, jsonObj.title,
      jsonObj.description, +jsonObj.weight, +jsonObj.maxPoints, jsonObj.taskStatus.toLocaleLowerCase());
  }

  public toJSON() {
    return {
      id: this.id,
      assignmentId: this.assignmentId,
      title: this.title,
      description: this.description,
      weight: this.weight,
      maxPoints: this.maxPoints,
      taskStatus: this.taskStatus
    }
  }
}
