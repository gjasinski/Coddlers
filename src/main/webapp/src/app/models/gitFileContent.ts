export class GitFileContent {
  private _path: number;
  private _content: string;

  constructor(path: number, content: string) {
    this._path = path;
    this._content = content;
  }

  get path(): number {
    return this._path;
  }

  set path(value: number) {
    this._path = value;
  }

  get content(): string {
    return this._content;
  }

  set content(value: string) {
    this._content = value;
  }

  public static fromJSON(jsonObj: any): GitFileContent {
    return new GitFileContent(+jsonObj.path, jsonObj.content);
  }

  public toJSON() {
    return {
      path: this.path,
      content: this.content
    }
  }
}
