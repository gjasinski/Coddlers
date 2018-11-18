import {Submission} from "./submission";
import {GitFileContent} from "./gitFileContent";

export class SubmissionData {
  private _fullName: string;
  private _submission: Submission;
  private _gitFileContents: GitFileContent[];


  constructor(fullName: string, submission: Submission, filesContent: GitFileContent[]) {
    this._fullName = fullName;
    this._submission = submission;
    this._gitFileContents = filesContent;
  }


  get fullName(): string {
    return this._fullName;
  }

  set fullName(value: string) {
    this._fullName = value;
  }

  get submission(): Submission {
    return this._submission;
  }

  set submission(value: Submission) {
    this._submission = value;
  }

  get gitFileContents(): GitFileContent[] {
    return this._gitFileContents;
  }

  set gitFileContents(value: GitFileContent[]) {
    this._gitFileContents = value;
  }

  public static fromJSON(jsonObj: any): SubmissionData {
    return new SubmissionData(jsonObj.fullName, jsonObj.submission, jsonObj.gitFileContents);
  }

  public toJSON() {
    return {
      fullName: this.fullName,
      submission: this.submission,
      gitFileContents: this.gitFileContents
    }
  }
}
