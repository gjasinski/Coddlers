import {Enum, EnumValue} from 'ts-enums';

export class SubmissionEnumStatus extends EnumValue {
  constructor(name: string) {
    super(name);
  }

  public toString(){
    return this.description.toString().toUpperCase().replace(/[_]/g, " ");
  }
}

export class SubmissionStatus extends Enum<SubmissionEnumStatus> {

  NOT_SUBMITTED: SubmissionEnumStatus = new SubmissionEnumStatus('NOT_SUBMITTED');
  GRADED: SubmissionEnumStatus = new SubmissionEnumStatus('GRADED');
  WAITING_FOR_REVIEW: SubmissionEnumStatus = new SubmissionEnumStatus('WAITING_FOR_REVIEW');
  CHANGES_REQUESTED: SubmissionEnumStatus = new SubmissionEnumStatus('CHANGES_REQUESTED');

  constructor() {
    super();
    this.initEnum('SubmissionStatus');
  }

  public static getEnumFromString(str: string) : SubmissionEnumStatus {
    for (const s of SubmissionStatusEnum.values) {
      if(s.propName.toLowerCase() === str.toLowerCase()){
        return s;
      }
    }
    return null;
  }
}

export const SubmissionStatusEnum: SubmissionStatus = new SubmissionStatus();
