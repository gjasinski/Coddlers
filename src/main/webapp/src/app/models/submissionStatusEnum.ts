import {Enum, EnumValue} from 'ts-enums';

export class Status extends EnumValue {
  constructor(name: string) {
    super(name);
  }

  public toDescription(): string{
    return this.description.toString().toUpperCase().replace(/[_]/g, " ");
  }

}

export class SubmissionStatus extends Enum<Status> {

  NOT_SUBMITTED: Status = new Status('NOT_SUBMITTED');
  GRADED: Status = new Status('GRADED');
  WAITING_FOR_REVIEW: Status = new Status('WAITING_FOR_REVIEW');
  CHANGES_REQUESTED: Status = new Status('CHANGES_REQUESTED');

  constructor() {
    super();
    this.initEnum('Status');
  }

  public static getEnumFromString(str: string) : Status {
    for (const s of SubmissionStatusEnum.values) {
      if(s.propName.toLowerCase() === str.toLowerCase()){
        return s;
      }
    }
    return null;
  }
}

export const SubmissionStatusEnum: SubmissionStatus = new SubmissionStatus();
